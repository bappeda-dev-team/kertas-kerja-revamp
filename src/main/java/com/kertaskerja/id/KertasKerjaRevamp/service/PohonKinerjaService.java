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

import java.util.*;
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

        if (nodeCheck.getParentId() != null || nodeCheck.getLevelPohon() != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID " + id + " bukan id TEMATIK (Level 0).");
        }

        List<PohonKinerja> specificTreeNodes = pohonKinerjaRepository.findTreeNodes(id);
        Map<Long, PohonKinerjaDto.TreeResponse> nodeMap = buildNodeMapFromEntities(specificTreeNodes);
        PohonKinerjaDto.TreeResponse rootNode = nodeMap.get(id);

        if (rootNode != null) {
            buildHierarchy(rootNode, nodeMap);
        }
        return rootNode;
    }

    public PohonKinerjaDto.OpdTreeResponse getOpdTreeByKodeOpdAndTahun(String kodeOpd, Integer tahun) {
        List<PohonKinerja> allOpdNodes = pohonKinerjaRepository.findAllByKodeOpdAndTahun(kodeOpd, tahun);

        if (allOpdNodes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data tidak ditemukan");
        }

        Map<Long, PohonKinerjaDto.TreeResponse> nodeMap = buildNodeMapFromEntities(allOpdNodes);
        List<PohonKinerjaDto.TreeResponse> roots = allOpdNodes.stream()
                .filter(p -> p.getParentId() == null && p.getLevelPohon() == 0)
                .map(p -> nodeMap.get(p.getId()))
                .filter(Objects::nonNull)
                .toList();

        for (PohonKinerjaDto.TreeResponse root : roots) {
            buildHierarchy(root, nodeMap);
        }

        return PohonKinerjaDto.OpdTreeResponse.builder()
                .kodeOpd(kodeOpd)
                .tahun(tahun)
                .roots(roots)
                .build();
    }

    public List<PohonKinerjaDto.TreeResponse> getStrategicListByKodeOpdAndTahun(String kodeOpd, Integer tahun) {
        return pohonKinerjaRepository.findRootsByKodeOpdAndTahun(kodeOpd, tahun)
                .stream().map(this::mapToTreeDto).toList();
    }

    @Transactional(readOnly = true)
    public PohonKinerjaDto.TematikWrapper findAllTematik(Integer tahun) {
        List<PohonKinerjaDto.TematikItem> listTematik = pohonKinerjaRepository.findTematikByTahun(tahun);
        return new PohonKinerjaDto.TematikWrapper(tahun, listTematik);
    }

    @Transactional
    public PohonKinerjaDto.DetailResponse create(PohonKinerjaDto.Request request) {
        validateBusinessRules(request);

        PohonKinerja entity = PohonKinerja.builder()
                .parentId(request.parentId()).namaPohon(request.namaPohon())
                .keterangan(request.keterangan()).tahun(request.tahun())
                .jenisPohon(request.jenisPohon()).levelPohon(request.levelPohon())
                .kodeOpd(request.kodeOpd()).kodePemda(request.kodePemda())
                .status("DRAFT").build();

        PohonKinerja saved = pohonKinerjaRepository.save(entity);
        saveIndikatorsAndTargets(saved.getId(), request.indikators());
        return mapToDetailResponse(saved);
    }

    @Transactional
    public PohonKinerjaDto.DetailResponse update(Long id, PohonKinerjaDto.Request request) {
        PohonKinerja existing = pohonKinerjaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!Objects.equals(existing.getParentId(), request.parentId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mutasi struktur dilarang");
        }

        validateBusinessRules(request);
        validateChildrenConsistency(id, request);

        existing.setNamaPohon(request.namaPohon());
        existing.setKeterangan(request.keterangan());
        existing.setTahun(request.tahun());
        existing.setJenisPohon(request.jenisPohon());
        existing.setLevelPohon(request.levelPohon());
        existing.setKodeOpd(request.kodeOpd());
        existing.setKodePemda(request.kodePemda());
        existing.setStatus(request.status());

        pohonKinerjaRepository.save(existing);
        syncIndikators(existing.getId(), request.indikators());
        return mapToDetailResponse(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!pohonKinerjaRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        pohonKinerjaRepository.deleteById(id);
    }

    private Map<Long, PohonKinerjaDto.TreeResponse> buildNodeMapFromEntities(List<PohonKinerja> nodes) {
        List<Long> ids = nodes.stream().map(PohonKinerja::getId).toList();
        List<Indikator> indikators = indikatorRepository.findByPohonKinerjaIdIn(ids);
        List<Long> indIds = indikators.stream().map(Indikator::getId).toList();
        List<Target> targets = indIds.isEmpty() ? Collections.emptyList() : targetRepository.findByIndikatorIdIn(indIds);

        Map<Long, List<Target>> targetsByIndId = targets.stream().collect(Collectors.groupingBy(Target::getIndikatorId));
        Map<Long, List<PohonKinerjaDto.IndikatorResponse>> indMapByPohonId = new HashMap<>();

        for (Indikator ind : indikators) {
            List<PohonKinerjaDto.TargetResponse> tDtos = targetsByIndId.getOrDefault(ind.getId(), Collections.emptyList())
                    .stream().map(t -> new PohonKinerjaDto.TargetResponse(t.getId(), t.getNilai(), t.getSatuan(), t.getTahun())).toList();

            indMapByPohonId.computeIfAbsent(ind.getPohonKinerjaId(), k -> new ArrayList<>())
                    .add(new PohonKinerjaDto.IndikatorResponse(ind.getId(), ind.getIndikator(), ind.getKeterangan(), ind.getTahun(), tDtos));
        }

        Map<Long, PohonKinerjaDto.TreeResponse> nodeMap = new HashMap<>();
        for (PohonKinerja p : nodes) {
            PohonKinerjaDto.TreeResponse dto = mapToTreeDto(p);
            dto.setIndikator(indMapByPohonId.getOrDefault(p.getId(), new ArrayList<>()));
            nodeMap.put(p.getId(), dto);
        }
        return nodeMap;
    }

    private void buildHierarchy(PohonKinerjaDto.TreeResponse parent, Map<Long, PohonKinerjaDto.TreeResponse> allNodes) {
        for (PohonKinerjaDto.TreeResponse candidate : allNodes.values()) {
            if (Objects.equals(candidate.getParentId(), parent.getId())) {
                parent.getChildren().add(candidate);
                buildHierarchy(candidate, allNodes);
            }
        }
    }

    private void validateBusinessRules(PohonKinerjaDto.Request req) {
        if (req.jenisPohon() == JenisPohon.OPERATIONAL_N) {
            if (req.levelPohon() < 7) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Level Operational_N min 7");
        } else if (req.jenisPohon().getLevel() != req.levelPohon()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Level tidak valid");
        }

        if (req.parentId() != null) {
            PohonKinerja parent = pohonKinerjaRepository.findById(req.parentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            if (req.kodeOpd() != null && !req.kodeOpd().isBlank()) {
                if (parent.getKodeOpd() == null && parent.getLevelPohon() != 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hierarki OPD Salah");
            }
        } else if (req.levelPohon() != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Harus level 0");
        }
    }

    private void validateChildrenConsistency(Long id, PohonKinerjaDto.Request req) {
        List<PohonKinerja> children = pohonKinerjaRepository.findByParentId(id);
        for (PohonKinerja child : children) {
            boolean valid = (req.levelPohon() == 0 && child.getLevelPohon() == 4) || (child.getLevelPohon() == req.levelPohon() + 1);
            if (!valid && child.getKodeOpd() != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Konflik level anak");
        }
    }

    private void saveIndikatorsAndTargets(Long pohonId, List<PohonKinerjaDto.IndikatorRequest> requests) {
        if (requests == null) return;
        for (PohonKinerjaDto.IndikatorRequest indReq : requests) {
            Indikator ind = indikatorRepository.save(Indikator.builder().pohonKinerjaId(pohonId)
                    .indikator(indReq.indikator()).keterangan(indReq.keterangan()).tahun(indReq.tahun()).build());
            if (indReq.targets() != null) {
                for (PohonKinerjaDto.TargetRequest tReq : indReq.targets()) {
                    targetRepository.save(Target.builder().indikatorId(ind.getId())
                            .nilai(tReq.nilai()).satuan(tReq.satuan()).tahun(tReq.tahun()).build());
                }
            }
        }
    }

    private void syncIndikators(Long pohonId, List<PohonKinerjaDto.IndikatorRequest> requests) {
        List<Indikator> currentInds = indikatorRepository.findByPohonKinerjaId(pohonId);
        Set<Long> reqIds = requests == null ? Collections.emptySet() :
                requests.stream().map(PohonKinerjaDto.IndikatorRequest::id).filter(Objects::nonNull).collect(Collectors.toSet());

        currentInds.stream().filter(i -> !reqIds.contains(i.getId())).forEach(i -> {
            targetRepository.deleteByIndikatorId(i.getId());
            indikatorRepository.deleteById(i.getId());
        });

        if (requests != null) {
            for (PohonKinerjaDto.IndikatorRequest req : requests) {
                Indikator ind = (req.id() != null) ? indikatorRepository.findById(req.id()).orElse(new Indikator()) : new Indikator();
                ind.setPohonKinerjaId(pohonId); ind.setIndikator(req.indikator());
                ind.setKeterangan(req.keterangan()); ind.setTahun(req.tahun());
                Indikator savedInd = indikatorRepository.save(ind);
                syncTargets(savedInd.getId(), req.targets());
            }
        }
    }

    private void syncTargets(Long indId, List<PohonKinerjaDto.TargetRequest> requests) {
        List<Target> currentTargets = targetRepository.findByIndikatorId(indId);
        Set<Long> reqIds = requests == null ? Collections.emptySet() :
                requests.stream().map(PohonKinerjaDto.TargetRequest::id).filter(Objects::nonNull).collect(Collectors.toSet());

        currentTargets.stream().filter(t -> !reqIds.contains(t.getId())).forEach(t -> targetRepository.deleteById(t.getId()));

        if (requests != null) {
            for (PohonKinerjaDto.TargetRequest req : requests) {
                Target t = (req.id() != null) ? targetRepository.findById(req.id()).orElse(new Target()) : new Target();
                t.setIndikatorId(indId); t.setNilai(req.nilai()); t.setSatuan(req.satuan()); t.setTahun(req.tahun());
                targetRepository.save(t);
            }
        }
    }

    public PohonKinerjaDto.CountPohonResponse countPohonByKodeOpdAndTahun(String kodeOpd, Integer tahun) {
        int total = pohonKinerjaRepository.countTotalByKodeOpdAndTahun(kodeOpd, tahun);
        List<PohonKinerjaDto.CountLevelDetail> details = pohonKinerjaRepository.countByKodeOpdAndTahunGroupByLevel(kodeOpd, tahun);

        return new PohonKinerjaDto.CountPohonResponse(
                kodeOpd,
                tahun,
                total,
                details
        );
    }

    private PohonKinerjaDto.TreeResponse mapToTreeDto(PohonKinerja e) {
        return PohonKinerjaDto.TreeResponse.builder().id(e.getId()).parentId(e.getParentId())
                .namaPohon(e.getNamaPohon()).keterangan(e.getKeterangan()).tahun(e.getTahun())
                .jenisPohon(e.getJenisPohon().name()).levelPohon(e.getLevelPohon())
                .status(e.getStatus()).children(new ArrayList<>()).build();
    }

    private PohonKinerjaDto.SimpleResponse mapToSimpleResponse(PohonKinerja e) {
        return new PohonKinerjaDto.SimpleResponse(e.getId(), e.getParentId(), e.getNamaPohon(), e.getTahun(),
                e.getJenisPohon().name(), e.getLevelPohon(), e.getKodeOpd(), e.getKodePemda(), e.getStatus());
    }

    private PohonKinerjaDto.DetailResponse mapToDetailResponse(PohonKinerja e) {
        return new PohonKinerjaDto.DetailResponse(e.getId(), e.getParentId(), e.getNamaPohon(), e.getKeterangan(),
                e.getTahun(), e.getJenisPohon(), e.getLevelPohon(), e.getKodeOpd(), e.getKodePemda(), e.getStatus(), new ArrayList<>());
    }
}