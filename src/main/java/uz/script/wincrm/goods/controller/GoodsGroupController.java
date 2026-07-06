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
import uz.script.wincrm.goods.dto.GoodsGroupDTO;
import uz.script.wincrm.goods.response.GoodsGroupResponse;
import uz.script.wincrm.goods.service.GoodsGroupService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goods-groups")
@Tag(
        name = "Goods Group",
        description = "Goods Group management APIs"
)
public class GoodsGroupController {

    private final GoodsGroupService service;

    @Operation(summary = "Create Goods Group")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Goods Group successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = GoodsGroupResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "409", description = "Goods Group already exists")
    })
    @PostMapping
    @PreAuthorize("hasAuthority('GOODS_GROUP_CREATE')")
    public ResponseEntity<GoodsGroupResponse> create(
            @Valid @RequestBody GoodsGroupDTO dto) {

        return ResponseEntity.ok(service.create(dto));
    }

    @Operation(summary = "Update Goods Group")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Goods Group updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = GoodsGroupResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Goods Group not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('GOODS_GROUP_EDIT')")
    public ResponseEntity<GoodsGroupResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody GoodsGroupDTO dto) {

        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Delete Goods Group")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Goods Group deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Goods Group not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('GOODS_GROUP_DELETE')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get Goods Group by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            schema = @Schema(implementation = GoodsGroupResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Goods Group not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GOODS_GROUP_VIEW')")
    public ResponseEntity<GoodsGroupResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Get all Goods Groups")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = GoodsGroupResponse.class)
                            )
                    )
            )
    })
    @GetMapping
    @PreAuthorize("hasAuthority('GOODS_GROUP_VIEW')")
    public ResponseEntity<List<GoodsGroupResponse>> getAll() {

        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get Goods Groups with pagination")
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
    @PreAuthorize("hasAuthority('GOODS_GROUP_VIEW')")
    public ResponseEntity<Page<GoodsGroupResponse>> getAll(Pageable pageable) {

        return ResponseEntity.ok(service.getAll(pageable));
    }
}