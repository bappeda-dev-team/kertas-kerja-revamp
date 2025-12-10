package com.kertaskerja.id.KertasKerjaRevamp.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PohonTreeDto {
    private Long id;
    private Long parentId;
    private String namaPohon;
    private String keterangan;
    private Integer tahun;
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