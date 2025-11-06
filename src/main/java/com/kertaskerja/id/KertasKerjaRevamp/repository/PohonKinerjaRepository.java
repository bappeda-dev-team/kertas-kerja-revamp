package com.kertaskerja.id.KertasKerjaRevamp.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PohonKinerjaRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public boolean existsByParentId(String idColumn, String idValue) {
        String sql = String.format(
              "SELECT COUNT(*) FROM pohon_kinerja WHERE %s = ?",
              idColumn
        );

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idValue);
        return count != null && count > 0;
    }

    public boolean existsByNamaPohonKinerja(String namaPohonKinerja) {
        Boolean exists = jdbcTemplate.queryForObject(
              "SELECT EXISTS (SELECT 1 FROM pohon_kinerja WHERE nama_pohon_kinerja = ?)",
              Boolean.class,
              namaPohonKinerja
        );
        return exists != null && exists;
    }

    public List<Map<String, Object>> findOneTematik(String idTematik) {
        String sql = """
                  SELECT
                                         t.id,
                                         t.id_tematik,
                                         t.nama_pohon_kinerja,
                                         t.jenis_pohon_kinerja,
                                         t.level_pohon_kinerja,
                                         t.keterangan,
                                         t.jenis_child,
                                         i.indikator,
                                         i.tahun,
                                         i.target::text AS target
                                     FROM pohon_kinerja t
                                     LEFT JOIN indikator_pohon_kinerja i
                                         ON t.id_tematik = i.id_tematik
                                         AND i.id_sub_tematik IS NULL
                                         AND i.id_sub_sub_tematik IS NULL
                                         AND i.id_strategic IS NULL
                                         AND i.id_tactical IS NULL
                                         AND i.id_operational IS NULL
                                     WHERE t.id_tematik = ?
                                       AND t.level_pohon_kinerja = 1  
                                     ORDER BY t.id, i.id;
              """;

        return jdbcTemplate.query(sql, new Object[]{idTematik}, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id_tematik", rs.getString("id_tematik"));
            row.put("nama_pohon_kinerja", rs.getString("nama_pohon_kinerja"));
            row.put("jenis_pohon_kinerja", rs.getString("jenis_pohon_kinerja"));
            row.put("level_pohon_kinerja", rs.getInt("level_pohon_kinerja"));
            row.put("keterangan", rs.getString("keterangan"));
            row.put("jenis_child", rs.getString("jenis_child"));
            row.put("indikator", rs.getString("indikator"));
            row.put("tahun", rs.getObject("tahun", Integer.class));
            row.put("target", rs.getString("target"));
            return row;
        });
    }

    public List<Map<String, Object>> findAllSubTematikByTematik(String idTematik) {
        String sql = """
                  SELECT 
                      st.id_tematik,
                      st.id_sub_tematik,
                      st.nama_pohon_kinerja,
                      st.jenis_pohon_kinerja,
                      st.level_pohon_kinerja,
                      st.keterangan,
                      st.jenis_child,
                      i.indikator,
                      i.tahun,
                      i.target::text AS target
                  FROM pohon_kinerja st
                  LEFT JOIN indikator_pohon_kinerja i
                      ON st.id_sub_tematik = i.id_sub_tematik
                      AND i.id_sub_sub_tematik IS NULL
                      AND i.id_strategic IS NULL
                      AND i.id_tactical IS NULL
                      AND i.id_operational IS NULL
                  WHERE st.id_tematik = ?
                    AND st.level_pohon_kinerja = 2
                  ORDER BY st.id, i.id;
              """;

        return jdbcTemplate.query(sql, new Object[]{idTematik}, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id_tematik", rs.getString("id_tematik"));
            row.put("id_sub_tematik", rs.getString("id_sub_tematik"));
            row.put("nama_pohon_kinerja", rs.getString("nama_pohon_kinerja"));
            row.put("jenis_pohon_kinerja", rs.getString("jenis_pohon_kinerja"));
            row.put("level_pohon_kinerja", rs.getInt("level_pohon_kinerja"));
            row.put("keterangan", rs.getString("keterangan"));
            row.put("jenis_child", rs.getString("jenis_child"));
            row.put("indikator", rs.getString("indikator"));
            row.put("tahun", rs.getObject("tahun", Integer.class));
            row.put("target", rs.getString("target"));
            return row;
        });
    }

    public List<Map<String, Object>> findAllSubSubTematikBySubTematik(String idSubTematik) {
        String sql = """
                SELECT
                    sst.id_sub_tematik,
                    sst.id_sub_sub_tematik,
                    sst.nama_pohon_kinerja,
                    sst.jenis_pohon_kinerja,
                    sst.level_pohon_kinerja,
                    sst.keterangan,
                    sst.jenis_child,
                    i.indikator,
                    i.tahun,
                    i.target::text AS target
                FROM pohon_kinerja sst
                LEFT JOIN indikator_pohon_kinerja i
                    ON sst.id_sub_sub_tematik = i.id_sub_sub_tematik
                    AND i.id_tematik IS NULL
                    AND i.id_sub_tematik IS NULL
                    AND i.id_strategic IS NULL
                    AND i.id_tactical IS NULL
                    AND i.id_operational IS NULL
                WHERE sst.id_sub_tematik = ?
                  AND sst.level_pohon_kinerja = 3
                ORDER BY sst.id, i.id;            
              """;

        return jdbcTemplate.query(sql, new Object[]{idSubTematik}, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id_sub_tematik", rs.getString("id_sub_tematik"));
            row.put("id_sub_sub_tematik", rs.getString("id_sub_sub_tematik"));
            row.put("nama_pohon_kinerja", rs.getString("nama_pohon_kinerja"));
            row.put("jenis_pohon_kinerja", rs.getString("jenis_pohon_kinerja"));
            row.put("level_pohon_kinerja", rs.getInt("level_pohon_kinerja"));
            row.put("keterangan", rs.getString("keterangan"));
            row.put("jenis_child", rs.getString("jenis_child"));
            row.put("indikator", rs.getString("indikator"));
            row.put("tahun", rs.getObject("tahun", Integer.class));
            row.put("target", rs.getString("target"));
            return row;
        });
    }

    public List<Map<String, Object>> findAllStrategicBySubSubTematik(String idSubSubTematik) {
        String sql = """
                SELECT 
                  s.id_sub_sub_tematik,
                  s.id_strategic,
                   s.nama_pohon_kinerja,
                    s.jenis_pohon_kinerja,
                    s.level_pohon_kinerja,
                    s.keterangan,
                    s.jenis_child,
                    i.indikator,
                    i.tahun,
                    i.target::text AS target
                    FROM pohon_kinerja s
                    LEFT JOIN indikator_pohon_kinerja i
                    ON s.id_strategic = i.id_strategic
                    AND i.id_tematik IS NULL
                    AND i.id_sub_tematik IS NULL
                    AND i.id_tactical IS NULL
                    AND i.id_operational IS NULL
                    WHERE s.id_sub_sub_tematik = ?
                    AND s.level_pohon_kinerja = 4
                    ORDER BY s.id, i.id;
              """;

        return jdbcTemplate.query(sql, new Object[]{idSubSubTematik}, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id_sub_sub_tematik", rs.getString("id_sub_sub_tematik"));
            row.put("id_strategic", rs.getString("id_strategic"));
            row.put("nama_pohon_kinerja", rs.getString("nama_pohon_kinerja"));
            row.put("jenis_pohon_kinerja", rs.getString("jenis_pohon_kinerja"));
            row.put("level_pohon_kinerja", rs.getInt("level_pohon_kinerja"));
            row.put("keterangan", rs.getString("keterangan"));
            row.put("jenis_child", rs.getString("jenis_child"));
            row.put("indikator", rs.getString("indikator"));
            row.put("tahun", rs.getObject("tahun", Integer.class));
            row.put("target", rs.getString("target"));
            return row;
        });
    }

    public List<Map<String, Object>> findAllTacticalByStrategic(String idStrategic) {
        String sql = """
              SELECT 
                  t.id_strategic,
                  t.id_tactical,
                  t.nama_pohon_kinerja,
                  t.jenis_pohon_kinerja,
                  t.level_pohon_kinerja,
                  t.keterangan,
                  t.jenis_child,
                  i.indikator,
                  i.tahun,
                  i.target::text AS target
              FROM pohon_kinerja t
              LEFT JOIN indikator_pohon_kinerja i
                  ON t.id_tactical = i.id_tactical
                  AND i.id_tematik IS NULL
                  AND i.id_sub_tematik IS NULL
                  AND i.id_sub_sub_tematik IS NULL
                  AND i.id_strategic IS NULL
                  AND i.id_operational IS NULL
              WHERE t.id_strategic = ?
                AND t.level_pohon_kinerja = 5
              ORDER BY t.id, i.id;
              """;

        return jdbcTemplate.query(sql, new Object[]{idStrategic}, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id_strategic", rs.getString("id_strategic"));
            row.put("id_tactical", rs.getString("id_tactical"));
            row.put("nama_pohon_kinerja", rs.getString("nama_pohon_kinerja"));
            row.put("jenis_pohon_kinerja", rs.getString("jenis_pohon_kinerja"));
            row.put("level_pohon_kinerja", rs.getInt("level_pohon_kinerja"));
            row.put("keterangan", rs.getString("keterangan"));
            row.put("jenis_child", rs.getString("jenis_child"));
            row.put("indikator", rs.getString("indikator"));
            row.put("tahun", rs.getObject("tahun", Integer.class));
            row.put("target", rs.getString("target"));
            return row;
        });
    }

    public List<Map<String, Object>> findAllOperationalByTactical(String idTactical) {
        String sql = """
              SELECT 
                  o.id_tactical,
                  o.id_operational,
                  o.nama_pohon_kinerja,
                  o.jenis_pohon_kinerja,
                  o.level_pohon_kinerja,
                  o.keterangan,
                  o.jenis_child,
                  i.indikator,
                  i.tahun,
                  i.target::text AS target
              FROM pohon_kinerja o
              LEFT JOIN indikator_pohon_kinerja i
                  ON o.id_operational = i.id_operational
                  AND i.id_tematik IS NULL
                  AND i.id_sub_tematik IS NULL
                  AND i.id_sub_sub_tematik IS NULL
                  AND i.id_strategic IS NULL
                  AND i.id_tactical IS NULL
              WHERE o.id_tactical = ?
                AND o.level_pohon_kinerja = 6
              ORDER BY o.id, i.id;
              """;

        return jdbcTemplate.query(sql, new Object[]{idTactical}, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id_tactical", rs.getString("id_tactical"));
            row.put("id_operational", rs.getString("id_operational"));
            row.put("nama_pohon_kinerja", rs.getString("nama_pohon_kinerja"));
            row.put("jenis_pohon_kinerja", rs.getString("jenis_pohon_kinerja"));
            row.put("level_pohon_kinerja", rs.getInt("level_pohon_kinerja"));
            row.put("keterangan", rs.getString("keterangan"));
            row.put("jenis_child", rs.getString("jenis_child"));
            row.put("indikator", rs.getString("indikator"));
            row.put("tahun", rs.getObject("tahun", Integer.class));
            row.put("target", rs.getString("target"));
            return row;
        });
    }

    public Map<String, Object> insert(
          String idTematik,
          String idSubTematik,
          String idSubSubTematik,
          String idStrategic,
          String idTactical,
          String idOperational,
          String namaPohonKinerja,
          String jenisPohonKinerja,
          Integer levelPohonKinerja,
          String keterangan,
          String jenisChild
    ) {
        String sql = """
                  INSERT INTO pohon_kinerja (
                      id_tematik,
                      id_sub_tematik,
                      id_sub_sub_tematik,
                      id_strategic,
                      id_tactical,
                      id_operational,
                      nama_pohon_kinerja,
                      jenis_pohon_kinerja,
                      level_pohon_kinerja,
                      keterangan,
                      jenis_child
                  ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                  RETURNING *;
              """;

        return jdbcTemplate.queryForMap(
              sql,
              idTematik,
              idSubTematik,
              idSubSubTematik,
              idStrategic,
              idTactical,
              idOperational,
              namaPohonKinerja,
              jenisPohonKinerja,
              levelPohonKinerja,
              keterangan,
              jenisChild
        );
    }

    public int update(String prefix, String idPokin, String nama, String keterangan) {
        String column;
        switch (prefix) {
            case "TMK" -> column = "id_tematik";
            case "STMK" -> column = "id_sub_tematik";
            case "SSTMK" -> column = "id_sub_sub_tematik";
            case "STR" -> column = "id_strategic";
            case "TACT" -> column = "id_tactical";
            case "OPS" -> column = "id_operational";
            default -> throw new IllegalArgumentException("Invalid prefix: " + prefix);
        }

        String sql = String.format("""
                  UPDATE pohon_kinerja
                     SET nama_pohon_kinerja = ?,
                         keterangan = ?,
                         updated_at = NOW()
                   WHERE %s = ?
              """, column);

        return jdbcTemplate.update(sql, nama, keterangan, idPokin);
    }

    public int delete(String idColumn, String idValue) {
        String sql = String.format("DELETE FROM pohon_kinerja WHERE %s = ?", idColumn);
        return jdbcTemplate.update(sql, idValue);
    }

    public int deleteIndikatorByPokin(String idColumn, String idValue) {
        String sql = String.format("DELETE FROM indikator_pohon_kinerja WHERE %s = ?", idColumn);
        return jdbcTemplate.update(sql, idValue);
    }

    public boolean existsParentForIndikator(String jenisPokin, String parentId) {
        if (parentId == null || parentId.isEmpty()) return false;

        String column = switch (jenisPokin.toLowerCase()) {
            case "tematik" -> "id_tematik";
            case "sub-tematik" -> "id_sub_tematik";
            case "sub-sub-tematik" -> "id_sub_sub_tematik";
            case "strategic" -> "id_strategic";
            case "tactical" -> "id_tactical";
            case "operational" -> "id_operational";
            default -> throw new IllegalArgumentException("Jenis Pokin tidak valid: " + jenisPokin);
        };

        String sql = String.format("SELECT COUNT(*) FROM pohon_kinerja WHERE %s = ?", column);
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, parentId);
            return count != null && count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public void insertIndikator(String jenisPokin, String parentId, String indikator, Integer tahun, Object targetList) {
        String idColumn = switch (jenisPokin.toLowerCase()) {
            case "tematik" -> "id_tematik";
            case "sub-tematik" -> "id_sub_tematik";
            case "sub-sub-tematik" -> "id_sub_sub_tematik";
            case "strategic" -> "id_strategic";
            case "tactical" -> "id_tactical";
            case "operational" -> "id_operational";
            default -> throw new IllegalArgumentException("Jenis Pokin tidak valid: " + jenisPokin);
        };

        String sql = String.format("""
                  INSERT INTO indikator_pohon_kinerja (%s, indikator, tahun, target)
                  VALUES (?, ?, ?, ?::jsonb)
              """, idColumn);

        try {
            String json = objectMapper.writeValueAsString(targetList);

            jdbcTemplate.update(sql, parentId, indikator, tahun, json);
        } catch (Exception e) {
            throw new RuntimeException("Gagal menyimpan indikator", e);
        }
    }
}