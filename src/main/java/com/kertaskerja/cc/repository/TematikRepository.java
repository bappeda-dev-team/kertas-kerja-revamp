package com.kertaskerja.cc.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kertaskerja.cc.dto.TematikRequest;
import com.kertaskerja.cc.entity.Tematik;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TematikRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean existsByIdTematik(String idTematik) {
        Boolean exists = jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM tematik WHERE id_tematik = ?)",
                Boolean.class,
                idTematik
        );
        return exists != null && exists;
    }

    public boolean existsByNamaPohonKinerja(String namaPohonKinerja) {
        Boolean exists = jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT 1 FROM tematik WHERE nama_pohon_kinerja = ?)",
                Boolean.class,
                namaPohonKinerja
        );
        return exists != null && exists;
    }

    public void insert(TematikRequest request) {
        String sql = "INSERT INTO tematik (id_tematik, nama_pohon_kinerja, jenis_pohon_kinerja, level_pohon_kinerja, keterangan, jenis_child) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                request.getId_tematik(),
                request.getNama_pohon_kinerja(),
                request.getJenis_pohon_kinerja(),
                request.getLevel_pohon_kinerja(),
                request.getKeterangan(),
                request.getJenis_child()
        );
    }

    public Optional<Tematik> findByIdTematik(String idTematik) {
        String sql = "SELECT id, id_tematik as idTematik, nama_pohon_kinerja as namaPohonKinerja, jenis_pohon_kinerja as jenisPohonKinerja, level_pohon_kinerja as levelPohonKinerja, keterangan, jenis_child as jenisChild, created_at as createdAt, updated_at as updatedAt FROM tematik WHERE id_tematik = ?";
        return jdbcTemplate.query(sql,
                        new BeanPropertyRowMapper<>(Tematik.class),
                        idTematik)
                .stream().findFirst();
    }

    public List<Map<String, Object>> findAllTematikWithIndicators() {
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
                i.target::text as target
            FROM tematik t
            LEFT JOIN indikator i ON t.id_tematik = i.id_tematik 
            WHERE i.id_sub_tematik IS NULL OR i.id_sub_tematik IS NULL
            ORDER BY t.id, i.id
        """;
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, Object> row = new HashMap<>();
            row.put("id", rs.getLong("id"));
            row.put("id_tematik", rs.getString("id_tematik"));
            row.put("nama_pohon_kinerja", rs.getString("nama_pohon_kinerja"));
            row.put("jenis_pohon_kinerja", rs.getString("jenis_pohon_kinerja"));
            row.put("level_pohon_kinerja", rs.getString("level_pohon_kinerja"));
            row.put("keterangan", rs.getString("keterangan"));
            row.put("jenis_child", rs.getString("jenis_child"));
            row.put("indikator", rs.getString("indikator"));
            row.put("tahun", rs.getObject("tahun", Integer.class));
            String target = rs.getString("target");
            try {
                Object parsed = target == null ? null : objectMapper.readValue(target, Object.class);
                row.put("target", parsed);
            } catch (Exception e) {
                row.put("target", null);
            }
            return row;
        });
    }
}