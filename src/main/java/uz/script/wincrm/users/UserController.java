package uz.script.wincrm.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User rest api management controller")
public class UserController {
    private final UserService service;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @Operation(summary = "User create", description = "Only users with USER_CREATE permission can use it.")
    @ApiResponse(responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @ModelAttribute UserDTO dto) {
        UserResponse response = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                RestApiResponse.<UserResponse>builder()
                        .message("User successfully created")
                        .data(response)
                        .build()
        );
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    @Operation(summary = "Fetch all users", description = "Only users with USER_VIEW permission can use it.")
    @ApiResponse(responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
            ))
    public ResponseEntity<?> fetchAllUsers(){
        return ResponseEntity.ok().body(
                RestApiResponse.<List<UserResponse>>builder()
                        .message("All users fetched success ")
                        .data(service.fetchAllUsers())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    @Operation(summary = "Fetch user by id", description = "Only users with USER_VIEW permission can use it.")
    @ApiResponse(responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
            ))
    public ResponseEntity<?> findUserById(@PathVariable Long id){
        return ResponseEntity.ok().body(
                RestApiResponse.<UserResponse>builder()
                        .message("User found success")
                        .data(service.findById(id))
                        .build()
        );
    }

    @PutMapping(
            value = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasAuthority('USER_EDIT')")
    @Operation(
            summary = "Update user",
            description = "Only users with USER_EDIT permission can use it."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @ModelAttribute UserDTO dto
    ) {
        UserResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<UserResponse>builder()
                        .message("User successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @Operation(summary = "User delete", description = "Only users with USER_DELETE permission can use it.")
    public ResponseEntity<?> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok().body(
                RestApiResponse.<Void>builder().message("User successfully deleted").build()
        );
    }

    @PutMapping("/change/status/{id}")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    @Operation(summary = "Active or Disable user", description = "Only users with USER_EDIT permission can use it.")
    public ResponseEntity<?> changeStatus(@PathVariable Long id){
        service.activeOrDisabledUser(id);
        return ResponseEntity.ok().body(
                RestApiResponse.<Void>builder().message("User status successfully changed").build()
        );
    }

}
