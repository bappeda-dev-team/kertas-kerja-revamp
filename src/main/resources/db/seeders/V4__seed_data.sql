-- ==========================================
-- SEEDER POHON KINERJA (Hierarchy)
-- Start ID: 1000
-- ==========================================

-- 1. LEVEL 0: TEMATIK (Root)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (1000, NULL, 'Peningkatan Kualitas Pendidikan', 'TEMATIK', 0, '2025', 'Fokus pada peningkatan mutu pendidikan dasar hingga menengah', 'APPROVED');

-- 2. LEVEL 1: SUB-TEMATIK (Child of 1000)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (1001, 1000, 'Contoh Mock Sub Tematik 1.1', 'SUB_TEMATIK', 1, '2025', 'Fokus sub tema', 'APPROVED');

-- 3. LEVEL 2: SUB-SUB-TEMATIK (Children of 1001)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES
    (1002, 1001, 'Contoh Mock Sub Sub Tematik 1.1.1', 'SUB_SUB_TEMATIK', 2, '2025', 'Fokus sub sub tema 1', 'APPROVED'),
    (1003, 1001, 'Contoh Mock Sub Sub Tematik 1.1.2', 'SUB_SUB_TEMATIK', 2, '2025', 'Sample keterangan kosong', 'APPROVED');

-- 4. LEVEL 3: STRATEGIC (Child of 1003)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status, kode_opd)
VALUES (1004, 1003, 'Contoh Mock Strategic 1.1.2', 'STRATEGIC', 3, '2025', 'Sampel Keterangan buat Strategic', 'APPROVED', '1.01.01');

-- 5. LEVEL 4: TACTICAL (Children of 1004)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status, kode_opd)
VALUES
    (1005, 1004, 'Contoh Mock Tactical 1.2.1', 'TACTICAL', 4, '2025', 'Sampel Keterangan buat Tactical', 'APPROVED', '1.01.01'),
    (1006, 1004, 'Contoh Mock Tactical 1.2.2', 'TACTICAL', 4, '2025', 'Sampel Keterangan buat Tactical ke-2', 'APPROVED', '1.01.01');

-- 6. LEVEL 5: OPERATIONAL (Children of 1005)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status, kode_opd)
VALUES
    (1007, 1005, 'Contoh Mock Operational 1.1.2', 'OPERATIONAL', 5, '2025', 'Sampel Keterangan buat Operational', 'APPROVED', '1.01.01'),
    (1008, 1005, 'Contoh Mock Operational 1.1.3', 'OPERATIONAL', 5, '2025', 'Sampel Keterangan buat Operational ke-2', 'APPROVED', '1.01.01');


-- ==========================================
-- SEEDER INDIKATOR & TARGET
-- ==========================================

-- Indikator untuk Tematik (ID 1000)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (1, 1000, 'Persentase Penurunan Angka Kemiskinan', '2025', 'Indikator Utama');

INSERT INTO target (indikator_id, nilai, satuan, tahun)
VALUES (1, 5, 'persen', '2025');


-- Indikator untuk Sub Tematik (ID 1001)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (2, 1001, 'Persentase Penurunan Angka Pengangguran', '2025', 'Indikator Sub');

INSERT INTO target (indikator_id, nilai, satuan, tahun)
VALUES (2, 9, 'persen', '2025');


-- Indikator untuk Sub Sub Tematik 1 (ID 1002)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (3, 1002, 'Persentase Indikator Buat Sub Sub Tematik', '2025', '-');

INSERT INTO target (indikator_id, nilai, satuan, tahun)
VALUES (3, 9, 'persen', '2025');


-- Indikator untuk Sub Sub Tematik 2 (ID 1003)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (4, 1003, 'Indikator Buat Sub Tematik ke-2', '2025', '-');

INSERT INTO target (indikator_id, nilai, satuan, tahun)
VALUES (4, 9, 'persen', '2025');


-- Indikator untuk Strategic (ID 1004)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (5, 1004, 'Indikator Buat Strategic ke-1', '2025', '-');

INSERT INTO target (indikator_id, nilai, satuan, tahun)
VALUES (5, 95, 'persen', '2025');


-- Indikator untuk Tactical (ID 1005)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (6, 1005, 'Indikator Buat Tactical ke-1', '2025', '-');

INSERT INTO target (indikator_id, nilai, satuan, tahun)
VALUES (6, 75, 'persen', '2025');


-- Indikator untuk Operational 1 (ID 1007)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (7, 1007, 'Indikator Buat Operational ke-1', '2025', '-');

INSERT INTO target (indikator_id, nilai, satuan, tahun)
VALUES (7, 75, 'persen', '2025');


-- Indikator untuk Operational 2 (ID 1008)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, tahun, keterangan)
VALUES (8, 1008, 'Indikator Buat Operational ke-2', '2025', '-');

INSERT INTO target (indikator_id, nilai, satuan, tahun)
VALUES (8, 75, 'persen', '2025');


-- ==========================================
-- RESET SEQUENCE (PENTING!)
-- Agar insert data baru nanti ID-nya lanjut dari angka terakhir
-- ==========================================
SELECT setval('pohon_kinerja_id_seq', (SELECT MAX(id) FROM pohon_kinerja));
SELECT setval('indikator_id_seq', (SELECT MAX(id) FROM indikator));
SELECT setval('target_id_seq', (SELECT MAX(id) FROM target));