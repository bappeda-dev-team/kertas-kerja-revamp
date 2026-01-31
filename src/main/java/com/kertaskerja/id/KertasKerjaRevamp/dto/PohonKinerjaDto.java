package com.kertaskerja.id.KertasKerjaRevamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kertaskerja.id.KertasKerjaRevamp.enums.JenisPohon;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PohonKinerjaDto {

    public record Request(
            Long parentId,
            @NotBlank String namaPohon,
            String keterangan,
            @NotNull Integer tahun,
            @NotNull JenisPohon jenisPohon,
            @NotNull Integer levelPohon,
            String kodeOpd,
            String kodePemda,
            String status,
            List<IndikatorRequest> indikators
    ) {}

    public record TematikWrapper(
            Integer tahun,
            List<TematikItem> tematiks
    ) {}

    public record TematikItem(
            Long id,
            Long parentId,
            @JsonProperty("tema")
            String namaPohon,
            String jenisPohon,
            Integer levelPohon,
            String keterangan,
            List<IndikatorResponse> indikator
    ) {}

    public record IndikatorRequest(
            Long id,
            String indikator,
            String keterangan,
            Integer tahun,
            List<TargetRequest> targets
    ) {}

    public record TargetRequest(
            Long id,
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

    @Getter
    @Setter
    @Builder
    public static class TreeResponse {
        private Long id;
        private Long parentId;
        private String namaPohon;
        private String keterangan;
        private Integer tahun;
        private String jenisPohon;
        private Integer levelPohon;
        private String status;
        private List<IndikatorResponse> indikator;
        private List<TreeResponse> children;
    }

    @Builder
    public record OpdTreeResponse(
            String kodeOpd,
            String namaOpd,
            Integer tahun,
            List<TreeResponse> roots
    ) {}
}