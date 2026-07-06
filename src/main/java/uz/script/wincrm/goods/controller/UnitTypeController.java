package uz.script.wincrm.goods.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.goods.dto.UnitTypeDTO;
import uz.script.wincrm.goods.response.UnitTypeResponse;
import uz.script.wincrm.goods.service.UnitTypeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/unit-types")
@Tag(
        name = "Unit Type",
        description = "Unit Type management APIs"
)
public class UnitTypeController {

    private final UnitTypeService service;

    @Operation(summary = "Create Unit Type")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Unit Type successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UnitTypeResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "Unit Type already exists")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('UNIT_TYPE_CREATE')")
    public ResponseEntity<UnitTypeResponse> create(
            @Valid @RequestBody UnitTypeDTO dto) {

        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Update Unit Type")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = UnitTypeResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Unit Type not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UNIT_TYPE_EDIT')")
    public ResponseEntity<UnitTypeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UnitTypeDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Delete Unit Type")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Unit Type not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('UNIT_TYPE_DELETE')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get Unit Type by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            schema = @Schema(implementation = UnitTypeResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Unit Type not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('UNIT_TYPE_VIEW')")
    public ResponseEntity<UnitTypeResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Get all Unit Types")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UnitTypeResponse.class)
                            )
                    )
            )
    })
    @GetMapping
    @PreAuthorize("hasAuthority('UNIT_TYPE_VIEW')")
    public ResponseEntity<List<UnitTypeResponse>> getAll() {

        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get Unit Types with pagination")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            schema = @Schema(implementation = Page.class)
                    )
            )
    })
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('UNIT_TYPE_VIEW')")
    public ResponseEntity<Page<UnitTypeResponse>> getAll(
            Pageable pageable) {

        return ResponseEntity.ok(service.getAll(pageable));
    }

}