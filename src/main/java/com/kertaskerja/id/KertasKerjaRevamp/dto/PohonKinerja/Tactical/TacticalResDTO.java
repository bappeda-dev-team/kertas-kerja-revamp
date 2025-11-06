package com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Tactical;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Indikator.IndikatorResDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Operational.OperationalResDTO;
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
public class TacticalResDTO {
    private String idTactical;
    private String namaPohonKinerja;
    private String jenisPohonKinerja;
    private Integer levelPohonKinerja;
    private String keterangan;
    private String jenisChild;
    private List<IndikatorResDTO> indikator;
    private List<OperationalResDTO> childs;
}
