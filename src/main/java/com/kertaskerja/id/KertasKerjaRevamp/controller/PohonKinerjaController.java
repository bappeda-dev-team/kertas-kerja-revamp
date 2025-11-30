package com.kertaskerja.id.KertasKerjaRevamp.controller;

import com.kertaskerja.id.KertasKerjaRevamp.dto.ApiResponse;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerjaDto;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonTreeDto;
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
@Tag(name = "1. Pohon Kinerja", description = "Management Structure of Pohon Kinerja (Tematik s.d. Operational)")
public class PohonKinerjaController {

    private final PohonKinerjaService service;

    @GetMapping("/tree")
    @Operation(summary = "Get Full Tree Structure", description = "Retrieves the complete hierarchy of performance trees from root level down to operational level")
    public ResponseEntity<ApiResponse<List<PohonTreeDto>>> getFullTree() {
        List<PohonTreeDto> treeData = service.getFullTree();
        return ResponseEntity.ok(ApiResponse.success(treeData, "Succesfully loaded all tree data"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Specific Tree Structure", description = "Retrieves the hierarchy starting from a specific node ID")
    public ResponseEntity<ApiResponse<PohonTreeDto>> getTreeById(@PathVariable Long id) {
        PohonTreeDto treeData = service.getTreeById(id);
        return ResponseEntity.ok(ApiResponse.success(treeData, "Succesfully loaded tree data by Id"));
    }

    @PostMapping
    @Operation(summary = "Create New Node", description = "Creates a new performance tree node. Can include nested indicators and targets")
    public ResponseEntity<ApiResponse<PohonKinerjaDto.Response>> create(@RequestBody @Valid PohonKinerjaDto.Request request) {
        PohonKinerjaDto.Response data = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Node", description = "Updates an existing performance tree node")
    public ResponseEntity<ApiResponse<PohonKinerjaDto.Response>> update(@PathVariable Long id, @RequestBody @Valid PohonKinerjaDto.Request request) {
        PohonKinerjaDto.Response data = service.update(id, request);
        return ResponseEntity.ok(ApiResponse.updated(data));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Node", description = "Deletes a performance tree node and all its descendants")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}