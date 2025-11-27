package com.kertaskerja.id.KertasKerjaRevamp.controller;

import com.kertaskerja.id.KertasKerjaRevamp.dto.ApiResponse;
import com.kertaskerja.id.KertasKerjaRevamp.dto.TargetDto;
import com.kertaskerja.id.KertasKerjaRevamp.service.TargetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/target")
public class TargetController {

    private final TargetService service;

    public TargetController(TargetService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TargetDto.Response>>> findAll(
            @RequestParam(required = false) Long indikatorId
    ) {
        if (indikatorId != null) {
            return ResponseEntity.ok(ApiResponse.success(service.findByIndikatorId(indikatorId), "200"));
        }
        return ResponseEntity.ok(ApiResponse.success(service.findAll(), "200"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TargetDto.Response>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.findById(id), "200"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TargetDto.Response>> create(@RequestBody @Valid TargetDto.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(service.create(request), "200"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TargetDto.Response>> update(@PathVariable Long id, @RequestBody @Valid TargetDto.Request request) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, request), "200"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}