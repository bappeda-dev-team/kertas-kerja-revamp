package com.kertaskerja.id.KertasKerjaRevamp.repository;

import com.kertaskerja.id.KertasKerjaRevamp.model.Target;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TargetRepository {

    private final JdbcClient jdbcClient;

    private final RowMapper<Target> rowMapper = (rs, rowNum) -> Target.builder()
            .id(rs.getLong("id"))
            .indikatorId(rs.getLong("indikator_id"))
            .nilai(rs.getDouble("nilai"))
            .satuan(rs.getString("satuan"))
            .tahun(rs.getInt("tahun"))
            .build();

    public Boolean existById(Long id){
        String sql = "SELECT count(*) FROM target WHERE id = :id";

        Integer count = jdbcClient.sql(sql)
                .param("id", id)
                .query(Integer.class)
                .single();

        return count > 0;
    }

    public List<Target> findAll() {
        return jdbcClient.sql("SELECT * FROM target")
                .query(rowMapper)
                .list();
    }

    public Optional<Target> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM target WHERE id = :id")
                .param("id", id)
                .query(rowMapper)
                .optional();
    }

    public List<Target> findByIndikatorIdIn(List<Long> indikatorIds) {
        if (indikatorIds == null || indikatorIds.isEmpty()) {
            return List.of();
        }

        String sql = "SELECT * FROM target WHERE indikator_id IN (:ids)";

        return jdbcClient.sql(sql)
                .param("ids", indikatorIds)
                .query(rowMapper)
                .list();
    }

    public List<Target> findByIndikatorId(Long indikatorId) {
        return jdbcClient.sql("SELECT * FROM target WHERE indikator_id = :indikatorId")
                .param("indikatorId", indikatorId)
                .query(rowMapper)
                .list();
    }

    @Transactional
    public Target save(Target target) {
        String sql = """
                INSERT INTO target (indikator_id, nilai, satuan, tahun)
                VALUES (:indikatorId, :nilai, :satuan, :tahun)
                RETURNING *
                """;

        return jdbcClient.sql(sql)
                .param("indikatorId", target.getIndikatorId())
                .param("nilai", target.getNilai())
                .param("satuan", target.getSatuan())
                .param("tahun", target.getTahun())
                .query(rowMapper)
                .single();
    }

    @Transactional
    public void update(Target target) {
        String sql = """
                UPDATE target
                SET indikator_id = :indikatorId,
                    nilai = :nilai,
                    satuan = :satuan,
                    tahun = :tahun
                WHERE id = :id
                """;

        int rows = jdbcClient.sql(sql)
                .param("indikatorId", target.getIndikatorId())
                .param("nilai", target.getNilai())
                .param("satuan", target.getSatuan())
                .param("tahun", target.getTahun())
                .param("id", target.getId())
                .update();

        if (rows == 0) {
            throw new RuntimeException("Gagal update Target ID " + target.getId() + " tidak ditemukan.");
        }
    }

    @Transactional
    public void delete(Long id) {
        jdbcClient.sql("DELETE FROM target WHERE id = :id")
                .param("id", id)
                .update();
    }
}