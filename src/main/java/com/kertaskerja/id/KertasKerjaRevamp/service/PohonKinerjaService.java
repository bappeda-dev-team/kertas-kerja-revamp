package com.kertaskerja.id.KertasKerjaRevamp.service;

import com.kertaskerja.id.KertasKerjaRevamp.dto.IndikatorDto;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerjaDto;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonTreeDto;
import com.kertaskerja.id.KertasKerjaRevamp.dto.TargetDto;
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

    public PohonTreeDto getTreeById(Long id) {
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

        Map<Long, PohonTreeDto> nodeMap = buildNodeMap(specificTreeNodes, relevantIndikators, relevantTargets);

        PohonTreeDto rootNode = nodeMap.get(id);

        if (rootNode != null) {
            buildHierarchy(rootNode, nodeMap);
        }

        return rootNode;
    }

    public List<PohonTreeDto> getFullTree() {
        List<PohonKinerja> allPohon = pohonKinerjaRepository.findAll();
        List<Indikator> allIndikator = indikatorRepository.findAll();
        List<Target> allTarget = targetRepository.findAll();

        Map<Long, PohonTreeDto> nodeMap = buildNodeMap(allPohon, allIndikator, allTarget);
        List<PohonTreeDto> roots = new ArrayList<>();

        for (PohonTreeDto node : nodeMap.values()) {
            if (node.getParentId() == null) {
                roots.add(node);
            } else {
                PohonTreeDto parent = nodeMap.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);
                }
            }
        }
        return roots;
    }

    @Transactional
    public PohonKinerjaDto.Response create(PohonKinerjaDto.Request request) {
        PohonKinerja entity = mapToEntity(request);
        PohonKinerja saved = pohonKinerjaRepository.save(entity);
        return mapToResponse(saved);
    }

    @Transactional
    public PohonKinerjaDto.Response update(Long id, PohonKinerjaDto.Request request) {
        PohonKinerja existing = pohonKinerjaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        existing.setParentId(request.parentId());
        existing.setNamaPohon(request.namaPohon());
        existing.setKeterangan(request.keterangan());
        existing.setTahun(request.tahun());
        existing.setJenisPohon(JenisPohon.valueOf(request.jenisPohon()));
        existing.setLevelPohon(request.levelPohon());
        existing.setKodeOpd(request.kodeOpd());
        existing.setKodePemda(request.kodePemda());
        existing.setStatus(request.status());
        pohonKinerjaRepository.update(existing);
        return mapToResponse(existing);
    }

    @Transactional
    public void delete(Long id) {
        pohonKinerjaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        pohonKinerjaRepository.deleteById(id);
    }

    public List<PohonKinerjaDto.Response> findAll() {
        return pohonKinerjaRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public PohonKinerjaDto.Response findById(Long id) {
        return pohonKinerjaRepository.findById(id).map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private Map<Long, PohonTreeDto> buildNodeMap(List<PohonKinerja> pohonList, List<Indikator> indikatorList, List<Target> targetList) {
        Map<Long, List<TargetDto.Response>> targetMap = targetList.stream()
                .map(this::mapTargetToResponse)
                .collect(Collectors.groupingBy(TargetDto.Response::indikatorId));

        Map<Long, List<IndikatorDto.Response>> indikatorMap = new HashMap<>();
        for (Indikator ind : indikatorList) {
            List<TargetDto.Response> myTargets = targetMap.getOrDefault(ind.getId(), new ArrayList<>());
            IndikatorDto.Response indDto = mapIndikatorToResponse(ind, myTargets);
            indikatorMap.computeIfAbsent(ind.getPohonKinerjaId(), k -> new ArrayList<>()).add(indDto);
        }

        Map<Long, PohonTreeDto> nodeMap = new HashMap<>();
        for (PohonKinerja p : pohonList) {
            PohonTreeDto dto = mapToTreeDto(p);
            if (indikatorMap.containsKey(p.getId())) {
                dto.setIndikator(indikatorMap.get(p.getId()));
            }
            nodeMap.put(p.getId(), dto);
        }
        return nodeMap;
    }

    private void buildHierarchy(PohonTreeDto parent, Map<Long, PohonTreeDto> allNodes) {
        for (PohonTreeDto candidate : allNodes.values()) {
            if (candidate.getParentId() != null && candidate.getParentId().equals(parent.getId())) {
                parent.getChildren().add(candidate);
                buildHierarchy(candidate, allNodes);
            }
        }
    }

    private PohonKinerjaDto.Response mapToResponse(PohonKinerja entity) {
        return new PohonKinerjaDto.Response(
                entity.getId(), entity.getParentId(), entity.getNamaPohon(), entity.getKeterangan(),
                entity.getTahun(), entity.getJenisPohon().name(), entity.getLevelPohon(),
                entity.getKodeOpd(), entity.getKodePemda(), entity.getStatus()
        );
    }

    private PohonTreeDto mapToTreeDto(PohonKinerja entity) {
        return PohonTreeDto.builder()
                .id(entity.getId())
                .parentId(entity.getParentId())
                .namaPohon(entity.getNamaPohon())
                .keterangan(entity.getKeterangan())
                .tahun(entity.getTahun())
                .jenisPohon(entity.getJenisPohon().name())
                .levelPohon(entity.getLevelPohon())
                .kodeOpd(entity.getKodeOpd())
                .kodePemda(entity.getKodePemda())
                .status(entity.getStatus())
                .build();
    }

    private PohonKinerja mapToEntity(PohonKinerjaDto.Request request) {
        return PohonKinerja.builder()
                .parentId(request.parentId())
                .namaPohon(request.namaPohon())
                .keterangan(request.keterangan())
                .tahun(request.tahun())
                .jenisPohon(JenisPohon.valueOf(request.jenisPohon()))
                .levelPohon(request.levelPohon())
                .kodeOpd(request.kodeOpd())
                .kodePemda(request.kodePemda())
                .status(request.status())
                .build();
    }

    private IndikatorDto.Response mapIndikatorToResponse(Indikator entity, List<TargetDto.Response> targets) {
        return new IndikatorDto.Response(
                entity.getId(), entity.getPohonKinerjaId(), entity.getIndikator(),
                entity.getKeterangan(), entity.getTahun(), targets
        );
    }

    private TargetDto.Response mapTargetToResponse(Target entity) {
        return new TargetDto.Response(
                entity.getId(), entity.getIndikatorId(), entity.getNilai(),
                entity.getSatuan(), entity.getTahun()
        );
    }
}