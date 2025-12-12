package com.kertaskerja.id.KertasKerjaRevamp.service;

import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerjaDto;
import com.kertaskerja.id.KertasKerjaRevamp.enums.JenisPohon;
import com.kertaskerja.id.KertasKerjaRevamp.model.Indikator;
import com.kertaskerja.id.KertasKerjaRevamp.model.PohonKinerja;
import com.kertaskerja.id.KertasKerjaRevamp.model.Target;
import com.kertaskerja.id.KertasKerjaRevamp.repository.IndikatorRepository;
import com.kertaskerja.id.KertasKerjaRevamp.repository.PohonKinerjaRepository;
import com.kertaskerja.id.KertasKerjaRevamp.repository.TargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PohonKinerjaService {

    private final PohonKinerjaRepository pohonKinerjaRepository;
    private final IndikatorRepository indikatorRepository;
    private final TargetRepository targetRepository;

    public PohonKinerjaDto.TreeResponse getTreeById(Long id) {
        PohonKinerja nodeCheck = pohonKinerjaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pohon Kinerja tidak ditemukan"));

        boolean isRoot = nodeCheck.getParentId() == null
                && nodeCheck.getLevelPohon() == 0
                && nodeCheck.getJenisPohon() == JenisPohon.TEMATIK;

        if (!isRoot) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID " + id + " bukan id TEMATIK (Level 0).");
        }

        List<PohonKinerja> specificTreeNodes = pohonKinerjaRepository.findTreeNodes(id);

        List<Long> treeIds = specificTreeNodes.stream()
                .map(PohonKinerja::getId)
                .toList();

        List<Indikator> relevantIndikators = indikatorRepository.findByPohonKinerjaIdIn(treeIds);

        List<Long> indIds = relevantIndikators.stream()
                .map(Indikator::getId)
                .toList();

        List<Target> relevantTargets = targetRepository.findByIndikatorIdIn(indIds);

        Map<Long, PohonKinerjaDto.TreeResponse> nodeMap = buildNodeMap(specificTreeNodes, relevantIndikators, relevantTargets);

        PohonKinerjaDto.TreeResponse rootNode = nodeMap.get(id);

        if (rootNode != null) {
            buildHierarchy(rootNode, nodeMap);
        }

        return rootNode;
    }

    public List<PohonKinerjaDto.SimpleResponse> findAllTematik() {
        return pohonKinerjaRepository.findAllTematik().stream()
                .map(this::mapToSimpleResponse)
                .toList();
    }

    @Transactional
    public PohonKinerjaDto.DetailResponse create(PohonKinerjaDto.Request request) {
        if (request.jenisPohon().getLevel() != request.levelPohon()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Level Pohon tidak sesuai dengan Jenis Pohon. " +
                            request.jenisPohon() + " harus level " + request.jenisPohon().getLevel());
        }

        if (request.parentId() != null) {
            PohonKinerja parent = pohonKinerjaRepository.findById(request.parentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Parent ID " + request.parentId() + " tidak ditemukan"));

            int expectedLevel = parent.getLevelPohon() + 1;
            if (request.levelPohon() != expectedLevel) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Hierarki salah. Parent jenis " + parent.getJenisPohon() +
                                " (" + parent.getLevelPohon() + ") hanya boleh memiliki anak level " + expectedLevel);
            }
        } else {
            if (request.levelPohon() != 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Pohon dengan level " + request.levelPohon() + " wajib memiliki Parent ID.");
            }
        }

        PohonKinerja pohonEntity = PohonKinerja.builder()
                .parentId(request.parentId())
                .namaPohon(request.namaPohon())
                .keterangan(request.keterangan())
                .tahun(request.tahun())
                .jenisPohon(request.jenisPohon())
                .levelPohon(request.levelPohon())
                .kodeOpd(request.kodeOpd())
                .kodePemda(request.kodePemda())
                .status("DRAFT")
                .build();

        PohonKinerja savedPohon = pohonKinerjaRepository.save(pohonEntity);
        Long idPohonBaru = savedPohon.getId();

        if (request.indikators() != null) {
            for (PohonKinerjaDto.IndikatorRequest indReq : request.indikators()) {
                Indikator indikatorEntity = Indikator.builder()
                        .pohonKinerjaId(idPohonBaru)
                        .indikator(indReq.indikator())
                        .keterangan(indReq.keterangan())
                        .tahun(indReq.tahun())
                        .build();

                Indikator savedIndikator = indikatorRepository.save(indikatorEntity);
                Long idIndikatorBaru = savedIndikator.getId();

                if (indReq.targets() != null) {
                    for (PohonKinerjaDto.TargetRequest targetReq : indReq.targets()) {
                        Target targetEntity = Target.builder()
                                .indikatorId(idIndikatorBaru)
                                .nilai(targetReq.nilai())
                                .satuan(targetReq.satuan())
                                .tahun(targetReq.tahun())
                                .build();

                        targetRepository.save(targetEntity);
                    }
                }
            }
        }

        return mapToResponse(savedPohon);
    }

    @Transactional
    public PohonKinerjaDto.DetailResponse update(Long id, PohonKinerjaDto.Request request) {
        if (request.parentId() != null && request.parentId().equals(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Induk (Parent) tidak boleh diri sendiri. Itu ilegal!"
            );
        }

        PohonKinerja existing = pohonKinerjaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pohon tidak ditemukan"));

        existing.setParentId(request.parentId());
        existing.setNamaPohon(request.namaPohon());
        existing.setKeterangan(request.keterangan());
        existing.setTahun(request.tahun());
        existing.setJenisPohon(request.jenisPohon());
        existing.setLevelPohon(request.levelPohon());
        existing.setKodeOpd(request.kodeOpd());
        existing.setKodePemda(request.kodePemda());
        existing.setStatus(request.status());

        pohonKinerjaRepository.save(existing);

        List<Indikator> oldIndikators = indikatorRepository.findByPohonKinerjaId(id);

        for (Indikator oldInd : oldIndikators) {
            targetRepository.deleteByIndikatorId(oldInd.getId());
        }
        indikatorRepository.deleteAll(oldIndikators);

        if (request.indikators() != null) {
            for (PohonKinerjaDto.IndikatorRequest indReq : request.indikators()) {
                Indikator indikatorEntity = Indikator.builder()
                        .pohonKinerjaId(existing.getId())
                        .indikator(indReq.indikator())
                        .keterangan(indReq.keterangan())
                        .tahun(indReq.tahun())
                        .build();

                Indikator savedIndikator = indikatorRepository.save(indikatorEntity);
                Long idIndikatorBaru = savedIndikator.getId();

                if (indReq.targets() != null) {
                    for (PohonKinerjaDto.TargetRequest targetReq : indReq.targets()) {
                        Target targetEntity = Target.builder()
                                .indikatorId(idIndikatorBaru)
                                .nilai(targetReq.nilai())
                                .satuan(targetReq.satuan())
                                .tahun(targetReq.tahun())
                                .build();

                        targetRepository.save(targetEntity);
                    }
                }
            }
        }

        return mapToResponse(existing);
    }

    @Transactional
    public void delete(Long id) {
        pohonKinerjaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        pohonKinerjaRepository.deleteById(id);
    }

    // ===========================================
    // FIX STARTS HERE
    // ===========================================

    private Map<Long, PohonKinerjaDto.TreeResponse> buildNodeMap(List<PohonKinerja> pohonList, List<Indikator> indikatorList, List<Target> targetList) {
        // 1. Group TARGET ENTITY berdasarkan Indikator ID
        // (Kita group Entity-nya, bukan DTO-nya, supaya aman)
        Map<Long, List<Target>> targetsByIndikatorId = targetList.stream()
                .collect(Collectors.groupingBy(Target::getIndikatorId));

        // 2. Build Map IndikatorResponse
        Map<Long, List<PohonKinerjaDto.IndikatorResponse>> indikatorMap = new HashMap<>();

        for (Indikator ind : indikatorList) {
            // Ambil list target entity milik indikator ini
            List<Target> myTargets = targetsByIndikatorId.getOrDefault(ind.getId(), new ArrayList<>());

            // Map target entity ke DTO
            List<PohonKinerjaDto.TargetResponse> targetDtos = myTargets.stream()
                    .map(this::mapTargetToResponse)
                    .toList();

            // Buat IndikatorResponse baru
            PohonKinerjaDto.IndikatorResponse indDto = new PohonKinerjaDto.IndikatorResponse(
                    ind.getId(),
                    ind.getIndikator(),
                    ind.getKeterangan(),
                    ind.getTahun(),
                    targetDtos
            );

            // Masukkan ke map, key-nya adalah PohonKinerjaId
            indikatorMap.computeIfAbsent(ind.getPohonKinerjaId(), k -> new ArrayList<>()).add(indDto);
        }

        // 3. Build Node Map
        Map<Long, PohonKinerjaDto.TreeResponse> nodeMap = new HashMap<>();
        for (PohonKinerja p : pohonList) {
            PohonKinerjaDto.TreeResponse dto = mapToTreeDto(p);

            // Sekarang tipenya sudah cocok (sama-sama List<PohonKinerjaDto.IndikatorResponse>)
            if (indikatorMap.containsKey(p.getId())) {
                dto.setIndikator(indikatorMap.get(p.getId()));
            }
            nodeMap.put(p.getId(), dto);
        }
        return nodeMap;
    }

    // --- Helper Mappers yang sudah disesuaikan ---

    private PohonKinerjaDto.TargetResponse mapTargetToResponse(Target entity) {
        return new PohonKinerjaDto.TargetResponse(
                entity.getId(),
                entity.getNilai(),
                entity.getSatuan(),
                entity.getTahun()
        );
    }

    private PohonKinerjaDto.SimpleResponse mapToSimpleResponse(PohonKinerja entity) {
        return new PohonKinerjaDto.SimpleResponse(
                entity.getId(),
                entity.getParentId(),
                entity.getNamaPohon(),
                entity.getTahun(),
                entity.getJenisPohon().name(),
                entity.getLevelPohon(),
                entity.getKodeOpd(),
                entity.getKodePemda(),
                entity.getStatus()
        );
    }

    private PohonKinerjaDto.DetailResponse mapToResponse(PohonKinerja entity) {
        List<Indikator> indikators = indikatorRepository.findByPohonKinerjaId(entity.getId());
        List<PohonKinerjaDto.IndikatorResponse> indikatorDtos = new ArrayList<>();

        if (indikators != null && !indikators.isEmpty()) {
            List<Long> indIds = indikators.stream().map(Indikator::getId).toList();
            List<Target> allTargets = targetRepository.findByIndikatorIdIn(indIds);

            for (Indikator ind : indikators) {
                List<PohonKinerjaDto.TargetResponse> targetDtos = allTargets.stream()
                        .filter(t -> t.getIndikatorId().equals(ind.getId()))
                        .map(this::mapTargetToResponse)
                        .toList();

                indikatorDtos.add(new PohonKinerjaDto.IndikatorResponse(
                        ind.getId(),
                        ind.getIndikator(),
                        ind.getKeterangan(),
                        ind.getTahun(),
                        targetDtos
                ));
            }
        }

        return new PohonKinerjaDto.DetailResponse(
                entity.getId(),
                entity.getParentId(),
                entity.getNamaPohon(),
                entity.getKeterangan(),
                entity.getTahun(),
                entity.getJenisPohon(),
                entity.getLevelPohon(),
                entity.getKodeOpd(),
                entity.getKodePemda(),
                entity.getStatus(),
                indikatorDtos
        );
    }

    private PohonKinerjaDto.TreeResponse mapToTreeDto(PohonKinerja entity) {
        return PohonKinerjaDto.TreeResponse.builder()
                .id(entity.getId())
                .parentId(entity.getParentId())
                .namaPohon(entity.getNamaPohon())
                .keterangan(entity.getKeterangan())
                .tahun(entity.getTahun())
                .jenisPohon(entity.getJenisPohon().name())
                .levelPohon(entity.getLevelPohon())
                .status(entity.getStatus())
                .build();
    }

    private void buildHierarchy(PohonKinerjaDto.TreeResponse parent, Map<Long, PohonKinerjaDto.TreeResponse> allNodes) {
        for (PohonKinerjaDto.TreeResponse candidate : allNodes.values()) {
            if (candidate.getParentId() != null && candidate.getParentId().equals(parent.getId())) {
                parent.getChildren().add(candidate);
                buildHierarchy(candidate, allNodes);
            }
        }
    }
}