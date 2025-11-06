package com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.SubTematik;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Indikator.IndikatorResDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.SubSubTematik.SubSubTematikResDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class SubTematikResDTO {
    private String idSubTematik;
    private String namaPohonKinerja;
    private String jenisPohonKinerja;
    private Integer levelPohonKinerja;
    private String keterangan;
    private String jenisChild;
    private List<IndikatorResDTO> indikator;
    private List<SubSubTematikResDTO> childs;
}

