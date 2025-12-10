package com.kertaskerja.id.KertasKerjaRevamp.service;

import com.kertaskerja.id.KertasKerjaRevamp.dto.TargetDto;
import com.kertaskerja.id.KertasKerjaRevamp.model.Target;
import com.kertaskerja.id.KertasKerjaRevamp.repository.IndikatorRepository;
import com.kertaskerja.id.KertasKerjaRevamp.repository.PohonKinerjaRepository;
import com.kertaskerja.id.KertasKerjaRevamp.repository.TargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TargetService {

    private final TargetRepository repository;
    private final IndikatorRepository indikatorRepository;

    public List<TargetDto.Response> findAll() {
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<TargetDto.Response> findByIndikatorId(Long indikatorId) {
        return repository.findByIndikatorId(indikatorId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public TargetDto.Response findById(Long id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target tidak ditemukan"));
    }

    @Transactional
    public TargetDto.Response create(TargetDto.Request request) {
        if (!indikatorRepository.existsById(request.indikatorId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Pohon Kinerja dengan ID " + request.indikatorId() + " tidak ditemukan");
        }

        Target target = mapToEntity(request);
        Target savedTarget = repository.save(target);
        return mapToResponse(savedTarget);
    }

    @Transactional
    public TargetDto.Response update(Long id, TargetDto.Request request) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target tidak ditemukan"));

        Target target = mapToEntity(request);

        target.setId(id);

        repository.update(target);

        return mapToResponse(target);
    }

    @Transactional
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target tidak ditemukan"));

        repository.delete(id);
    }

    private TargetDto.Response mapToResponse(Target entity) {
        return new TargetDto.Response(
                entity.getId(),
                entity.getIndikatorId(),
                entity.getNilai(),
                entity.getSatuan(),
                entity.getTahun()
        );
    }

    private Target mapToEntity(TargetDto.Request request) {
        return Target.builder()
                .indikatorId(request.indikatorId())
                .nilai(request.nilai())
                .satuan(request.satuan())
                .tahun(request.tahun())
                .build();
    }
}