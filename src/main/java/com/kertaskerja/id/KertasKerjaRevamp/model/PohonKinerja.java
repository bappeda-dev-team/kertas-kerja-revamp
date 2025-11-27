package com.kertaskerja.id.KertasKerjaRevamp.model;

import com.kertaskerja.id.KertasKerjaRevamp.enums.JenisPohon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PohonKinerja {

    @Id
    private Long id;

    private Long parentId;
    private String namaPohon;
    private String keterangan;
    private String tahun;
    private JenisPohon jenisPohon;
    private Integer levelPohon;
    private String kodeOpd;
    private String kodePemda;
    private String status;
}