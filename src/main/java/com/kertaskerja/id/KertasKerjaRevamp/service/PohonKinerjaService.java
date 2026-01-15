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
import java.util.Objects;

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

            int parentLevel = parent.getLevelPohon();
            int childLevel = request.levelPohon();
            boolean isHierarchyValid = false;

            switch (parentLevel) {
                case 0:
                    isHierarchyValid = (childLevel == 1 || childLevel == 4);
                    break;
                case 1:
                    isHierarchyValid = (childLevel == 2 || childLevel == 4);
                    break;
                case 2:
                    isHierarchyValid = (childLevel == 3 || childLevel == 4);
                    break;
                case 3:
                    isHierarchyValid = (childLevel == 4);
                    break;
                case 4:
                    isHierarchyValid = (childLevel == 5);
                    break;
                case 5:
                    isHierarchyValid = (childLevel == 6);
                    break;
                default:
            }

            if (!isHierarchyValid) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Hierarki salah. Parent level " + parentLevel + " (" + parent.getJenisPohon() +
                                ") tidak diizinkan memiliki anak level " + childLevel + " (" + request.jenisPohon() + ").");
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
        PohonKinerja existing = pohonKinerjaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pohon tidak ditemukan"));

        if (!Objects.equals(existing.getParentId(), request.parentId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Update Gagal: Parent ID tidak boleh diubah (Mutasi struktur dilarang).");
        }

        if (request.jenisPohon().getLevel() != request.levelPohon()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Level Pohon tidak sesuai dengan Jenis Pohon. " +
                            request.jenisPohon() + " harus level " + request.jenisPohon().getLevel());
        }

        if (request.parentId() != null) {
            PohonKinerja parent = pohonKinerjaRepository.findById(request.parentId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Parent ID " + request.parentId() + " tidak ditemukan"));

            int parentLevel = parent.getLevelPohon();
            int currentLevel = request.levelPohon();
            boolean isParentValid = false;

            switch (parentLevel) {
                case 0: isParentValid = (currentLevel == 1 || currentLevel == 4); break;
                case 1: isParentValid = (currentLevel == 2 || currentLevel == 4); break;
                case 2: isParentValid = (currentLevel == 3 || currentLevel == 4); break;
                case 3: isParentValid = (currentLevel == 4); break;
                case 4: isParentValid = (currentLevel == 5); break;
                case 5: isParentValid = (currentLevel == 6); break;
                default:
            }

            if (!isParentValid) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Hierarki ke Atas salah. Parent level " + parentLevel + " (" + parent.getJenisPohon() +
                                ") tidak diizinkan memiliki anak level " + currentLevel + " (" + request.jenisPohon() + ").");
            }
        } else {
            if (request.levelPohon() != 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Pohon dengan level " + request.levelPohon() + " wajib memiliki Parent ID.");
            }
        }

        List<PohonKinerja> children = pohonKinerjaRepository.findByParentId(id);

        if (!children.isEmpty()) {
            int newLevelSelf = request.levelPohon();

            for (PohonKinerja child : children) {
                int childLevel = child.getLevelPohon();
                boolean isChildValid = false;

                switch (newLevelSelf) {
                    case 0: isChildValid = (childLevel == 1 || childLevel == 4); break;
                    case 1: isChildValid = (childLevel == 2 || childLevel == 4); break;
                    case 2: isChildValid = (childLevel == 3 || childLevel == 4); break;
                    case 3: isChildValid = (childLevel == 4); break;
                    case 4: isChildValid = (childLevel == 5); break;
                    case 5: isChildValid = (childLevel == 6); break;
                    case 6:
                        break;
                    default: isChildValid = false;
                }

                if (!isChildValid) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Update Gagal: Perubahan level menjadi " + newLevelSelf + " (" + request.jenisPohon() +
                                    ") konflik dengan Anak (ID: " + child.getId() + ") yang memiliki level " + childLevel +
                                    ". Harap sesuaikan anak terlebih dahulu.");
                }
            }
        }

        existing.setNamaPohon(request.namaPohon());
        existing.setKeterangan(request.keterangan());
        existing.setTahun(request.tahun());
        existing.setJenisPohon(request.jenisPohon());
        existing.setLevelPohon(request.levelPohon());
        existing.setKodeOpd(request.kodeOpd());
        existing.setKodePemda(request.kodePemda());
        existing.setStatus(request.status());

        pohonKinerjaRepository.save(existing);

        // Get existing indikators
        List<Indikator> existingIndikators = indikatorRepository.findByPohonKinerjaId(id);
        List<Long> existingIndikatorIds = existingIndikators.stream()
                .map(Indikator::getId)
                .toList();

        // Track which indikators are in the request
        List<Long> requestIndikatorIds = new ArrayList<>();

        if (request.indikators() != null) {
            for (PohonKinerjaDto.IndikatorRequest indReq : request.indikators()) {
                Indikator indikatorEntity;

                if (indReq.id() != null) {
                    // UPDATE existing indikator
                    indikatorEntity = indikatorRepository.findById(indReq.id())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Indikator dengan ID " + indReq.id() + " tidak ditemukan"));

                    indikatorEntity.setIndikator(indReq.indikator());
                    indikatorEntity.setKeterangan(indReq.keterangan());
                    indikatorEntity.setTahun(indReq.tahun());

                    requestIndikatorIds.add(indReq.id());
                } else {
                    // CREATE new indikator
                    indikatorEntity = Indikator.builder()
                            .pohonKinerjaId(existing.getId())
                            .indikator(indReq.indikator())
                            .keterangan(indReq.keterangan())
                            .tahun(indReq.tahun())
                            .build();
                }

                Indikator savedIndikator = indikatorRepository.save(indikatorEntity);
                Long savedIndikatorId = savedIndikator.getId();

                // Track newly created indikator to prevent deletion
                if (indReq.id() == null) {
                    requestIndikatorIds.add(savedIndikatorId);
                }

                // Handle targets for this indikator
                if (indReq.targets() != null) {
                    // Get existing targets for this indikator
                    List<Target> existingTargets = targetRepository.findByIndikatorId(savedIndikatorId);
                    List<Long> existingTargetIds = existingTargets.stream()
                            .map(Target::getId)
                            .toList();

                    List<Long> requestTargetIds = new ArrayList<>();

                    for (PohonKinerjaDto.TargetRequest targetReq : indReq.targets()) {
                        Target targetEntity;

                        if (targetReq.id() != null) {
                            // UPDATE existing target
                            targetEntity = targetRepository.findById(targetReq.id())
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                            "Target dengan ID " + targetReq.id() + " tidak ditemukan"));

                            targetEntity.setNilai(targetReq.nilai());
                            targetEntity.setSatuan(targetReq.satuan());
                            targetEntity.setTahun(targetReq.tahun());

                            requestTargetIds.add(targetReq.id());
                        } else {
                            // CREATE new target
                            targetEntity = Target.builder()
                                    .indikatorId(savedIndikatorId)
                                    .nilai(targetReq.nilai())
                                    .satuan(targetReq.satuan())
                                    .tahun(targetReq.tahun())
                                    .build();
                        }

                        Target savedTarget = targetRepository.save(targetEntity);

                        // Track newly created target to prevent deletion
                        if (targetReq.id() == null) {
                            requestTargetIds.add(savedTarget.getId());
                        }
                    }

                    // Delete targets that are no longer in the request
                    for (Long existingTargetId : existingTargetIds) {
                        if (!requestTargetIds.contains(existingTargetId)) {
                            targetRepository.deleteById(existingTargetId);
                        }
                    }
                } else {
                    // If no targets in request, delete all existing targets for this indikator
                    targetRepository.deleteByIndikatorId(savedIndikatorId);
                }
            }
        }

        // Delete indikators that are no longer in the request
        for (Long existingIndikatorId : existingIndikatorIds) {
            if (!requestIndikatorIds.contains(existingIndikatorId)) {
                targetRepository.deleteByIndikatorId(existingIndikatorId);
                indikatorRepository.deleteById(existingIndikatorId);
            }
        }

        return mapToResponse(existing);
    }

    @Transactional
    public void delete(Long id) {
        pohonKinerjaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        pohonKinerjaRepository.deleteById(id);
    }

    private Map<Long, PohonKinerjaDto.TreeResponse> buildNodeMap(List<PohonKinerja> pohonList, List<Indikator> indikatorList, List<Target> targetList) {

        Map<Long, List<Target>> targetsByIndikatorId = targetList.stream()
                .collect(Collectors.groupingBy(Target::getIndikatorId));

        Map<Long, List<PohonKinerjaDto.IndikatorResponse>> indikatorMap = new HashMap<>();

        for (Indikator ind : indikatorList) {
            List<Target> myTargets = targetsByIndikatorId.getOrDefault(ind.getId(), new ArrayList<>());

            List<PohonKinerjaDto.TargetResponse> targetDtos = myTargets.stream()
                    .map(this::mapTargetToResponse)
                    .toList();

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