package com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Tactical;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TacticalReqDTO {
    @NotBlank
    private String id_strategic;

    @NotBlank(message = "Nama Pokin Tactical tidak boleh kosong")
    private String nama_pohon_kinerja;

    private String keterangan;
}
