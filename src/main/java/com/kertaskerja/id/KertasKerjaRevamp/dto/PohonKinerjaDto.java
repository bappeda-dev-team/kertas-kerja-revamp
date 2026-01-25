package com.kertaskerja.id.KertasKerjaRevamp.dto;

import com.kertaskerja.id.KertasKerjaRevamp.enums.JenisPohon;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class PohonKinerjaDto {

    // ==========================================
    // 1. REQUEST (Untuk Create/Update)
    // ==========================================
    public record Request(
            Long parentId,

            @NotBlank(message = "Nama pohon tidak boleh kosong")
            String namaPohon,

            String keterangan,

            @NotNull(message = "Tahun tidak boleh kosong")
            Integer tahun,

            @NotNull(message = "Jenis pohon tidak boleh kosong")
            JenisPohon jenisPohon,

            @NotNull(message = "Level pohon tidak boleh kosong")
            Integer levelPohon,

            String kodeOpd,
            String kodePemda,
            String status,

            List<IndikatorRequest> indikators
    ) {}

    // ==========================================
    // 2. RESPONSE SIMPLE (Tanpa Anak, Tanpa Indikator)
    // ==========================================
    public record SimpleResponse(
            Long id,
            Long parentId,
            String namaPohon,
            Integer tahun,
            String jenisPohon,
            Integer levelPohon,
            String kodeOpd,
            String kodePemda,
            String status
    ) {}

    // ==========================================
    // 3. RESPONSE DETAIL (Flat + Indikator)
    // ==========================================
    public record DetailResponse(
            Long id,
            Long parentId,
            String namaPohon,
            String keterangan,
            Integer tahun,
            JenisPohon jenisPohon,
            Integer levelPohon,
            String kodeOpd,
            String kodePemda,
            String status,
            List<IndikatorResponse> indikators
    ) {}

    // ==========================================
    // 4. RESPONSE TREE (Hierarki / Bersarang)
    // ==========================================
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeResponse {
        private Long id;
        private Long parentId;
        private String namaPohon;
        private String keterangan;
        private Integer tahun;
        private String jenisPohon;
        private Integer levelPohon;
        private String status;

        @Builder.Default
        private List<IndikatorResponse> indikator = new ArrayList<>();

        @Builder.Default
        private List<TreeResponse> children = new ArrayList<>();
    }

    // ==========================================
    // SHARED DTOs (Komponen Pendukung)
    // ==========================================

    public record IndikatorRequest(
            Long id,  // ADD THIS - null for new, populated for existing
            String indikator,
            String keterangan,
            Integer tahun,
            List<TargetRequest> targets
    ) {}

    public record TargetRequest(
            Long id,  // ADD THIS - null for new, populated for existing
            Double nilai,
            String satuan,
            Integer tahun
    ) {}

    public record IndikatorResponse(
            Long id,
            String indikator,
            String keterangan,
            Integer tahun,
            List<TargetResponse> targets
    ) {}

    public record TargetResponse(
            Long id,
            Double nilai,
            String satuan,
            Integer tahun
    ) {}

    public record OpdTreeResponse(
            String kodeOpd,
            String namaOpd,        // Akan di-populate oleh frontend
            Integer tahun,
            List<TreeResponse> roots   // Bisa multiple tematik roots
    ) {
        @lombok.Builder
        public OpdTreeResponse {}
    }
}