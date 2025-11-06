package com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.SubTematik;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubTematikReqDTO {
    @NotBlank
    private String id_tematik;

    @NotBlank(message = "Nama Pokin Sub Tematik tidak boleh kosong")
    private String nama_pohon_kinerja;

    private String keterangan;
}
