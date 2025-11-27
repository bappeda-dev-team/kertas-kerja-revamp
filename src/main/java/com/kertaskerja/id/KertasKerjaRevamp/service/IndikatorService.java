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

@Service
@RequiredArgsConstructor
public class IndikatorService {

    private final IndikatorRepository repository;
    // TAMBAHAN: Kita butuh ini buat ambil target
    private final TargetRepository targetRepository;

    public List<IndikatorDto.Response> findAll() {
        List<Indikator> indikators = repository.findAll();
        // Optimasi: Ambil semua target dulu biar ga N+1 Query
        List<Target> allTargets = targetRepository.findAll();

        // Grouping Target berdasarkan Indikator ID
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

        // Disini kita bisa ambil target spesifik atau semua (tergantung performa).
        // Untuk simpelnya, kita ambil semua target lalu filter di memory (sama seperti findAll)
        // Atau kalau mau lebih irit, query targetRepository.findByListIndikatorIds(...) -> tapi repo belum support
        List<Target> allTargets = targetRepository.findAll();

        Map<Long, List<Target>> targetMap = allTargets.stream()
                .collect(Collectors.groupingBy(Target::getIndikatorId));

        return indikators.stream()
                .map(ind -> {
                    List<Target> myTargets = targetMap.getOrDefault(ind.getId(), new ArrayList<>());
                    return mapToResponse(ind, myTargets);
                })
                .toList();
    }

    public IndikatorDto.Response findById(Long id) {
        Indikator indikator = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Indikator tidak ditemukan"));

        // Ambil target khusus untuk indikator ini
        List<Target> targets = targetRepository.findByIndikatorId(id);

        return mapToResponse(indikator, targets);
    }

    @Transactional
    public IndikatorDto.Response create(IndikatorDto.Request request) {
        Indikator indikator = mapToEntity(request);
        Indikator savedIndikator = repository.save(indikator);

        // Saat create baru, target pasti masih kosong
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

        // Ambil target yang sudah ada (biar return response-nya lengkap)
        List<Target> targets = targetRepository.findByIndikatorId(id);

        return mapToResponse(indikatorBaru, targets);
    }

    @Transactional
    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Indikator tidak ditemukan"));
        repository.delete(id);
    }

    // --- MAPPING HELPERS ---

    // Update Mapper: Terima List<Target> dari luar
    private IndikatorDto.Response mapToResponse(Indikator entity, List<Target> targets) {
        // Konversi List<Target> ke List<TargetDto.Response>
        List<TargetDto.Response> target = targets.stream()
                .map(this::mapTargetToResponse)
                .toList();

        return new IndikatorDto.Response(
                entity.getId(),
                entity.getPohonKinerjaId(),
                entity.getIndikator(),
                entity.getKeterangan(),
                entity.getTahun(),
                target // Masukkan ke DTO
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