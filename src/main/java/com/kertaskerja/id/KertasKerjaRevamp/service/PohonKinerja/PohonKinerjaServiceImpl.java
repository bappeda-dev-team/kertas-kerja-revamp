package com.kertaskerja.id.KertasKerjaRevamp.service.PohonKinerja;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Indikator.IndikatorReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Indikator.IndikatorResDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Operational.OperationalReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Operational.OperationalResDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.PohonKinerjaReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.PohonKinerjaResDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Strategic.StrategicReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Strategic.StrategicResDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.SubSubTematik.SubSubTematikReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.SubSubTematik.SubSubTematikResDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.SubTematik.SubTematikReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.SubTematik.SubTematikResDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Tactical.TacticalReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Tactical.TacticalResDTO;
import com.kertaskerja.id.KertasKerjaRevamp.dto.PohonKinerja.Tematik.TematikReqDTO;
import com.kertaskerja.id.KertasKerjaRevamp.enums.JenisPohonKinerjaConfig;
import com.kertaskerja.id.KertasKerjaRevamp.exception.BadRequestException;
import com.kertaskerja.id.KertasKerjaRevamp.exception.ConflictException;
import com.kertaskerja.id.KertasKerjaRevamp.exception.ResourceNotFoundException;
import com.kertaskerja.id.KertasKerjaRevamp.helper.Mixins;
import com.kertaskerja.id.KertasKerjaRevamp.repository.PohonKinerjaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PohonKinerjaServiceImpl implements PohonKinerjaService {

    private final PohonKinerjaRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PohonKinerjaResDTO getPohonKinerjaByIdTematik(String idTematik) {
        // 1️⃣ Fetch Tematik dan Sub-Tematik
        List<Map<String, Object>> tematikRows = repository.findOneTematik(idTematik);
        if (tematikRows.isEmpty()) return null;

        List<Map<String, Object>> subTematikRows = repository.findAllSubTematikByTematik(idTematik);

        // 2️⃣ Tematik indikator
        List<IndikatorResDTO> tematikIndikators = tematikRows.stream()
              .filter(row -> row.get("indikator") != null)
              .map(this::mapToIndikator)
              .collect(Collectors.toList());

        // 3️⃣ Build Sub-Tematik
        List<SubTematikResDTO> subTematikDTOs = new ArrayList<>();
        for (Map<String, Object> subRow : subTematikRows) {
            String idSubTematik = (String) subRow.get("id_sub_tematik");

            // Fetch sub-sub-tematik
            List<Map<String, Object>> subSubRows = repository.findAllSubSubTematikBySubTematik(idSubTematik);

            // Build indikator untuk sub-tematik
            List<IndikatorResDTO> subIndikators = subRow.get("indikator") == null
                  ? new ArrayList<>()
                  : Collections.singletonList(mapToIndikator(subRow));

            // 4️⃣ Build SubSubTematik
            List<SubSubTematikResDTO> subSubTematikDTOs = subSubRows.stream()
                  .collect(Collectors.groupingBy(row -> row.get("id_sub_sub_tematik")))
                  .entrySet().stream()
                  .map(entry -> {
                      Map<String, Object> first = entry.getValue().get(0);
                      String idSubSubTematik = (String) first.get("id_sub_sub_tematik");

                      List<IndikatorResDTO> subSubIndikators = entry.getValue().stream()
                            .filter(r -> r.get("indikator") != null)
                            .map(this::mapToIndikator)
                            .collect(Collectors.toList());

                      // 5️⃣ Fetch Strategic
                      List<Map<String, Object>> strategicRows = repository.findAllStrategicBySubSubTematik(idSubSubTematik);
                      List<StrategicResDTO> strategicDTOs = buildStrategic(strategicRows);

                      return SubSubTematikResDTO.builder()
                            .idSubSubTematik(idSubSubTematik)
                            .namaPohonKinerja((String) first.get("nama_pohon_kinerja"))
                            .jenisPohonKinerja((String) first.get("jenis_pohon_kinerja"))
                            .levelPohonKinerja(asInt(first.get("level_pohon_kinerja")))
                            .keterangan((String) first.get("keterangan"))
                            .jenisChild((String) first.get("jenis_child"))
                            .indikator(subSubIndikators)
                            .childs(strategicDTOs)
                            .build();
                  })
                  .collect(Collectors.toList());

            // 6️⃣ SubTematik final
            subTematikDTOs.add(SubTematikResDTO.builder()
                  .idSubTematik(idSubTematik)
                  .namaPohonKinerja((String) subRow.get("nama_pohon_kinerja"))
                  .jenisPohonKinerja((String) subRow.get("jenis_pohon_kinerja"))
                  .levelPohonKinerja(asInt(subRow.get("level_pohon_kinerja")))
                  .keterangan((String) subRow.get("keterangan"))
                  .jenisChild((String) subRow.get("jenis_child"))
                  .indikator(subIndikators)
                  .childs(subSubTematikDTOs)
                  .build());
        }

        // 7️⃣ Build final Tematik DTO
        Map<String, Object> tematik = tematikRows.get(0);
        return PohonKinerjaResDTO.builder()
              .idTematik((String) tematik.get("id_tematik"))
              .namaPohonKinerja((String) tematik.get("nama_pohon_kinerja"))
              .jenisPohonKinerja((String) tematik.get("jenis_pohon_kinerja"))
              .levelPohonKinerja(asInt(tematik.get("level_pohon_kinerja")))
              .keterangan((String) tematik.get("keterangan"))
              .jenisChild((String) tematik.get("jenis_child"))
              .indikator(tematikIndikators)
              .childs(subTematikDTOs)
              .build();
    }

    // 🧩 Helper: Build Strategic → Tactical → Operational tree
    private List<StrategicResDTO> buildStrategic(List<Map<String, Object>> strategicRows) {
        return strategicRows.stream()
              .collect(Collectors.groupingBy(r -> r.get("id_strategic")))
              .entrySet().stream()
              .map(entry -> {
                  Map<String, Object> first = entry.getValue().get(0);
                  String idStrategic = (String) first.get("id_strategic");

                  List<IndikatorResDTO> strategicIndikators = entry.getValue().stream()
                        .filter(r -> r.get("indikator") != null)
                        .map(this::mapToIndikator)
                        .collect(Collectors.toList());

                  // Fetch Tactical
                  List<Map<String, Object>> tacticalRows = repository.findAllTacticalByStrategic(idStrategic);
                  List<TacticalResDTO> tacticalDTOs = buildTactical(tacticalRows);

                  return StrategicResDTO.builder()
                        .idStrategic(idStrategic)
                        .namaPohonKinerja((String) first.get("nama_pohon_kinerja"))
                        .jenisPohonKinerja((String) first.get("jenis_pohon_kinerja"))
                        .levelPohonKinerja(asInt(first.get("level_pohon_kinerja")))
                        .keterangan((String) first.get("keterangan"))
                        .jenisChild((String) first.get("jenis_child"))
                        .indikator(strategicIndikators)
                        .childs(tacticalDTOs)
                        .build();
              })
              .collect(Collectors.toList());
    }

    private List<TacticalResDTO> buildTactical(List<Map<String, Object>> tacticalRows) {
        return tacticalRows.stream()
              .collect(Collectors.groupingBy(r -> r.get("id_tactical")))
              .entrySet().stream()
              .map(entry -> {
                  Map<String, Object> first = entry.getValue().get(0);
                  String idTactical = (String) first.get("id_tactical");

                  List<IndikatorResDTO> tacticalIndikators = entry.getValue().stream()
                        .filter(r -> r.get("indikator") != null)
                        .map(this::mapToIndikator)
                        .collect(Collectors.toList());

                  // Fetch Operational
                  List<Map<String, Object>> operationalRows = repository.findAllOperationalByTactical(idTactical);
                  List<OperationalResDTO> operationalDTOs = buildOperational(operationalRows);

                  return TacticalResDTO.builder()
                        .idTactical(idTactical)
                        .namaPohonKinerja((String) first.get("nama_pohon_kinerja"))
                        .jenisPohonKinerja((String) first.get("jenis_pohon_kinerja"))
                        .levelPohonKinerja(asInt(first.get("level_pohon_kinerja")))
                        .keterangan((String) first.get("keterangan"))
                        .jenisChild((String) first.get("jenis_child"))
                        .indikator(tacticalIndikators)
                        .childs(operationalDTOs)
                        .build();
              })
              .collect(Collectors.toList());
    }

    private List<OperationalResDTO> buildOperational(List<Map<String, Object>> operationalRows) {
        return operationalRows.stream()
              .collect(Collectors.groupingBy(r -> r.get("id_operational")))
              .entrySet().stream()
              .map(entry -> {
                  Map<String, Object> first = entry.getValue().get(0);

                  List<IndikatorResDTO> operationalIndikators = entry.getValue().stream()
                        .filter(r -> r.get("indikator") != null)
                        .map(this::mapToIndikator)
                        .collect(Collectors.toList());

                  return OperationalResDTO.builder()
                        .idOperational((String) first.get("id_operational"))
                        .namaPohonKinerja((String) first.get("nama_pohon_kinerja"))
                        .jenisPohonKinerja((String) first.get("jenis_pohon_kinerja"))
                        .levelPohonKinerja(asInt(first.get("level_pohon_kinerja")))
                        .keterangan((String) first.get("keterangan"))
                        .jenisChild((String) first.get("jenis_child"))
                        .indikator(operationalIndikators)
                        .build();
              })
              .collect(Collectors.toList());
    }

    // 🧩 Helper untuk parsing indikator
    private IndikatorResDTO mapToIndikator(Map<String, Object> row) {
        return IndikatorResDTO.builder()
              .indikator((String) row.get("indikator"))
              .tahun((Integer) row.get("tahun"))
              .target(parseTarget(row.get("target")))
              .build();
    }

    private Integer asInt(Object obj) {
        return obj == null ? null : ((Number) obj).intValue();
    }

    /**
     * Helper to parse 'target' JSON string into list of TargetItem
     */
    private List<IndikatorResDTO.TargetItem> parseTarget(Object targetObj) {
        if (targetObj == null) return new ArrayList<>();
        try {
            String targetJson = targetObj.toString();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(targetJson, new TypeReference<List<IndikatorResDTO.TargetItem>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public PohonKinerjaResDTO.SaveResponse savePohonKinerja(String jenisPokin, Object dto) {
        try {
            if (repository.existsByNamaPohonKinerja(getNama(dto))) {
                throw new ConflictException("Nama Pokin already exists");
            }

            String year = String.valueOf(LocalDate.now().getYear());
            JenisPohonKinerjaConfig config = JenisPohonKinerjaConfig.from(jenisPokin);
            String idPokin = Mixins.generateRandomId(config.getPrefix(), year);
            String parentId = extractParentId(dto, jenisPokin);

            validateParentExistence(parentId, jenisPokin);

            String idTematik = null;
            String idSubTematik = null;
            String idSubSubTematik = null;
            String idStrategic = null;
            String idTactical = null;
            String idOperational = null;

            switch (jenisPokin.toLowerCase()) {
                case "tematik" -> idTematik = idPokin;
                case "sub-tematik" -> {
                    idTematik = parentId;
                    idSubTematik = idPokin;
                }
                case "sub-sub-tematik" -> {
                    idSubTematik = parentId;
                    idSubSubTematik = idPokin;
                }
                case "strategic" -> {
                    idSubSubTematik = parentId;
                    idStrategic = idPokin;
                }
                case "tactical" -> {
                    idStrategic = parentId;
                    idTactical = idPokin;
                }
                case "operational" -> {
                    idTactical = parentId;
                    idOperational = idPokin;
                }
                default -> throw new IllegalArgumentException("Jenis Pokin tidak valid: " + jenisPokin);
            }

            Map<String, Object> result = repository.insert(
                  idTematik, idSubTematik, idSubSubTematik, idStrategic,
                  idTactical, idOperational, getNama(dto), "PEMDA",
                  config.getLevelPokin(), getKeterangan(dto), config.getJenisChild()
            );

            return new PohonKinerjaResDTO.SaveResponse(
                  idPokin,
                  (String) result.get("nama_pohon_kinerja"),
                  (String) result.get("jenis_pohon_kinerja"),
                  (Integer) result.get("level_pohon_kinerja"),
                  (String) result.get("keterangan")
            );
        } catch (ResourceNotFoundException | BadRequestException | IllegalArgumentException | ConflictException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save");
        }
    }

    @Override
    public PohonKinerjaResDTO.SaveResponse updatePohonKinerja(String idPokin, PohonKinerjaReqDTO dto) {
        try {
            if (repository.existsByNamaPohonKinerja(dto.getNama_pohon_kinerja())) {
                throw new ConflictException("Nama Pokin already exists");
            }

            String prefix = idPokin.split("-")[0];
            int updated = repository.update(prefix, idPokin, dto.getNama_pohon_kinerja(), dto.getKeterangan());

            if (updated == 0) {
                throw new ResourceNotFoundException("Pohon Kinerja with ID " + idPokin + " is not found.");
            }

            return new PohonKinerjaResDTO.SaveResponse(
                  idPokin,
                  dto.getNama_pohon_kinerja(),
                  null,
                  null,
                  dto.getKeterangan()
            );
        } catch (ResourceNotFoundException | BadRequestException | IllegalArgumentException | ConflictException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update");
        }
    }

    @Override
    public void deletePohonKinerja(String idPokin) {
        try {
            String prefix = idPokin.split("-")[0];

            String column = switch (prefix) {
                case "TMK" -> "id_tematik";
                case "STMK" -> "id_sub_tematik";
                case "SSTMK" -> "id_sub_sub_tematik";
                case "STR" -> "id_strategic";
                case "TAC" -> "id_tactical";
                case "OPR" -> "id_operational";
                default -> throw new IllegalArgumentException("Prefix ID tidak dikenali: " + prefix);
            };

            boolean exists = repository.existsByParentId(column, idPokin);
            if (!exists) {
                throw new ResourceNotFoundException("Data Pohon Kinerja dengan ID " + idPokin + " tidak ditemukan.");
            }

            repository.deleteIndikatorByPokin(column, idPokin);

            int deleted = repository.delete(column, idPokin);
            if (deleted == 0) {
                throw new ResourceNotFoundException("Gagal menghapus data Pohon Kinerja dengan ID " + idPokin);
            }

        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Gagal menghapus data Pohon Kinerja", e);
        }
    }

    @Override
    public IndikatorResDTO saveIndikator(String jenisPokin, Object dto) {
        try {
            String parentId = extractParentIdForIndikator(dto, jenisPokin);

            if (parentId != null && !repository.existsParentForIndikator(jenisPokin, parentId)) {
                throw new ResourceNotFoundException("Parent " + jenisPokin + " with ID " + parentId + " NOT FOUND, data cannot be saved.");
            }

            String indikator;
            Integer tahun;
            List<IndikatorReqDTO.TargetItem> targetList;

            switch (jenisPokin.toLowerCase()) {
                case "tematik" -> {
                    var d = (IndikatorReqDTO.IndikatorTematik) dto;
                    indikator = d.getIndikator();
                    tahun = d.getTahun();
                    targetList = d.getTarget();
                }
                case "sub-tematik" -> {
                    var d = (IndikatorReqDTO.IndikatorSubTematik) dto;
                    indikator = d.getIndikator();
                    tahun = d.getTahun();
                    targetList = d.getTarget();
                }
                case "sub-sub-tematik" -> {
                    var d = (IndikatorReqDTO.IndikatorSubSubTematik) dto;
                    indikator = d.getIndikator();
                    tahun = d.getTahun();
                    targetList = d.getTarget();
                }
                case "strategic" -> {
                    var d = (IndikatorReqDTO.IndikatorStrategic) dto;
                    indikator = d.getIndikator();
                    tahun = d.getTahun();
                    targetList = d.getTarget();
                }
                case "tactical" -> {
                    var d = (IndikatorReqDTO.IndikatorTactical) dto;
                    indikator = d.getIndikator();
                    tahun = d.getTahun();
                    targetList = d.getTarget();
                }
                case "operational" -> {
                    var d = (IndikatorReqDTO.IndikatorOperational) dto;
                    indikator = d.getIndikator();
                    tahun = d.getTahun();
                    targetList = d.getTarget();
                }
                default -> throw new IllegalArgumentException("Jenis Pokin tidak valid: " + jenisPokin);
            }

            repository.insertIndikator(jenisPokin, parentId, indikator, tahun, targetList);

            return IndikatorResDTO.builder()
                  .indikator(indikator)
                  .tahun(tahun)
                  .target(targetList.stream()
                        .map(t -> IndikatorResDTO.TargetItem.builder()
                              .nilai(t.getNilai())
                              .satuan(t.getSatuan())
                              .keterangan(t.getKeterangan())
                              .build())
                        .collect(Collectors.toList()))
                  .build();

        } catch (ResourceNotFoundException | BadRequestException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error saving indikator {}: {}", jenisPokin, e.getMessage(), e);
            throw new RuntimeException("Failed to save indikator " + jenisPokin);
        }
    }


    //    * ================
    //    ===== HELPER =====
    //    * ================


    private void validateParentExistence(String parentId, String jenisPokin) {
        if (parentId == null) return;

        boolean exists = switch (jenisPokin.toLowerCase()) {
            case "sub-tematik" -> repository.existsByParentId("id_tematik", parentId);
            case "sub-sub-tematik" -> repository.existsByParentId("id_sub_tematik", parentId);
            case "strategic" -> repository.existsByParentId("id_sub_sub_tematik", parentId);
            case "tactical" -> repository.existsByParentId("id_strategic", parentId);
            case "operational" -> repository.existsByParentId("id_tactical", parentId);
            default -> true;
        };

        if (!exists) {
            throw new ResourceNotFoundException(
                  "Parent " + jenisPokin + " with ID " + parentId + " is not found."
            );
        }
    }

    private String extractParentId(Object dto, String jenisPokin) {
        return switch (jenisPokin.toLowerCase()) {
            case "sub-tematik" -> ((SubTematikReqDTO) dto).getId_tematik();
            case "sub-sub-tematik" -> ((SubSubTematikReqDTO) dto).getId_sub_tematik();
            case "strategic" -> ((StrategicReqDTO) dto).getId_sub_sub_tematik();
            case "tactical" -> ((TacticalReqDTO) dto).getId_strategic();
            case "operational" -> ((OperationalReqDTO) dto).getId_tactical();
            default -> null;
        };
    }

    private String getNama(Object dto) {
        return switch (dto) {
            case TematikReqDTO d -> d.getNama_pohon_kinerja();
            case SubTematikReqDTO d -> d.getNama_pohon_kinerja();
            case SubSubTematikReqDTO d -> d.getNama_pohon_kinerja();
            case StrategicReqDTO d -> d.getNama_pohon_kinerja();
            case TacticalReqDTO d -> d.getNama_pohon_kinerja();
            case OperationalReqDTO d -> d.getNama_pohon_kinerja();
            default -> throw new IllegalArgumentException("DTO is not valid");
        };
    }

    private String getKeterangan(Object dto) {
        return switch (dto) {
            case TematikReqDTO d -> d.getKeterangan();
            case SubTematikReqDTO d -> d.getKeterangan();
            case SubSubTematikReqDTO d -> d.getKeterangan();
            case StrategicReqDTO d -> d.getKeterangan();
            case TacticalReqDTO d -> d.getKeterangan();
            case OperationalReqDTO d -> d.getKeterangan();
            default -> "-";
        };
    }

    private String extractParentIdForIndikator(Object dto, String jenisPokin) {
        return switch (jenisPokin.toLowerCase()) {
            case "tematik" -> ((IndikatorReqDTO.IndikatorTematik) dto).getId_tematik();
            case "sub-tematik" -> ((IndikatorReqDTO.IndikatorSubTematik) dto).getId_sub_tematik();
            case "sub-sub-tematik" -> ((IndikatorReqDTO.IndikatorSubSubTematik) dto).getId_sub_sub_tematik();
            case "strategic" -> ((IndikatorReqDTO.IndikatorStrategic) dto).getId_strategic();
            case "tactical" -> ((IndikatorReqDTO.IndikatorTactical) dto).getId_tactical();
            case "operational" -> ((IndikatorReqDTO.IndikatorOperational) dto).getId_operational();
            default -> null;
        };
    }
}
