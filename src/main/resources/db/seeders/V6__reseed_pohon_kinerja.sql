-- =============================================================
-- FILE: Seed_Pohon_Kinerja_Baru.sql
-- DESKRIPSI: Mengisi data hirarki Pohon Kinerja, Indikator, dan Target
-- ASUMSI: Tabel sudah kosong (Truncated) dan Sequence di-reset via Migration
-- =============================================================

-- ==========================================
-- 1. SEED POHON KINERJA (Hierarchy)
-- ==========================================

-- LEVEL 0: TEMATIK (Root) -> ID 1
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (1, NULL, 'Peningkatan Kualitas Pendidikan', 'TEMATIK', 0, '2025', 'Fokus pada peningkatan mutu pendidikan dasar hingga menengah', 'APPROVED');

-- LEVEL 1: SUB-TEMATIK (Child of 1) -> ID 2
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (2, 1, 'Contoh Mock Sub Tematik 1.1', 'SUB_TEMATIK', 1, '2025', 'Fokus sub tema', 'APPROVED');

-- LEVEL 2: SUB-SUB-TEMATIK (Children of 2) -> ID 3, 4
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES
    (3, 2, 'Contoh Mock Sub Sub Tematik 1.1.1', 'SUB_SUB_TEMATIK', 2, '2025', 'Fokus sub sub tema 1', 'APPROVED'),
    (4, 2, 'Contoh Mock Sub Sub Tematik 1.1.2', 'SUB_SUB_TEMATIK', 2, '2025', 'Sample keterangan kosong', 'APPROVED');

-- LEVEL 3: STRATEGIC (Child of 4) -> ID 5
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status, kode_opd)
VALUES (5, 4, 'Contoh Mock Strategic 1.1.2', 'STRATEGIC', 3, '2025', 'Sampel Keterangan buat Strategic', 'APPROVED', '1.01.01');

-- LEVEL 4: TACTICAL (Children of 5) -> ID 6, 7
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status, kode_opd)
VALUES
    (6, 5, 'Contoh Mock Tactical 1.2.1', 'TACTICAL', 4, '2025', 'Sampel Keterangan buat Tactical', 'APPROVED', '1.01.01'),
    (7, 5, 'Contoh Mock Tactical 1.2.2', 'TACTICAL', 4, '2025', 'Sampel Keterangan buat Tactical ke-2', 'APPROVED', '1.01.01');

-- LEVEL 5: OPERATIONAL (Children of 6) -> ID 8, 9
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status, kode_opd)
VALUES
    (8, 6, 'Contoh Mock Operational 1.1.2', 'OPERATIONAL', 5, '2025', 'Sampel Keterangan buat Operational', 'APPROVED', '1.01.01'),
    (9, 6, 'Contoh Mock Operational 1.1.3', 'OPERATIONAL', 5, '2025', 'Sampel Keterangan buat Operational ke-2', 'APPROVED', '1.01.01');


-- ==========================================
-- 2. SEED INDIKATOR
-- ==========================================

-- Indikator untuk Tematik (ID 1)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (1, 1, 'Persentase Penurunan Angka Kemiskinan', '2025', 'Indikator Utama');

-- Indikator untuk Sub Tematik (ID 2)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (2, 2, 'Persentase Penurunan Angka Pengangguran', '2025', 'Indikator Sub');

-- Indikator untuk Sub Sub Tematik 1 (ID 3)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (3, 3, 'Persentase Indikator Buat Sub Sub Tematik', '2025', '-');

-- Indikator untuk Sub Sub Tematik 2 (ID 4)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (4, 4, 'Indikator Buat Sub Tematik ke-2', '2025', '-');

-- Indikator untuk Strategic (ID 5)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (5, 5, 'Indikator Buat Strategic ke-1', '2025', '-');

-- Indikator untuk Tactical (ID 6)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (6, 6, 'Indikator Buat Tactical ke-1', '2025', '-');

-- Indikator untuk Operational 1 (ID 8)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (7, 8, 'Indikator Buat Operational ke-1', '2025', '-');

-- Indikator untuk Operational 2 (ID 9)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (8, 9, 'Indikator Buat Operational ke-2', '2025', '-');


-- ==========================================
-- 3. SEED TARGET
-- ==========================================

INSERT INTO target (indikator_id, nilai, satuan, tahun) VALUES
(1, 5, 'persen', '2025'),
(2, 9, 'persen', '2025'),
(3, 9, 'persen', '2025'),
(4, 9, 'persen', '2025'),
(5, 95, 'persen', '2025'),
(6, 75, 'persen', '2025'),
(7, 75, 'persen', '2025'),
(8, 75, 'persen', '2025');


-- ==========================================
-- 4. FINAL SEQUENCE UPDATE
-- Menyesuaikan internal counter PostgreSQL ke ID terakhir yang baru saja di-insert
-- ==========================================
SELECT setval(pg_get_serial_sequence('pohon_kinerja', 'id'), (SELECT MAX(id) FROM pohon_kinerja));
SELECT setval(pg_get_serial_sequence('indikator', 'id'), (SELECT MAX(id) FROM indikator));
SELECT setval(pg_get_serial_sequence('target', 'id'), (SELECT MAX(id) FROM target));