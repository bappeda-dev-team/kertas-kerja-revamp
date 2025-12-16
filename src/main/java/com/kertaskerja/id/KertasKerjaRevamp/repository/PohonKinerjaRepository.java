package com.kertaskerja.id.KertasKerjaRevamp.repository;

import com.kertaskerja.id.KertasKerjaRevamp.enums.JenisPohon;
import com.kertaskerja.id.KertasKerjaRevamp.model.PohonKinerja;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PohonKinerjaRepository {

    private final JdbcClient jdbcClient;

    private final RowMapper<PohonKinerja> pohonRowMapper = (rs, rowNum) -> {
        PohonKinerja p = new PohonKinerja();
        p.setId(rs.getLong("id"));
        long parentIdVal = rs.getLong("parent_id");
        if (rs.wasNull()) p.setParentId(null);
        else p.setParentId(parentIdVal);
        p.setNamaPohon(rs.getString("nama_pohon"));
        p.setKeterangan(rs.getString("keterangan"));
        p.setTahun(rs.getInt("tahun"));
        String jenisString = rs.getString("jenis_pohon");
        if (jenisString != null) p.setJenisPohon(JenisPohon.valueOf(jenisString));
        p.setLevelPohon(rs.getInt("level_pohon"));
        p.setKodeOpd(rs.getString("kode_opd"));
        p.setKodePemda(rs.getString("kode_pemda"));
        p.setStatus(rs.getString("status"));
        return p;
    };

    public boolean existsById(Long id) {
        String sql = "SELECT count(*) FROM pohon_kinerja WHERE id = :id";

        Integer count = jdbcClient.sql(sql).param("id", id).query(Integer.class).single();

        return count > 0;
    }

    public List<PohonKinerja> findTreeNodes(Long rootId) {
        String sql = """
                    WITH RECURSIVE hierarchy AS (
                        SELECT * FROM pohon_kinerja WHERE id = :rootId
                        UNION ALL
                        SELECT p.* FROM pohon_kinerja p
                        INNER JOIN hierarchy h ON p.parent_id = h.id
                    )
                    SELECT * FROM hierarchy
                """;
        return jdbcClient.sql(sql).param("rootId", rootId).query(pohonRowMapper).list();
    }

    @Transactional
    public PohonKinerja save(PohonKinerja pohon) {
        String sql = """
                INSERT INTO pohon_kinerja 
                (parent_id, nama_pohon, keterangan, tahun, jenis_pohon, level_pohon, kode_opd, kode_pemda, status)
                VALUES 
                (:parentId, :nama, :ket, :tahun, :jenis, :level, :opd, :pemda, :status)
                RETURNING *
                """;
        return jdbcClient.sql(sql).param("parentId", pohon.getParentId()).param("nama", pohon.getNamaPohon()).param("ket", pohon.getKeterangan()).param("tahun", pohon.getTahun()).param("jenis", pohon.getJenisPohon().name()).param("level", pohon.getLevelPohon()).param("opd", pohon.getKodeOpd()).param("pemda", pohon.getKodePemda()).param("status", "DRAFT").query(pohonRowMapper).single();
    }

    public Optional<PohonKinerja> findById(Long id) {
        String sql = "SELECT * FROM pohon_kinerja WHERE id = :id";
        return jdbcClient.sql(sql).param("id", id).query(pohonRowMapper).optional();
    }

    public List<PohonKinerja> findAllTematik() {
        String sql = "SELECT * FROM pohon_kinerja WHERE jenis_pohon = 'TEMATIK' ORDER BY id";
        return jdbcClient.sql(sql).query(pohonRowMapper).list();
    }

    public List<PohonKinerja> findByParentId(Long parentId) {
        String sql = "SELECT * FROM pohon_kinerja WHERE parent_id = :parentId";
        return jdbcClient.sql(sql)
                .param("parentId", parentId)
                .query(pohonRowMapper)
                .list();
    }

    @Transactional
    public void update(PohonKinerja pohon) {
        String sql = """
                UPDATE pohon_kinerja
                SET parent_id = :parentId,
                    nama_pohon = :nama,
                    keterangan = :ket,
                    tahun = :tahun,
                    jenis_pohon = :jenis,
                    level_pohon = :level,
                    kode_opd = :opd,
                    kode_pemda = :pemda,
                    status = :status
                WHERE id = :id
                """;
        int rows = jdbcClient.sql(sql).param("parentId", pohon.getParentId()).param("nama", pohon.getNamaPohon()).param("ket", pohon.getKeterangan()).param("tahun", pohon.getTahun()).param("jenis", pohon.getJenisPohon().name()).param("level", pohon.getLevelPohon()).param("opd", pohon.getKodeOpd()).param("pemda", pohon.getKodePemda()).param("status", pohon.getStatus()).param("id", pohon.getId()).update();
        if (rows == 0) throw new RuntimeException("Gagal update ID " + pohon.getId());
    }

    @Transactional
    public void deleteById(Long id) {
        String sql = "DELETE FROM pohon_kinerja WHERE id = :id";
        jdbcClient.sql(sql).param("id", id).update();
    }
}