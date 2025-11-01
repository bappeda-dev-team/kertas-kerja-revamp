package com.kertaskerja.cc.controller;

import com.kertaskerja.cc.dto.ApiResponse;
import com.kertaskerja.cc.dto.IndikatorRequest;
import com.kertaskerja.cc.service.TematikService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/indikator")
@RequiredArgsConstructor
public class IndikatorController {

    private final TematikService tematikService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> createIndikator(@Valid @RequestBody IndikatorRequest request) {
        // Validate tematik exists
        if (!tematikService.existsByIdTematik(request.getId_tematik())) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error(404, "id_tematik not found"));
        }

        Map<String, Object> body;
        try {
            body = tematikService.createIndikator(request);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(400, e.getMessage()));
        }
        return ResponseEntity.status(201).body(ApiResponse.created(body));
    }
}
