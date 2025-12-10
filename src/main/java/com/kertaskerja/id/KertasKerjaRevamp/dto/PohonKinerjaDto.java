package com.kertaskerja.id.KertasKerjaRevamp.dto;

import com.kertaskerja.id.KertasKerjaRevamp.enums.JenisPohon;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class PohonKinerjaDto {

    public record Request(
            Long parentId,

            @NotBlank(message = "Nama pohon tidak boleh kosong")
            String namaPohon,

            String keterangan,

            @NotNull(message = "Tahun tidak boleh kosong")
            @Pattern(regexp = "\\d{4}", message = "Format tahun harus 4 digit angka (YYYY)")
            Integer tahun,

            @NotNull(message = "Jenis pohon tidak boleh kosong (CONTOH: TEMATIK, STRATEGIC)")
            JenisPohon jenisPohon,

            @NotNull(message = "Level pohon tidak boleh kosong")
            Integer levelPohon,

            String kodeOpd,
            String kodePemda,
            String status
    ) {}

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
    ) {}
}