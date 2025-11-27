package com.kertaskerja.id.KertasKerjaRevamp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Indikator {

    @Id
    private Long id;

    private Long pohonKinerjaId;
    private String indikator;
    private String keterangan;
    private String tahun;
}