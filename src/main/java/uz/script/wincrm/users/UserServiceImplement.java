package uz.script.wincrm.users;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.AlreadyExistsException;
import uz.script.wincrm.exceptions.BadRequestException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.roles.Role;
import uz.script.wincrm.roles.RoleRepository;
import uz.script.wincrm.roles.RoleResponse;
import uz.script.wincrm.storage.StorageService;
import uz.script.wincrm.utils.Status;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImplement implements UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    @Override
//    @Caching(evict = {
//            @CacheEvict(value = "users", allEntries = true)
//    })
    @Auditable(
            action = AuditAction.CREATE,
            entity = "User"
    )
    public UserResponse create(UserDTO dto) {

        log.info("Create user {}", dto.getUsername());

        if (repository.existsByUsername(dto.getUsername())) {
            throw new AlreadyExistsException(
                    "Username already exists: " + dto.getUsername()
            );
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));

        if (roles.size() != dto.getRoleIds().size()) {
            throw new BadRequestException("One or more roles not found");
        }


        String photoLink = null;

        if (dto.getPhoto() != null && !dto.getPhoto().isEmpty()) {
            photoLink = "/api/files/" + storageService.uploadFile(dto.getPhoto());
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .roles(roles)
                .photoLink(photoLink)
                .status(Status.ACTIVE)
                .build();

        repository.save(user);

        log.info("User {} successfully created", user.getUsername());

        return mapUserToUserResponse(user);
    }

    @Override
//    @Cacheable(value = "user", key = "#id")
    public UserResponse findById(Long id) {

        log.info("Find user by id {}", id);

        User user = repository.findByIdAndStatusNot(id, Status.DELETED)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        return mapUserToUserResponse(user);
    }

    @Override
//    @Cacheable(value = "users")
    public List<UserResponse> fetchAllUsers() {

        log.info("Fetch all users");

        return repository.findAllByStatusNot(Status.DELETED)
                .stream()
                .map(this::mapUserToUserResponse)
                .toList();
    }

    @Override
//    @Caching(evict = {
//            @CacheEvict(value = "user", key = "#id"),
//            @CacheEvict(value = "users", allEntries = true)
//    })
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "User"
    )
    public UserResponse update(Long id, UserDTO dto) {

        log.info("Update user {}", id);

        User user = repository.findByIdAndStatusNot(id, Status.DELETED)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));

        if (roles.size() != dto.getRoleIds().size()) {
            throw new BadRequestException("One or more roles not found");
        }

        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setRoles(roles);

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getPhoto() != null && !dto.getPhoto().isEmpty()) {

            if (user.getPhotoLink() != null) {
                storageService.delete(user.getPhotoLink().replace("/api/files/", ""));
            }

            user.setPhotoLink(
                    "/api/files/" + storageService.uploadFile(dto.getPhoto())
            );
        }

        log.info("User {} successfully updated", user.getUsername());

        return mapUserToUserResponse(user);
    }

    @Override
//    @Caching(evict = {
//            @CacheEvict(value = "user", key = "#id"),
//            @CacheEvict(value = "users", allEntries = true)
//    })
    @Auditable(
            action = AuditAction.DELETE,
            entity = "User"
    )
    public void delete(Long id) {

        log.info("Delete user {}", id);

        User user = repository.findByIdAndStatusNot(id, Status.DELETED)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        user.setStatus(Status.DELETED);

        log.info("User {} marked as deleted", user.getUsername());
    }

    @Override
//    @Caching(evict = {
//            @CacheEvict(value = "user", key = "#id"),
//            @CacheEvict(value = "users", allEntries = true)
//    })
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "User"
    )
    public void activeOrDisabledUser(Long id) {

        log.info("Change status for user {}", id);

        User user = repository.findByIdAndStatusNot(id, Status.DELETED)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id " + id));

        switch (user.getStatus()) {

            case ACTIVE -> {
                user.setStatus(Status.DISABLED);
                log.info("User {} disabled", user.getUsername());
            }

            case DISABLED -> {
                user.setStatus(Status.ACTIVE);
                log.info("User {} activated", user.getUsername());
            }

            default -> throw new BadRequestException(
                    "Status cannot be changed"
            );
        }
    }

    private UserResponse mapUserToUserResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .photoLink(user.getPhotoLink())
                .role(
                        user.getRoles()
                                .stream()
                                .map(this::mapRoleToRoleResponse)
                                .collect(Collectors.toSet())
                )
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updateAt(user.getUpdatedAt())
                .build();
    }

    private RoleResponse mapRoleToRoleResponse(Role role) {

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .status(role.getStatus())
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .createdUsername(role.getCreatedUsername())
                .build();
    }
}