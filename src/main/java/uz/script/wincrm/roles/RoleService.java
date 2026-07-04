package uz.script.wincrm.roles;

import uz.script.wincrm.permissions.PermissionsResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleDTO dto);
    List<RoleResponse> fetchAllRoles();
    RoleResponse findById(Long id);
    void delete(Long id);
    RoleResponse update(Long id, RoleDTO dto);
    void setPermissionToRole(Long roleId, Long permissionId);
    void removePermissionFromRole(Long roleId, Long permissionId);
    List<PermissionsResponse> fetchPermissionsByRoleId(Long roleId);
    List<PermissionsResponse> fetchAllPermissions();
}
