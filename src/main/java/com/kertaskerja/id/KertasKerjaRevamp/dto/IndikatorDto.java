package com.kertaskerja.id.KertasKerjaRevamp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class IndikatorDto {

    public record Request(
            Long pohonKinerjaId,

            @NotBlank(message = "Nama indikator tidak boleh kosong")
            String indikator,

            String keterangan,

            @NotNull(message = "Tahun tidak boleh kosong")
            Integer tahun
    ) {}

    public record Response(
            Long id,
            Long pohonKinerjaId,
            String indikator,
            String keterangan,
            Integer tahun,
            List<TargetDto.Response>targets
    ) {}
}