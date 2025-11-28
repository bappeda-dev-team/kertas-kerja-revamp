package com.kertaskerja.id.KertasKerjaRevamp.controller;

import com.kertaskerja.id.KertasKerjaRevamp.dto.ApiResponse;
import com.kertaskerja.id.KertasKerjaRevamp.dto.TargetDto;
import com.kertaskerja.id.KertasKerjaRevamp.service.TargetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/target")
@RequiredArgsConstructor
@Tag(name = "3. Target Kinerja", description = "Target Management that Sticks into Indikator")
public class TargetController {

    private final TargetService service;

    @GetMapping
    @Operation(summary = "Get All Targets", description = "Retrieves a list of targets. Optional filtering by Indicator ID")
    public ResponseEntity<ApiResponse<List<TargetDto.Response>>> findAll(
            @RequestParam(required = false) Long indikatorId
    ) {
        List<TargetDto.Response> data;

        if (indikatorId != null) {
            data = service.findByIndikatorId(indikatorId);
        } else {
            data = service.findAll();
        }

        return ResponseEntity.ok(ApiResponse.success(data, "Successfully loaded list of target data"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Target by ID", description = "Retrieves details of a specific target")
    public ResponseEntity<ApiResponse<TargetDto.Response>> findById(@PathVariable Long id) {
        TargetDto.Response data = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(data, "Successfully loaded target data by Id"));
    }

    @PostMapping
    @Operation(summary = "Create New Target", description = "Creates a new target linked to a specific indicator")
    public ResponseEntity<ApiResponse<TargetDto.Response>> create(@RequestBody @Valid TargetDto.Request request) {
        TargetDto.Response data = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Target", description = "Updates an existing target data")
    public ResponseEntity<ApiResponse<TargetDto.Response>> update(@PathVariable Long id, @RequestBody @Valid TargetDto.Request request) {
        TargetDto.Response data = service.update(id, request);
        return ResponseEntity.ok(ApiResponse.updated(data));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Target", description = "Deletes a target permanently")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}