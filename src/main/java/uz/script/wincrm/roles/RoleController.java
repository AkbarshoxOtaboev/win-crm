package uz.script.wincrm.roles;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.permissions.PermissionsResponse;
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Role rest api management controller")
public class RoleController {
    private final RoleService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    @Operation(summary = "Create role", description = "Only users with ROLE_CREATE permission can use it.")
    @ApiResponse(
            responseCode = "201",
            description = "Role saved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RoleResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody RoleDTO dto){
        RoleResponse response = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<RoleResponse>builder()
                                .message("Role saved successfully")
                                .data(response)
                                .build()
                );
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @Operation(summary = "Fetch all active roles", description = "Only users with ROLE_VIEW permission can use it.")
    @ApiResponse(responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = RoleResponse.class))
            )
    )
    public ResponseEntity<?> fetchAllRoles(){
        return ResponseEntity.status(HttpStatus.OK).body(
                RestApiResponse.<List<RoleResponse>>builder()
                        .message("All active roles successfully fetched")
                        .data(service.fetchAllRoles())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @Operation(summary = "Find role by id", description = "Only users with ROLE_VIEW permission can use it.")
    @ApiResponse(responseCode = "200",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RoleResponse.class)
        )
    )
    public ResponseEntity<?> fetchRoleById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(
                RestApiResponse.<RoleResponse>builder()
                        .message("Role founded successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_EDIT')")
    @Operation(summary = "Update role ", description = "Only users with ROLE_EDIT permission can use it.")
    @ApiResponse(responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RoleResponse.class)
            )
    )
    public ResponseEntity<?> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDTO dto){
        RoleResponse response = service.update(id, dto);
        return ResponseEntity.ok(
                RestApiResponse.<RoleResponse>builder()
                        .message("Role successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete role" , description = "Only users with ROLE_DELETE permission can use it.")
    public ResponseEntity<?> deleteRole(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                RestApiResponse.<Void>builder()
                        .message("Role deleted successfully")
                        .build()
        );
    }
    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('ROLE_EDIT')")
    @Operation(
            summary = "Assign permission to role",
            description = "Only users with ROLE_EDIT permission can assign a permission to a role."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Permission assigned successfully"
    )
    public ResponseEntity<?> setPermissionToRole(
            @PathVariable Long roleId,
            @PathVariable Long permissionId
    ) {

        service.setPermissionToRole(roleId, permissionId);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Permission assigned to role successfully")
                        .build()
        );
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('ROLE_EDIT')")
    @Operation(
            summary = "Remove permission from role",
            description = "Only users with ROLE_EDIT permission can remove a permission from a role."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Permission removed successfully"
    )
    public ResponseEntity<?> removePermissionFromRole(
            @PathVariable Long roleId,
            @PathVariable Long permissionId
    ) {

        service.removePermissionFromRole(roleId, permissionId);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Permission removed from role successfully")
                        .build()
        );
    }

    @GetMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @Operation(summary = "Fetch all permissions by role id", description ="Only users with ROLE_VIEW permission can fetch  permissions from a role.")
    @ApiResponse(responseCode = "200",
        content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = PermissionsResponse.class))
        )
    )
    public ResponseEntity<?> fetchPermissionsByRoleId(@PathVariable Long roleId){
        return ResponseEntity.ok().body(
                RestApiResponse.<List<PermissionsResponse>>builder()
                        .message(roleId + " role permissions fetched successfully")
                        .data(service.fetchPermissionsByRoleId(roleId))
                        .build()
        );
    }

    @GetMapping("/all/permissions")
    @PreAuthorize("hasAuthority('ROLE_VIEW')")
    @Operation(summary = "Fetch all permissions by role id", description ="Only users with ROLE_VIEW permission can fetch permissions from a role.")
    @ApiResponse(responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PermissionsResponse.class))
            )
    )
    public ResponseEntity<?> fetchAllPermissions(){
        return ResponseEntity.ok().body(
                RestApiResponse.<List<PermissionsResponse>>builder()
                        .message("All permissions")
                        .data(service.fetchAllPermissions())
                        .build()
        );
    }

}
