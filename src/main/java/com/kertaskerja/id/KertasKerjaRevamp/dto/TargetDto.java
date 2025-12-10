package com.kertaskerja.id.KertasKerjaRevamp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class TargetDto {

    public record Request(
            @NotNull(message = "ID Indikator tidak boleh kosong")
            Long indikatorId,

            @NotNull(message = "Nilai target tidak boleh kosong")
            Double nilai,

            @NotBlank(message = "Satuan tidak boleh kosong")
            String satuan,

            @NotNull(message = "Tahun tidak boleh kosong")
            @Pattern(regexp = "\\d{4}", message = "Format tahun harus 4 digit angka (YYYY)")
            Integer tahun
    ) {}

    public record Response(
            Long id,
            Long indikatorId,
            Double nilai,
            String satuan,
            Integer tahun
    ) {}
}