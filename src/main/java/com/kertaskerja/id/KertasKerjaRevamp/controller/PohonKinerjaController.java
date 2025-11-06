package com.kertaskerja.id.KertasKerjaRevamp.controller;

import com.kertaskerja.id.KertasKerjaRevamp.dto.ApiResponse;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Indikator.IndikatorReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Operational.OperationalReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.PohonKinerjaReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.PohonKinerjaResDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Strategic.StrategicReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.SubSubTematik.SubSubTematikReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.SubTematik.SubTematikReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Tactical.TacticalReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Tematik.TematikReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.service.PohonKinerja.PohonKinerjaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Order(1)
@RestController
@RequestMapping("/pohon-kinerja")
@RequiredArgsConstructor
@Tag(name = "Endpoint Pohon Kinerja Pemda")
public class PohonKinerjaController {

    private final PohonKinerjaService service;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private <T> ResponseEntity<ApiResponse<?>> validate(BindingResult result) {
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(err ->
                  System.err.println("Validation error: " + err.getField() + " -> " + err.getDefaultMessage())
            );

            List<String> errors = result.getFieldErrors().stream()
                  .map(err -> err.getField() + ": " + err.getDefaultMessage())
                  .toList();

            ApiResponse<List<String>> response = ApiResponse.<List<String>>builder()
                  .status(400)
                  .success(false)
                  .message("Validation failed")
                  .errors(errors)
                  .timestamp(LocalDateTime.now().format(formatter))
                  .build();

            return ResponseEntity.badRequest().body(response);
        }

        return null;
    }


    @GetMapping("/{idTematik}")
    @Operation(summary = "Ambil data pohon kinerja berdasarkan id_tematik")
    public ResponseEntity<ApiResponse<PohonKinerjaResDTO>> getPohonKinerjaByIdTematik(@PathVariable String idTematik) {
        PohonKinerjaResDTO result = service.getPohonKinerjaByIdTematik(idTematik);

        ApiResponse<PohonKinerjaResDTO> response = ApiResponse.success(
              result,
              "Data Pohon Kinerja retrieved successfully"
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/tematik")
    @Operation(summary = "[1] - Simpan data Tematik")
    public ResponseEntity<ApiResponse<?>> saveTematik(@Valid @RequestBody TematikReqDTO dto, BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.savePohonKinerja("tematik", dto);
        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PostMapping("/sub-tematik")
    @Operation(summary = "[2] - Simpan data Sub Tematik")
    public ResponseEntity<ApiResponse<?>> saveSubTematik(@Valid @RequestBody SubTematikReqDTO dto, BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.savePohonKinerja("sub-tematik", dto);
        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PostMapping("/sub-sub-tematik")
    @Operation(summary = "[3] - Simpan data Sub Sub Tematik")
    public ResponseEntity<ApiResponse<?>> saveSubSubTematik(@Valid @RequestBody SubSubTematikReqDTO dto, BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.savePohonKinerja("sub-sub-tematik", dto);
        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PostMapping("/strategic")
    @Operation(summary = "[4] - Simpan data Strategic")
    public ResponseEntity<ApiResponse<?>> saveStrategic(@Valid @RequestBody StrategicReqDTO dto, BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.savePohonKinerja("strategic", dto);
        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PostMapping("/tactical")
    @Operation(summary = "[5] - Simpan data Tactical")
    public ResponseEntity<ApiResponse<?>> saveTactical(@Valid @RequestBody TacticalReqDTO dto, BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.savePohonKinerja("tactical", dto);
        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PostMapping("/operational")
    @Operation(summary = "[6] - Simpan data Operational")
    public ResponseEntity<ApiResponse<?>> saveOperational(@Valid @RequestBody OperationalReqDTO dto, BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.savePohonKinerja("operational", dto);
        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PostMapping("/indikator/tematik")
    @Operation(summary = "[7] - Simpan indikator Tematik")
    public ResponseEntity<ApiResponse<?>> saveIndikatorTematik(@Valid @RequestBody IndikatorReqDTO.IndikatorTematik dto,
                                                               BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.saveIndikator("tematik", dto);
        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PostMapping("/indikator/sub-tematik")
    @Operation(summary = "[8] - Simpan indikator Tematik")
    public ResponseEntity<ApiResponse<?>> saveIndikatorSubTematik(@Valid @RequestBody IndikatorReqDTO.IndikatorSubTematik dto,
                                                                  BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.saveIndikator("sub-tematik", dto);
        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PostMapping("/indikator/sub-sub-tematik")
    @Operation(summary = "[9] - Simpan indikator Tematik")
    public ResponseEntity<ApiResponse<?>> saveIndikatorSubSubTematik(@Valid @RequestBody IndikatorReqDTO.IndikatorSubSubTematik dto,
                                                                     BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.saveIndikator("sub-sub-tematik", dto);
        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PostMapping("/indikator/strategic")
    @Operation(summary = "[10] - Simpan indikator Tematik")
    public ResponseEntity<ApiResponse<?>> saveIndikatorStrategic(@Valid @RequestBody IndikatorReqDTO.IndikatorStrategic dto,
                                                                 BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.saveIndikator("strategic", dto);
        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PostMapping("/indikator/tactical")
    @Operation(summary = "[11] - Simpan indikator Tematik")
    public ResponseEntity<ApiResponse<?>> saveIndikatorTactical(@Valid @RequestBody IndikatorReqDTO.IndikatorTactical dto,
                                                                BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.saveIndikator("tactical", dto);
        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PostMapping("/indikator/operational")
    @Operation(summary = "[12] - Simpan indikator Operational")
    public ResponseEntity<ApiResponse<?>> saveIndikatorOperational(@Valid @RequestBody IndikatorReqDTO.IndikatorOperational dto,
                                                                   BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.saveIndikator("operational", dto);
        return ResponseEntity.ok(ApiResponse.created(response));
    }

    @PutMapping("/update/{idPokin}")
    @Operation(summary = "Edit data Pohon Kinerja")
    public ResponseEntity<ApiResponse<?>> updatePohonKinerja(@PathVariable String idPokin,
                                                             @Valid @RequestBody PohonKinerjaReqDTO dto,
                                                             BindingResult result) {
        var error = validate(result);
        if (error != null) return error;

        var response = service.updatePohonKinerja(idPokin, dto);
        return ResponseEntity.ok(ApiResponse.updated(response));
    }

    @DeleteMapping("/delete/{idPokin}")
    @Operation(summary = "Hapus data Pohon Kinerja")
    public ResponseEntity<ApiResponse<?>> deletePohonKinerja(@PathVariable String idPokin) {
        service.deletePohonKinerja(idPokin);

        return ResponseEntity.ok(ApiResponse.success(null, "Pohon Kinerja with ID " + idPokin + " is deleted successfully"));
    }
}
