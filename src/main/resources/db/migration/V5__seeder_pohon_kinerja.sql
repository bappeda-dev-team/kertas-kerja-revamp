-- =================================================================================
-- FLYWAY V5: SEED DATA POHON KINERJA (URUT DARI 1)
-- =================================================================================

-- 1. POHON KINERJA (Dimulai dari ID 1)
-- Level 0 (Tematik)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status, kode_opd, kode_pemda)
VALUES (1, NULL, 'Tematik: Pengentasan Kemiskinan & Stunting', 'TEMATIK', 0, 2025, 'Fokus Utama RPJMD', 'APPROVED', NULL, '35.15');

-- Level 1 (Sub Tematik) -> Parent: 1
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (2, 1, 'Peningkatan Kualitas Kesehatan Ibu & Anak', 'SUB_TEMATIK', 1, 2025, 'Intervensi Spesifik', 'APPROVED');

-- Level 2 (Sub Sub Tematik) -> Parent: 2
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (3, 2, 'Pemenuhan Gizi 1000 Hari Pertama Kehidupan', 'SUB_SUB_TEMATIK', 2, 2025, 'Fokus Baduta & Bumil', 'APPROVED');

-- Level 4 (Strategic Pemda) -> Parent: 3 (Loncatan Level 2 ke 4)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status, kode_opd)
VALUES (4, 3, 'Meningkatnya Status Gizi Masyarakat', 'STRATEGIC_PEMDA', 4, 2025, 'Dinas Kesehatan', 'APPROVED', '1.02.01');

-- Level 5 (Tactical Pemda) -> Parent: 4
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (5, 4, 'Peningkatan Cakupan Pelayanan Kesehatan Balita', 'TACTICAL_PEMDA', 5, 2025, 'Bidang Kesmas', 'APPROVED');

-- Level 6 (Operational Pemda) -> Parent: 5
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (6, 5, 'Pelaksanaan Bulan Timbang & PMT', 'OPERATIONAL_PEMDA', 6, 2025, 'Seksi Gizi', 'APPROVED');


-- 2. INDIKATOR (Dimulai dari ID 1)
-- Indikator milik Pohon ID 1
INSERT INTO indikator (id, pohon_kinerja_id, indikator, keterangan, tahun)
VALUES
    (1, 1, 'Persentase Angka Kemiskinan', 'Data BPS', 2025),
    (2, 1, 'Prevalensi Stunting', 'Data SSGI', 2025);

-- Indikator milik Pohon ID 4
INSERT INTO indikator (id, pohon_kinerja_id, indikator, keterangan, tahun)
VALUES
    (3, 4, 'Persentase Balita Gizi Buruk yang Mendapat Perawatan', 'Laporan Dinas', 2025);

-- Indikator milik Pohon ID 6
INSERT INTO indikator (id, pohon_kinerja_id, indikator, keterangan, tahun)
VALUES
    (4, 6, 'Jumlah Balita yang Mengikuti Bulan Timbang', 'Laporan Posyandu', 2025);


-- 3. TARGET (Dimulai dari ID 1)
-- Target milik Indikator ID 2
INSERT INTO target (id, indikator_id, nilai, satuan, tahun)
VALUES
    (1, 2, 14.00, 'Persen', 2025),
    (2, 2, 12.50, 'Persen', 2026);

-- Target milik Indikator ID 4
INSERT INTO target (id, indikator_id, nilai, satuan, tahun)
VALUES
    (3, 4, 1500.00, 'Anak', 2025);


-- 4. RESET SEQUENCE (WAJIB)
-- Agar insert data baru selanjutnya otomatis mendapat ID 7, 5, dan 4.
SELECT setval('pohon_kinerja_id_seq', (SELECT MAX(id) FROM pohon_kinerja));
SELECT setval('indikator_id_seq', (SELECT MAX(id) FROM indikator));
SELECT setval('target_id_seq', (SELECT MAX(id) FROM target));