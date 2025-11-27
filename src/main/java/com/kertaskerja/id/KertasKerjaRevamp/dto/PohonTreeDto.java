package com.kertaskerja.id.KertasKerjaRevamp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PohonTreeDto {
    private Long id;
    private Long parentId;
    private String namaPohon;
    private String keterangan;
    private String tahun;
    private String jenisPohon;
    private Integer levelPohon;
    private String kodeOpd;
    private String kodePemda;
    private String status;

    @Builder.Default
    private List<IndikatorDto.Response> indikator = new ArrayList<>();

    @Builder.Default
    private List<PohonTreeDto> children = new ArrayList<>();
}