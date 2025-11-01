package com.kertaskerja.cc.entity;

import com.kertaskerja.cc.common.BaseAuditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Indikator extends BaseAuditable {

    private Long id;
    private String idTematik;
    private String idSubTematik;
    private String indikator;
    private Integer tahun;
    private String target; // JSON array string
}
