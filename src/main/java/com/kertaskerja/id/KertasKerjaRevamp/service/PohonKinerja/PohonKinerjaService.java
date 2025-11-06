package com.kertaskerja.id.KertasKerjaRevamp.service.PohonKinerja;

import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Indikator.IndikatorResDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.PohonKinerjaReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.PohonKinerjaResDTO;

public interface PohonKinerjaService {

    PohonKinerjaResDTO getPohonKinerjaByIdTematik(String idTematik);

    PohonKinerjaResDTO.SaveResponse savePohonKinerja(String jenisPokin, Object dto);

    PohonKinerjaResDTO.SaveResponse updatePohonKinerja(String idPokin, PohonKinerjaReqDTO dto);

    void deletePohonKinerja(String idPokin);

    IndikatorResDTO saveIndikator(String jenisPokin, Object dto);

}
