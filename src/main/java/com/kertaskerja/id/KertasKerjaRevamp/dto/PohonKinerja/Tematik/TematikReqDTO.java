package com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Tematik;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TematikReqDTO {
    @NotBlank(message = "Nama Pokin Tematik tidak boleh kosong")
    private String nama_pohon_kinerja;

    private String keterangan;
}