package com.kertaskerja.id.KertasKerjaRevamp.controller;

import com.kertaskerja.id.KertasKerjaRevamp.dto.ApiResponse;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerjaDto;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonTreeDto;
import com.kertaskerja.id.KertasKerjaRevamp.service.PohonKinerjaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pohon-kinerja")
@RequiredArgsConstructor
public class PohonKinerjaController {

    private final PohonKinerjaService service;

    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<PohonTreeDto>>> getFullTree() {
        List<PohonTreeDto> treeData = service.getFullTree();
        return ResponseEntity.ok(ApiResponse.success(treeData, "200"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PohonTreeDto>> getTreeById(@PathVariable Long id) {
        PohonTreeDto treeData = service.getTreeById(id);
        return ResponseEntity.ok(ApiResponse.success(treeData, "200"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PohonKinerjaDto.Response>> create(@RequestBody @Valid PohonKinerjaDto.Request request) {
        PohonKinerjaDto.Response data = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PohonKinerjaDto.Response>> update(@PathVariable Long id, @RequestBody @Valid PohonKinerjaDto.Request request) {
        PohonKinerjaDto.Response data = service.update(id, request);
        return ResponseEntity.ok(ApiResponse.updated(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}