package com.kertaskerja.id.KertasKerjaRevamp.service;

import com.kertaskerja.id.KertasKerjaRevamp.dto.IndikatorDto;
import com.kertaskerja.id.KertasKerjaRevamp.dto.TargetDto;
import com.kertaskerja.id.KertasKerjaRevamp.model.Indikator;
import com.kertaskerja.id.KertasKerjaRevamp.model.Target;
import com.kertaskerja.id.KertasKerjaRevamp.repository.IndikatorRepository;
import com.kertaskerja.id.KertasKerjaRevamp.repository.TargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.kertaskerja.id.KertasKerjaRevamp.repository.PohonKinerjaRepository;

@Service
@RequiredArgsConstructor
public class IndikatorService {

    private final IndikatorRepository repository;
    private final TargetRepository targetRepository;
    private final PohonKinerjaRepository pohonKinerjaRepository;

    public List<IndikatorDto.Response> findAll() {
        List<Indikator> indikators = repository.findAll();
        List<Target> allTargets = targetRepository.findAll();

        return getResponses(indikators, allTargets);
    }

    private List<IndikatorDto.Response> getResponses(List<Indikator> indikators, List<Target> allTargets) {
        Map<Long, List<Target>> targetMap = allTargets.stream()
                .collect(Collectors.groupingBy(Target::getIndikatorId));

        return indikators.stream()
                .map(ind -> {
                    List<Target> myTargets = targetMap.getOrDefault(ind.getId(), new ArrayList<>());
                    return mapToResponse(ind, myTargets);
                })
                .toList();
    }

    public List<IndikatorDto.Response> findByPohonId(Long pohonId) {
        List<Indikator> indikators = repository.findByPohonKinerjaId(pohonId);

        List<Target> allTargets = targetRepository.findAll();

        return getResponses(indikators, allTargets);
    }

    public IndikatorDto.Response findById(Long id) {
        Indikator indikator = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Indikator tidak ditemukan"));

        List<Target> targets = targetRepository.findByIndikatorId(id);

        return mapToResponse(indikator, targets);
    }

    @Transactional
    public IndikatorDto.Response create(IndikatorDto.Request request) {
        if (!pohonKinerjaRepository.existsById(request.pohonKinerjaId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Pohon Kinerja dengan ID " + request.pohonKinerjaId() + " tidak ditemukan");
        }

        Indikator indikator = mapToEntity(request);
        Indikator savedIndikator = repository.save(indikator);

        return mapToResponse(savedIndikator, new ArrayList<>());
    }

    @Transactional
    public IndikatorDto.Response update(Long id, IndikatorDto.Request request) {
        Indikator existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Indikator tidak ditemukan"));

        Indikator indikatorBaru = mapToEntity(request);
        indikatorBaru.setId(id);
        indikatorBaru.setPohonKinerjaId(existing.getPohonKinerjaId());

        repository.update(indikatorBaru);

        List<Target> targets = targetRepository.findByIndikatorId(id);

        return mapToResponse(indikatorBaru, targets);
    }

    @Transactional
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Indikator tidak ditemukan"));
        repository.delete(id);
    }

    private IndikatorDto.Response mapToResponse(Indikator entity, List<Target> targets) {

        List<TargetDto.Response> target = targets.stream()
                .map(this::mapTargetToResponse)
                .toList();

        return new IndikatorDto.Response(
                entity.getId(),
                entity.getPohonKinerjaId(),
                entity.getIndikator(),
                entity.getKeterangan(),
                entity.getTahun(),
                target
        );
    }

    private TargetDto.Response mapTargetToResponse(Target entity) {
        return new TargetDto.Response(
                entity.getId(),
                entity.getIndikatorId(),
                entity.getNilai(),
                entity.getSatuan(),
                entity.getTahun()
        );
    }

    private Indikator mapToEntity(IndikatorDto.Request request) {
        return Indikator.builder()
                .pohonKinerjaId(request.pohonKinerjaId())
                .indikator(request.indikator())
                .keterangan(request.keterangan())
                .tahun(request.tahun())
                .build();
    }
}