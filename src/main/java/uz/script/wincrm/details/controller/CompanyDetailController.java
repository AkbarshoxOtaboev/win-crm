package uz.script.wincrm.details.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import uz.script.wincrm.details.dto.CompanyDetailDTO;
import uz.script.wincrm.details.response.CompanyDetailResponse;
import uz.script.wincrm.details.service.CompanyDetailService;
import uz.script.wincrm.utils.RestApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/company-details")
@RequiredArgsConstructor
@Tag(name = "Company Detail REST API", description = "Company detail (rekvizit) CRUD operations")
public class CompanyDetailController {

    private final CompanyDetailService service;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('COMPANY_DETAIL_CREATE')")
    @Operation(
            summary = "Create company detail",
            description = "Only users with COMPANY_DETAIL_CREATE permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "201",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CompanyDetailResponse.class)
            )
    )
    public ResponseEntity<?> create(@Valid @RequestBody CompanyDetailDTO dto) {
        CompanyDetailResponse response = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        RestApiResponse.<CompanyDetailResponse>builder()
                                .message("Company detail successfully created")
                                .data(response)
                                .build()
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('COMPANY_DETAIL_VIEW')")
    @Operation(
            summary = "Fetch all company details",
            description = "Only users with COMPANY_DETAIL_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = CompanyDetailResponse.class))
            )
    )
    public ResponseEntity<?> fetchAll() {
        return ResponseEntity.ok(
                RestApiResponse.<List<CompanyDetailResponse>>builder()
                        .message("All company details fetched successfully")
                        .data(service.fetchAll())
                        .build()
        );
    }

    @GetMapping("/current")
    @PreAuthorize("hasAuthority('COMPANY_DETAIL_VIEW')")
    @Operation(
            summary = "Fetch the company detail",
            description = "Returns the system's single company detail (rekvizit) record. " +
                    "Only users with COMPANY_DETAIL_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CompanyDetailResponse.class)
            )
    )
    public ResponseEntity<?> getCompanyDetail() {
        return ResponseEntity.ok(
                RestApiResponse.<CompanyDetailResponse>builder()
                        .message("Company detail fetched successfully")
                        .data(service.getCompanyDetail())
                        .build()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('COMPANY_DETAIL_VIEW')")
    @Operation(
            summary = "Fetch company detail by id",
            description = "Only users with COMPANY_DETAIL_VIEW permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CompanyDetailResponse.class)
            )
    )
    public ResponseEntity<?> findById(
            @Parameter(description = "Company detail ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                RestApiResponse.<CompanyDetailResponse>builder()
                        .message("Company detail found successfully")
                        .data(service.findById(id))
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('COMPANY_DETAIL_EDIT')")
    @Operation(
            summary = "Update company detail",
            description = "Only users with COMPANY_DETAIL_EDIT permission can use this endpoint."
    )
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CompanyDetailResponse.class)
            )
    )
    public ResponseEntity<?> update(
            @Parameter(description = "Company detail ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody CompanyDetailDTO dto
    ) {
        CompanyDetailResponse response = service.update(id, dto);

        return ResponseEntity.ok(
                RestApiResponse.<CompanyDetailResponse>builder()
                        .message("Company detail successfully updated")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('COMPANY_DETAIL_DELETE')")
    @Operation(
            summary = "Delete company detail",
            description = "Only users with COMPANY_DETAIL_DELETE permission can use this endpoint."
    )
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> delete(
            @Parameter(description = "Company detail ID", example = "1")
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.ok(
                RestApiResponse.<Void>builder()
                        .message("Company detail successfully deleted")
                        .build()
        );
    }
}