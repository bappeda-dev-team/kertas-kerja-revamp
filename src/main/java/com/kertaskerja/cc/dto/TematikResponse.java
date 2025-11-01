package com.kertaskerja.cc.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TematikResponse {
    private String namaPohonKinerja;
    private String jenisPohonKinerja;
    private String levelPohonKinerja;
    private String keterangan;
    private String jenisChild;
    private List<IndikatorItem> indikator;
}
