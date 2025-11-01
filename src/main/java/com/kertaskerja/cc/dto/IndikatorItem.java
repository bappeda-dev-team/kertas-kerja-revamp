package com.kertaskerja.cc.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IndikatorItem {
    private String indikator;
    private Integer tahun;
    private List<TargetItem> target;
}
