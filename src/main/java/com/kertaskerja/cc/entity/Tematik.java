package com.kertaskerja.cc.entity;

import com.kertaskerja.cc.common.BaseAuditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Tematik extends BaseAuditable {

    private Long id;
    private String idTematik;
    private String namaPohonKinerja;
    private String jenisPohonKinerja;
    private String levelPohonKinerja;
    private String keterangan;
    private String jenisChild;
}
