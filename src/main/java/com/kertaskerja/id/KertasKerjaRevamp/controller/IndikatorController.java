package com.kertaskerja.id.KertasKerjaRevamp.controller;

import com.kertaskerja.id.KertasKerjaRevamp.dto.ApiResponse;
import com.kertaskerja.id.KertasKerjaRevamp.dto.IndikatorDto;
import com.kertaskerja.id.KertasKerjaRevamp.service.IndikatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/indikator")
@RequiredArgsConstructor
@Tag(name = "2. Indikator Kinerja", description = "Indikator Management that Sticks into Pohon Kinerja")
public class IndikatorController {

    private final IndikatorService service;

    @GetMapping
    @Operation(summary = "Get All Indicators", description = "Retrieves a list of indicators. Optional filtering by Pohon Kinerja ID")
    public ResponseEntity<ApiResponse<List<IndikatorDto.Response>>> findAll(
            @RequestParam(required = false) Long pohonId
    ) {
        List<IndikatorDto.Response> data;

        if (pohonId != null) {
            data = service.findByPohonId(pohonId);
        } else {
            data = service.findAll();
        }

        return ResponseEntity.ok(ApiResponse.success(data, "Successfully loaded list of indikator data"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Indicator by ID", description = "Retrieves details of a specific indicator")
    public ResponseEntity<ApiResponse<IndikatorDto.Response>> findById(@PathVariable Long id) {
        IndikatorDto.Response data = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(data, "Successfully loaded indikator data by Id"));
    }

    @PostMapping
    @Operation(summary = "Create New Indicator", description = "Creates a new indicator linked to a specific performance tree node")
    public ResponseEntity<ApiResponse<IndikatorDto.Response>> create(@RequestBody @Valid IndikatorDto.Request request) {
        IndikatorDto.Response data = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Indicator", description = "Updates an existing indicator data")
    public ResponseEntity<ApiResponse<IndikatorDto.Response>> update(@PathVariable Long id, @RequestBody @Valid IndikatorDto.Request request) {
        IndikatorDto.Response data = service.update(id, request);
        return ResponseEntity.ok(ApiResponse.updated(data));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Indicator", description = "Deletes an indicator permanently")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}