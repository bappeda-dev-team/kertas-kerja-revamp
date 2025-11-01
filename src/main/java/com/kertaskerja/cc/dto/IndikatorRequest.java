package com.kertaskerja.cc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class IndikatorRequest {
    @NotBlank
    private String id_tematik;
    private String id_sub_tematik; // optional
    @NotBlank
    private String indikator;
    @NotNull
    private Integer tahun;
    @NotNull
    private List<TargetItem> target;
}
