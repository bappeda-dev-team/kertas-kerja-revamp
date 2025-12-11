package com.kertaskerja.id.KertasKerjaRevamp.dto;

import com.kertaskerja.id.KertasKerjaRevamp.enums.JenisPohon;
import jakarta.validation.constraints.*;

import java.util.List;

public class PohonKinerjaDto {

    public record Request(
            Long parentId,

            @NotBlank(message = "Nama pohon tidak boleh kosong")
            String namaPohon,

            String keterangan,

            @NotNull(message = "Tahun tidak boleh kosong")
            Integer tahun,

            @NotNull(message = "Jenis pohon tidak boleh kosong (CONTOH: TEMATIK, STRATEGIC)")
            JenisPohon jenisPohon,

            @NotNull(message = "Level pohon tidak boleh kosong")
            Integer levelPohon,

            String kodeOpd,
            String kodePemda,
            String status
    ) {
    }

    public record Response(
            Long id,
            Long parentId,
            String namaPohon,
            String keterangan,
            Integer tahun,
            JenisPohon jenisPohon,
            Integer levelPohon,
            String kodeOpd,
            String kodePemda,
            String status
    ) {
    }

    // === DTO KHUSUS CREATE COMPOSITE ===
    public record CreateCompositeRequest(
            Long parentId,
            @NotBlank String namaPohon,
            String keterangan,
            Integer tahun,
            JenisPohon jenisPohon,
            Integer levelPohon,
            String kodeOpd,
            String kodePemda,
            String status,

            List<IndikatorRequestItem> indikators
    ) {
    }

    public record IndikatorRequestItem(
            String indikator,
            String keterangan,
            Integer tahun,

            List<TargetRequestItem> targets
    ) {
    }

    public record TargetRequestItem(
            Double nilai,
            String satuan,
            Integer tahun
    ) {
    }
}