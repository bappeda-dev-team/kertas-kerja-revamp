package com.kertaskerja.cc.controller;

import com.kertaskerja.cc.dto.ApiResponse;
import com.kertaskerja.cc.dto.TematikRequest;
import com.kertaskerja.cc.dto.TematikItem;
import com.kertaskerja.cc.service.TematikService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tematik")
@RequiredArgsConstructor
public class TematikController {

    private final TematikService tematikService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> createTematik(@Valid @RequestBody TematikRequest request) {
        if (tematikService.existsByIdTematik(request.getId_tematik())) {
            return ResponseEntity.status(409)
                    .body(ApiResponse.error(409, "id_tematik already exists"));
        }
        if (tematikService.existsByNamaPohonKinerja(request.getNama_pohon_kinerja())) {
            return ResponseEntity.status(409)
                    .body(ApiResponse.error(409, "nama_pohon_kinerja already exists"));
        }

        Map<String, Object> body = tematikService.createTematik(request);
        return ResponseEntity.status(201).body(ApiResponse.created(body));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<java.util.List<TematikItem>>> getAllTematikWithIndicators() {
        java.util.List<TematikItem> data = tematikService.getAllTematikWithIndicators();
        return ResponseEntity.ok(ApiResponse.success(data, "Data fetched successfully"));
    }
}
