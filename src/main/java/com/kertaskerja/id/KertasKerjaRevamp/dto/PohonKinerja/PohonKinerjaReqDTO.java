package com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PohonKinerjaReqDTO {
    @NotBlank(message = "Nama pohon kinerja tidak boleh kosong")
    private String nama_pohon_kinerja;

    private String keterangan;
}

