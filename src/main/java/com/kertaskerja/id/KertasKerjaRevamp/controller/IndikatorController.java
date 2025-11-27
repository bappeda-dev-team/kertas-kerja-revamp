package com.kertaskerja.id.KertasKerjaRevamp.controller;

import com.kertaskerja.id.KertasKerjaRevamp.dto.ApiResponse; // Import Wrapper
import com.kertaskerja.id.KertasKerjaRevamp.dto.IndikatorDto;
import com.kertaskerja.id.KertasKerjaRevamp.service.IndikatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/indikator")
@RequiredArgsConstructor // Ganti constructor manual dengan ini
public class IndikatorController {

    private final IndikatorService service;

    // --- 1. GET ALL (Bisa filter by pohonId) ---
    @GetMapping
    public ResponseEntity<ApiResponse<List<IndikatorDto.Response>>> findAll(
            @RequestParam(required = false) Long pohonId
    ) {
        List<IndikatorDto.Response> data;

        if (pohonId != null) {
            data = service.findByPohonId(pohonId);
        } else {
            data = service.findAll();
        }

        return ResponseEntity.ok(ApiResponse.success(data, "200"));
    }

    // --- 2. GET BY ID ---
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IndikatorDto.Response>> findById(@PathVariable Long id) {
        IndikatorDto.Response data = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(data, "200"));
    }

    // --- 3. CREATE ---
    @PostMapping
    public ResponseEntity<ApiResponse<IndikatorDto.Response>> create(@RequestBody @Valid IndikatorDto.Request request) {
        IndikatorDto.Response data = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(data));
    }

    // --- 4. UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<IndikatorDto.Response>> update(@PathVariable Long id, @RequestBody @Valid IndikatorDto.Request request) {
        IndikatorDto.Response data = service.update(id, request);
        return ResponseEntity.ok(ApiResponse.updated(data));
    }

    // --- 5. DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}