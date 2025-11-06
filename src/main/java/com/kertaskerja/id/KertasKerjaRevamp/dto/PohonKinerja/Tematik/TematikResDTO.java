package com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Tematik;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class TematikResDTO {
    private String idTematik;
    private String namaPohonKinerja;
    private String jenisPohonKinerja;
    private Integer levelPohonKinerja;
    private String keterangan;
    private String jenisChild;
}

