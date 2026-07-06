package uz.script.wincrm.goods.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.script.wincrm.goods.dto.GoodsDTO;
import uz.script.wincrm.goods.response.GoodsResponse;
import uz.script.wincrm.goods.service.GoodsService;
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
@RequiredArgsConstructor
@Tag(name = "Goods REST API Management Controller")
public class GoodsController {

    private final GoodsService service;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('GOODS_CREATE')")
    @Operation(
            summary = "Create goods",
            description = "Only users with GOODS_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GoodsResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @ModelAttribute GoodsDTO dto) {
        GoodsResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<GoodsResponse>builder()
                                .message("Goods successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GOODS_VIEW')")
    @Operation(
            summary = "Fetch all goods",
            description = "Only users with GOODS_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = GoodsResponse.class)
                    )
            )
    )
    public ResponseEntity<?> fetchAllGoods() {
        return ResponseEntity.ok(
                RestApiResponse.<List<GoodsResponse>>builder()
                        .message("All goods fetched successfully")
                        .data(service.fetchAllGoods())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GOODS_VIEW')")
    @Operation(
            summary = "Fetch goods by id",
            description = "Only users with GOODS_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GoodsResponse.class)
            )
    )
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                RestApiResponse.<GoodsResponse>builder()
                        .message("Goods found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('GOODS_EDIT')")
    @Operation(
            summary = "Update goods",
            description = "Only users with GOODS_EDIT permission can use this endpoint. Photo is optional — existing photo is kept if not provided."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GoodsResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @ModelAttribute GoodsDTO dto
    ) {
        GoodsResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<GoodsResponse>builder()
                        .message("Goods successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('GOODS_DELETE')")
    @Operation(
            summary = "Delete goods",
            description = "Only users with GOODS_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Goods successfully deleted")
                        .build()
        );
    }
}