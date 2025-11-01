package com.kertaskerja.cc.service;

import com.kertaskerja.cc.dto.IndikatorRequest;
import com.kertaskerja.cc.dto.TematikRequest;
import com.kertaskerja.cc.dto.TematikItem;
import com.kertaskerja.cc.dto.TematikResponse;

import java.util.List;
import java.util.Map;

public interface TematikService {
    boolean existsByIdTematik(String idTematik);
    boolean existsByNamaPohonKinerja(String nama);

    Map<String, Object> createTematik(TematikRequest request);

    Map<String, Object> createIndikator(IndikatorRequest request);

    List<Map<String, Object>> getAllTematikLikeData();
    List<TematikItem> getAllTematikWithIndicators();

    TematikResponse getTematikAsExample();
}
