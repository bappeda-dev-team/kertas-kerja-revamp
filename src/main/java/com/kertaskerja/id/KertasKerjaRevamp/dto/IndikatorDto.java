package com.kertaskerja.id.KertasKerjaRevamp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class IndikatorDto {

    public record Request(
            Long pohonKinerjaId,

            @NotBlank(message = "Nama indikator tidak boleh kosong")
            String indikator,

            String keterangan,

            @NotNull(message = "Tahun tidak boleh kosong")
            @Pattern(regexp = "\\d{4}", message = "Format tahun harus 4 digit angka (YYYY)")
            String tahun
    ) {}

    public record Response(
            Long id,
            Long pohonKinerjaId,
            String indikator,
            String keterangan,
            String tahun,
            List<TargetDto.Response>targets
    ) {}
}