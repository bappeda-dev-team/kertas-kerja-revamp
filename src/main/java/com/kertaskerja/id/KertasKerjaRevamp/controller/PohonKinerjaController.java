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

    @GetMapping("/{id}")
    @Operation(summary = "Get Specific Tree Structure", description = "Retrieves the hierarchy starting from a specific tree ID")
    public ResponseEntity<ApiResponse<PohonTreeDto>> getTreeById(@PathVariable Long id) {
        PohonTreeDto treeData = service.getTreeById(id);
        return ResponseEntity.ok(ApiResponse.success(treeData, "Succesfully loaded tree data by Id"));
    }

    @GetMapping("/tematik")
    @Operation(summary = "Get All Tematik", description = "Retrieves all root level (Tematik) performance trees")
    public ResponseEntity<ApiResponse<List<PohonKinerjaDto.Response>>> getTematik() {
        List<PohonKinerjaDto.Response> data = service.findAllTematik();
        return ResponseEntity.ok(ApiResponse.success(data, "Successfully loaded all tematik data"));
    }

    @PostMapping
    @Operation(summary = "Create New Tree", description = "Creates a new tree. Including nested indicators and targets")
    public ResponseEntity<ApiResponse<PohonKinerjaDto.Response>> create(@RequestBody @Valid PohonKinerjaDto.CreateCompositeRequest request) {
        PohonKinerjaDto.Response data = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(data));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Node", description = "Updates an existing tree ")
    public ResponseEntity<ApiResponse<PohonKinerjaDto.Response>> update(@PathVariable Long id, @RequestBody @Valid PohonKinerjaDto.Request request) {
        PohonKinerjaDto.Response data = service.update(id, request);
        return ResponseEntity.ok(ApiResponse.updated(data));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Node", description = "Deletes a tree and all its descendants")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}