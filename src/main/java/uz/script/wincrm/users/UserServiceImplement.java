package uz.script.wincrm.users;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.BadRequestException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.roles.Role;
import uz.script.wincrm.roles.RoleRepository;
import uz.script.wincrm.roles.RoleResponse;
import uz.script.wincrm.storage.StorageService;
import uz.script.wincrm.utils.Status;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImplement implements UserService  {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    @Override
    @CacheEvict(value = "users", allEntries = true)
    @Auditable(
            action = AuditAction.CREATE,
            entity = "User"
    )
    public UserResponse create(UserDTO dto) {
        if (repository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));

        if (roles.size() != dto.getRoleIds().size()) {
            throw new RuntimeException("One or more roles not found");
        }
        String username = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getName();
        String photoLink = null;

        if (dto.getPhoto() != null && !dto.getPhoto().isEmpty()) {
            photoLink = "/api/files/" + storageService.uploadFile(dto.getPhoto());
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .phone(dto.getPhone())
                .photoLink(photoLink)
                .roles(roles)
                .status(Status.ACTIVE)
                .createdUsername(username)
                .build();

        User saved = repository.save(user);
        return mapUserToUserResponse(saved);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    @Auditable(
            action = AuditAction.READ,
            entity = "User"
    )
    public UserResponse findById(Long id) {
        log.info("Find user by user id {}", id);
        return mapUserToUserResponse(repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id " + id)));
    }

    @Override
    @Cacheable(value = "users")
    @Auditable(
            action = AuditAction.READ,
            entity = "User"
    )
    public List<UserResponse> fetchAllUsers() {
        log.info("Fetch all users");
        return repository.findAll().stream().map(this::mapUserToUserResponse).toList();
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "User"
    )
    public UserResponse update(Long id, UserDTO dto) {
        log.info("Update user with id {}", id);

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));

        if (roles.size() != dto.getRoleIds().size()) {
            throw new BadRequestException("One or more roles not found");
        }

        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id = " + id));

        user.setFullName(dto.getFullName());
        user.setPhone(dto.getPhone());
        user.setRoles(roles);

        // Password faqat yuborilgan bo'lsa yangilanadi
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Photo faqat yangi fayl yuborilgan bo'lsa yangilanadi
        if (dto.getPhoto() != null && !dto.getPhoto().isEmpty()) {
            String photoLink = "/api/files/" + storageService.uploadFile(dto.getPhoto());
            user.setPhotoLink(photoLink);
        }

        User updatedUser = repository.save(user);

        return mapUserToUserResponse(updatedUser);
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    @Auditable(
            action = AuditAction.DELETE,
            entity = "User"
    )
    public void delete(Long id) {
        log.info("Delete user by user id {}", id);
        User user = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id = " + id));
        user.setStatus(Status.DELETED);

    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "User"
    )
    public void activeOrDisabledUser(Long id) {
        log.info("Disable or active user with id {}" , id);
        User user = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id = " + id));
        if(user.getStatus() == Status.ACTIVE){
            log.info("Disabled user {}", user.getUsername());
            user.setStatus(Status.DISABLED);
        }
        if(user.getStatus() == Status.DISABLED){
            log.info("Activate user {}", user.getUsername());
            user.setStatus(Status.ACTIVE);
        }
    }

    private UserResponse mapUserToUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .role(
                        user.getRoles().stream()
                                .map(this::mapRoleToRoleResponse)
                                .collect(Collectors.toSet()))
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updateAt(user.getUpdatedAt())
                .photoLink(user.getPhotoLink())
                .build();
    }

    private RoleResponse mapRoleToRoleResponse(Role role){
        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getStatus(),
                role.getCreatedAt(),
                role.getUpdatedAt(),
                role.getCreatedUsername());
    }
}
