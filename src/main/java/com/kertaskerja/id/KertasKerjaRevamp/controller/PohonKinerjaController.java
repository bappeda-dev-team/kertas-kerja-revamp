package com.kertaskerja.id.KertasKerjaRevamp.controller;

import com.kertaskerja.id.KertasKerjaRevamp.dto.ApiResponse;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerjaDto;
import com.kertaskerja.id.KertasKerjaRevamp.service.PohonKinerjaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pohon-kinerja")
@RequiredArgsConstructor
@Tag(name = "1. Pohon Kinerja", description = "Management of Pohon Kinerja for both Pemda and OPD.")
public class PohonKinerjaController {

    private final PohonKinerjaService service;

    @GetMapping("/{id}")
    @Operation(summary = "Get Tree by ID")
    public ResponseEntity<ApiResponse<PohonKinerjaDto.TreeResponse>> getTreeById(@PathVariable Long id) {
        PohonKinerjaDto.TreeResponse treeData = service.getTreeById(id);
        return ResponseEntity.ok(ApiResponse.success(treeData, "Successfully loaded tree data by ID"));
    }

    @GetMapping("/tematik/{tahun}")
    @Operation(summary = "Get All Tematik (Root Level) by Tahun")
    public ResponseEntity<ApiResponse<PohonKinerjaDto.TematikWrapper>> getTematik(
            @PathVariable Integer tahun
    ) {
        PohonKinerjaDto.TematikWrapper data = service.findAllTematik(tahun);
        return ResponseEntity.ok(ApiResponse.success(data, "Successfully loaded all tematik data"));
    }

    @PostMapping
    @Operation(summary = "Create Pohon Kinerja")
    public ResponseEntity<ApiResponse<PohonKinerjaDto.DetailResponse>> create(@Valid @RequestBody PohonKinerjaDto.Request request) {
        PohonKinerjaDto.DetailResponse data = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Pohon Kinerja")
    public ResponseEntity<ApiResponse<PohonKinerjaDto.DetailResponse>> update(@PathVariable Long id, @Valid @RequestBody PohonKinerjaDto.Request request) {
        PohonKinerjaDto.DetailResponse data = service.update(id, request);
        return ResponseEntity.ok(ApiResponse.updated(data));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Pohon Kinerja")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    @GetMapping("/opd/{kodeOpd}/{tahun}")
    @Operation(summary = "Get OPD Tree Hierarchy")
    public ResponseEntity<ApiResponse<PohonKinerjaDto.OpdTreeResponse>> getOpdTree(
            @PathVariable String kodeOpd,
            @PathVariable Integer tahun
    ) {
        PohonKinerjaDto.OpdTreeResponse response = service.getOpdTreeByKodeOpdAndTahun(kodeOpd, tahun);
        return ResponseEntity.ok(ApiResponse.success(response, "Successfully loaded OPD tree"));
    }

    @GetMapping("/opd/{kodeOpd}/{tahun}/strategic")
    @Operation(summary = "Get OPD Strategic Roots (List View)")
    public ResponseEntity<ApiResponse<List<PohonKinerjaDto.TreeResponse>>> getStrategicList(
            @PathVariable String kodeOpd,
            @PathVariable Integer tahun
    ) {
        List<PohonKinerjaDto.TreeResponse> response = service.getStrategicListByKodeOpdAndTahun(kodeOpd, tahun);
        return ResponseEntity.ok(ApiResponse.success(response, "Successfully loaded Strategic OPD list"));
    }
}