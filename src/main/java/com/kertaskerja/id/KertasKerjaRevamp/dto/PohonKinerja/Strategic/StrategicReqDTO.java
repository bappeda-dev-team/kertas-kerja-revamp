package com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Strategic;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StrategicReqDTO {
    @NotBlank
    private String id_sub_sub_tematik;

    @NotBlank(message = "Nama Pokin Strategic tidak boleh kosong")
    private String nama_pohon_kinerja;

    private String keterangan;
}
