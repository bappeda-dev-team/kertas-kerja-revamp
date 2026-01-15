package com.kertaskerja.id.KertasKerjaRevamp.repository;

import com.kertaskerja.id.KertasKerjaRevamp.model.Indikator;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IndikatorRepository {

    private final JdbcClient jdbcClient;

    private final RowMapper<Indikator> rowMapper = (rs, rowNum) -> Indikator.builder()
            .id(rs.getLong("id"))
            .pohonKinerjaId(rs.getLong("pohon_kinerja_id"))
            .indikator(rs.getString("indikator"))
            .keterangan(rs.getString("keterangan"))
            .tahun(rs.getInt("tahun"))
            .build();

    public List<Indikator> findAll() {
        return jdbcClient.sql("SELECT * FROM indikator")
                .query(rowMapper)
                .list();
    }

    public Optional<Indikator> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM indikator WHERE id = :id")
                .param("id", id)
                .query(rowMapper)
                .optional();
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM indikator WHERE id = :id";

        Integer count = jdbcClient.sql(sql)
                .param("id", id)
                .query(Integer.class)
                .single();

        return count > 0;
    }

    public List<Indikator> findByPohonKinerjaIdIn(List<Long> pohonIds) {
        if (pohonIds == null || pohonIds.isEmpty()) {
            return List.of();
        }

        String sql = "SELECT * FROM indikator WHERE pohon_kinerja_id IN (:ids)";

        return jdbcClient.sql(sql)
                .param("ids", pohonIds)
                .query(rowMapper)
                .list();
    }

    public List<Indikator> findByPohonKinerjaId(Long pohonKinerjaId) {
        return jdbcClient.sql("SELECT * FROM indikator WHERE pohon_kinerja_id = :pohonKinerjaId")
                .param("pohonKinerjaId", pohonKinerjaId)
                .query(rowMapper)
                .list();
    }

    @Transactional
    public Indikator save(Indikator indikator) {
        String sql = """
                INSERT INTO indikator (pohon_kinerja_id, indikator, keterangan, tahun)
                VALUES (:pohonKinerjaId, :indikator, :ket, :tahun)
                RETURNING *
                """;

        return jdbcClient.sql(sql)
                .param("pohonKinerjaId", indikator.getPohonKinerjaId())
                .param("indikator", indikator.getIndikator())
                .param("ket", indikator.getKeterangan())
                .param("tahun", indikator.getTahun())
                .query(rowMapper)
                .single();
    }

    @Transactional
    public void update(Indikator indikator) {
        String sql = """
                UPDATE indikator
                SET pohon_kinerja_id = :pohonKinerjaId,
                    indikator = :indikator,
                    keterangan = :ket,
                    tahun = :tahun
                WHERE id = :id
                """;

        int rows = jdbcClient.sql(sql)
                .param("pohonKinerjaId", indikator.getPohonKinerjaId())
                .param("indikator", indikator.getIndikator())
                .param("ket", indikator.getKeterangan())
                .param("tahun", indikator.getTahun())
                .param("id", indikator.getId())
                .update();

        if (rows == 0) {
            throw new RuntimeException("Gagal update Indikator ID " + indikator.getId() + " tidak ditemukan.");
        }
    }

    @Transactional
    public void delete(Long id) {
        jdbcClient.sql("DELETE FROM indikator WHERE id = :id")
                .param("id", id)
                .update();
    }

    @Transactional
    public void deleteById(Long id) {
        String sql = "DELETE FROM indikator WHERE id = :id";

        int rows = jdbcClient.sql(sql)
                .param("id", id)
                .update();

        if (rows == 0) {
            throw new RuntimeException("Gagal delete Indikator. ID " + id + " tidak ditemukan.");
        }
    }

    @Transactional
    public void deleteAll(List<Indikator> indikators) {
        if (indikators == null || indikators.isEmpty()) {
            return;
        }

        List<Long> ids = indikators.stream()
                .map(Indikator::getId)
                .toList();

        String sql = "DELETE FROM indikator WHERE id IN (:ids)";

        jdbcClient.sql(sql)
                .param("ids", ids)
                .update();
    }
}