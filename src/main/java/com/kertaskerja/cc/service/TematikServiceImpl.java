package com.kertaskerja.cc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.kertaskerja.cc.dto.IndikatorItem;
import com.kertaskerja.cc.dto.IndikatorRequest;
import com.kertaskerja.cc.dto.TargetItem;
import com.kertaskerja.cc.dto.TematikItem;
import com.kertaskerja.cc.dto.TematikRequest;
import com.kertaskerja.cc.dto.TematikResponse;
import com.kertaskerja.cc.repository.IndikatorRepository;
import com.kertaskerja.cc.repository.TematikRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
class TematikServiceImpl implements TematikService {

    private final TematikRepository tematikRepository;
    private final IndikatorRepository indikatorRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean existsByIdTematik(String idTematik) {
        return tematikRepository.existsByIdTematik(idTematik);
    }

    @Override
    public boolean existsByNamaPohonKinerja(String nama) {
        return tematikRepository.existsByNamaPohonKinerja(nama);
    }

    @Override
    public Map<String, Object> createTematik(TematikRequest request) {
        tematikRepository.insert(request);

        Map<String, Object> body = new HashMap<>();
        body.put("id_tematik", request.getId_tematik());
        body.put("nama_pohon_kinerja", request.getNama_pohon_kinerja());
        body.put("jenis_pohon_kinerja", request.getJenis_pohon_kinerja());
        body.put("level_pohon_kinerja", request.getLevel_pohon_kinerja());
        body.put("keterangan", request.getKeterangan());
        body.put("jenis_child", request.getJenis_child());
        return body;
    }

    @Override
    public Map<String, Object> createIndikator(IndikatorRequest request) {
        // id_sub_tematik may be empty string or "-" -> store as null
        String idSub = (request.getId_sub_tematik() == null ||
                       request.getId_sub_tematik().isBlank() ||
                       "-".equals(request.getId_sub_tematik())) ? null : request.getId_sub_tematik();

        String targetJson;
        try {
            targetJson = objectMapper.writeValueAsString(request.getTarget());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid target format");
        }

        indikatorRepository.insert(
                request.getId_tematik(),
                idSub,
                request.getIndikator(),
                request.getTahun(),
                targetJson
        );

        Map<String, Object> body = new HashMap<>();
        body.put("id_tematik", request.getId_tematik());
        body.put("id_sub_tematik", idSub == null ? "" : idSub);
        body.put("indikator", request.getIndikator());
        body.put("tahun", request.getTahun());
        body.put("target", request.getTarget());
        return body;
    }

    @Override
    public List<Map<String, Object>> getAllTematikLikeData() {
        return indikatorRepository.findAllForExample();
    }

    @Override
    public List<TematikItem> getAllTematikWithIndicators() {
        List<Map<String, Object>> rawData = tematikRepository.findAllTematikWithIndicators();

        Map<String, TematikItem> grouped = new HashMap<>();

        for (Map<String, Object> row : rawData) {
            String idTematik = (String) row.get("id_tematik");

            TematikItem tematik = grouped.get(idTematik);
            if (tematik == null) {
                tematik = new TematikItem();
                tematik.setNamaPohonKinerja((String) row.get("nama_pohon_kinerja"));
                tematik.setJenisPohonKinerja((String) row.get("jenis_pohon_kinerja"));
                tematik.setLevelPohonKinerja((String) row.get("level_pohon_kinerja"));
                tematik.setKeterangan((String) row.get("keterangan"));
                tematik.setJenisChild((String) row.get("jenis_child"));
                grouped.put(idTematik, tematik);
            }

            if (row.get("indikator") != null) {
                IndikatorItem indikatorItem = new IndikatorItem();
                indikatorItem.setIndikator((String) row.get("indikator"));
                indikatorItem.setTahun((Integer) row.get("tahun"));

                Object targetObj = row.get("target");
                List<TargetItem> targets = new ArrayList<>();
                if (targetObj != null) {
                    try {
                        // Convert already-parsed target object into List<TargetItem>
                        CollectionType listType = objectMapper.getTypeFactory()
                                .constructCollectionType(List.class, TargetItem.class);
                        targets = objectMapper.convertValue(targetObj, listType);
                    } catch (IllegalArgumentException ex) {
                        targets = new ArrayList<>();
                    }
                }
                indikatorItem.setTarget(targets);

                tematik.getIndikator().add(indikatorItem);
            }
        }

        return new ArrayList<>(grouped.values());
    }

    @Override
    public TematikResponse getTematikAsExample() {
        List<TematikItem> items = getAllTematikWithIndicators();
        if (items == null || items.isEmpty()) {
            return TematikResponse.builder()
                    .indikator(List.of())
                    .build();
        }
        TematikItem first = items.get(0);
        return TematikResponse.builder()
                .namaPohonKinerja(first.getNamaPohonKinerja())
                .jenisPohonKinerja(first.getJenisPohonKinerja())
                .levelPohonKinerja(first.getLevelPohonKinerja())
                .keterangan(first.getKeterangan())
                .jenisChild(first.getJenisChild())
                .indikator(first.getIndikator())
                .build();
    }
}
