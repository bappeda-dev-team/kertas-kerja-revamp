package com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.SubSubTematik;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubSubTematikReqDTO {
    @NotBlank
    private String id_sub_tematik;

    @NotBlank(message = "Nama Pokin Sub Sub Tematik tidak boleh kosong")
    private String nama_pohon_kinerja;

    private String keterangan;
}
