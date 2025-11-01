package com.kertaskerja.cc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TematikRequest {
    @NotBlank
    private String id_tematik;
    @NotBlank
    private String nama_pohon_kinerja;
    @NotBlank
    private String jenis_pohon_kinerja;
    @NotBlank
    private String level_pohon_kinerja;
    private String keterangan;
    @NotBlank
    private String jenis_child;
}
