package com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Operational;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OperationalReqDTO {
    @NotBlank
    private String id_tactical;

    @NotBlank(message = "Nama Pokin Operational tidak boleh kosong")
    private String nama_pohon_kinerja;

    private String keterangan;
}
