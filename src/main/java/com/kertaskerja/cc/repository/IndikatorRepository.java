package com.kertaskerja.cc.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class IndikatorRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void insert(String idTematik,
                       String idSubTematikOrNull,
                       String indikator,
                       Integer tahun,
                       String targetJson) {
        String sql = "INSERT INTO indikator (id_tematik, id_sub_tematik, indikator, tahun, target) VALUES (?,?,?,?,CAST(? AS JSONB))";
        jdbcTemplate.update(sql,
                idTematik,
                idSubTematikOrNull,
                indikator,
                tahun,
                targetJson
        );
    }

    public List<Map<String, Object>> findAllForExample() {
        String sql = "SELECT id_tematik, id_sub_tematik, indikator, tahun, target::text as target FROM indikator ORDER BY id ASC";
        return jdbcTemplate.query(sql, indikatorRowMapper);
    }

    private final RowMapper<Map<String, Object>> indikatorRowMapper = (rs, rowNum) -> {
        Map<String, Object> m = new HashMap<>();
        m.put("id_tematik", rs.getString("id_tematik"));
        String idSub = rs.getString("id_sub_tematik");
        m.put("id_sub_tematik", idSub == null ? "" : idSub);
        m.put("indikator", rs.getString("indikator"));
        m.put("tahun", rs.getInt("tahun"));
        String target = rs.getString("target");
        try {
            Object parsed = target == null ? null : objectMapper.readValue(target, Object.class);
            m.put("target", parsed);
        } catch (Exception e) {
            m.put("target", null);
        }
        return m;
    };
}
