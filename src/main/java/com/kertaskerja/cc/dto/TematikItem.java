package com.kertaskerja.cc.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TematikItem {
    private String namaPohonKinerja;
    private String jenisPohonKinerja;
    private String levelPohonKinerja;
    private String keterangan;
    private String jenisChild;
    private List<IndikatorItem> indikator = new ArrayList<>();
}
