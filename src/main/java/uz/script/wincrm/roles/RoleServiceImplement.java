package uz.script.wincrm.roles;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.script.wincrm.audit.AuditAction;
import uz.script.wincrm.audit.Auditable;
import uz.script.wincrm.exceptions.AlreadyExistsException;
import uz.script.wincrm.exceptions.ResourceNotFoundException;
import uz.script.wincrm.permissions.Permissions;
import uz.script.wincrm.permissions.PermissionsRepository;
import uz.script.wincrm.permissions.PermissionsResponse;
import uz.script.wincrm.utils.Status;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleServiceImplement implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionsRepository permissionsRepository;

    @Override
//    @CacheEvict(value = "roles", allEntries = true)
    @Auditable(
            action = AuditAction.CREATE,
            entity = "Role"
    )
    public RoleResponse create(RoleDTO dto) {

        log.info("Create role with name: {}", dto.getName());

        if (roleRepository.existsRoleByName(dto.getName())) {
            throw new AlreadyExistsException("Role already exists with name: " + dto.getName());
        }
        String username = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();

        Role role = Role.builder()
                .name(dto.getName())
                .status(Status.ACTIVE)
                .createdUsername(username)
                .build();

        Role savedRole = roleRepository.save(role);

        return mapRoleToRoleResponse(savedRole);
    }

    @Override
//    @Cacheable(value = "roles")
    @Auditable(
            action = AuditAction.READ,
            entity = "Role"
    )
    public List<RoleResponse> fetchAllRoles() {
        log.info("Fetch all roles");
        return roleRepository.findAll().stream().map(this::mapRoleToRoleResponse).toList();
    }

    @Override
//    @Cacheable(value = "roles", key = "#id")
    @Auditable(
            action = AuditAction.READ,
            entity = "Role"
    )
    public RoleResponse findById(Long id) {
        log.info("Find role by id {}", id);
        return mapRoleToRoleResponse(roleRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Role not found with id " + id)));
    }

    @Override
//    @CacheEvict(value = "roles", key = "#id")
    @Auditable(
            action = AuditAction.DELETE,
            entity = "Role"
    )
    public void delete(Long id) {
        log.info("Delete role by id {} ", id);
        Role role = roleRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Role not found with id " + id));
        role.setStatus(Status.DELETED);
    }

    @Override
//    @CachePut(value = "roles", key = "#result.id")
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "Role"
    )
    public RoleResponse update(Long id, RoleDTO dto) {
        log.info("Find role by id {}, for update", id);
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role with id not found"));
        log.info("Change role name {} to => {}", role.getName(), dto.getName());
        role.setName(dto.getName());
        return mapRoleToRoleResponse(role);
    }

    @Override
    @Transactional
//    @CacheEvict(value = {"roles", "permissions"}, allEntries = true)
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "Role"
    )
    public void setPermissionToRole(Long roleId, Long permissionId) {
        log.info("Set permission {} to role {}", permissionId, roleId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        Permissions permission = permissionsRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));

        if (role.getPermissions().contains(permission)) {
            throw new IllegalArgumentException("Permission already assigned to role");
        }

        role.getPermissions().add(permission);

        roleRepository.save(role);
    }

    @Override
    @Transactional
//    @CacheEvict(value = {"roles", "permissions"}, allEntries = true)
    @Auditable(
            action = AuditAction.UPDATE,
            entity = "Role"
    )
    public void removePermissionFromRole(Long roleId, Long permissionId) {
        log.info("Remove permission {} from role {}", permissionId, roleId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        Permissions permission = permissionsRepository.findById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));

        if (!role.getPermissions().contains(permission)) {
            throw new IllegalArgumentException("Permission is not assigned to role");
        }

        role.getPermissions().remove(permission);

        roleRepository.save(role);
    }

    @Override
//    @Cacheable(value = "permissions" ,key = "#roleId")
    public List<PermissionsResponse> fetchPermissionsByRoleId(Long roleId) {
        log.info("Fetch all permissions by role id {}", roleId);
        return permissionsRepository.findAllByRoleId(roleId)
                .stream()
                .map(this::mapToPermissionsToPermissionsResponse)
                .toList();
    }

    @Override
//    @Cacheable(value = "permissions")
    public List<PermissionsResponse> fetchAllPermissions() {
        log.info("Fetch all permissions");
        return permissionsRepository.findAll()
                .stream()
                .map(this::mapToPermissionsToPermissionsResponse)
                .toList();
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

    private PermissionsResponse mapToPermissionsToPermissionsResponse(Permissions permissions){
        return new PermissionsResponse(
                permissions.getId(),
                permissions.getName()
        );
    }
}
