-- =================================================================================
-- FLYWAY V6: SEED DATA KOMPLEKS - PARIWISATA & EKRAF (LANJUTAN ID DARI V5)
-- Skenario: Cross-Cutting (Lintas OPD: Dinas Pariwisata, Dinas PU, Dinas Koperasi)
-- =================================================================================

-- Asumsi V5 berakhir di ID 6. Kita mulai dari ID 7.

-- 1. LEVEL 0 (TEMATIK) - ROOT
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status, kode_pemda)
VALUES (7, NULL, 'Tematik: Pariwisata & Ekonomi Kreatif Berbasis Digital', 'TEMATIK', 0, 2025, 'Prioritas Unggulan Daerah', 'APPROVED', '35.15');

-- CABANG A: FOKUS DESTINASI (LEVEL 1)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (8, 7, 'Peningkatan Daya Tarik Destinasi Wisata', 'SUB_TEMATIK', 1, 2025, 'Sisi Supply Wisata', 'APPROVED');

-- CABANG A.1: INFRASTRUKTUR (LEVEL 2) -> AKAN DIKERJAKAN DINAS PU
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (9, 8, 'Pemenuhan Infrastruktur Dasar Kawasan Wisata', 'SUB_SUB_TEMATIK', 2, 2025, 'Akses Jalan & Air Bersih', 'APPROVED');

-- Level 4 (Strategic) - DINAS PU (1.03.01)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status, kode_opd)
VALUES (10, 9, 'Meningkatnya Kualitas Jalan Menuju Destinasi Unggulan', 'STRATEGIC_PEMDA', 4, 2025, 'Dinas Pekerjaan Umum', 'APPROVED', '1.03.01');

-- Level 5 (Tactical)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (11, 10, 'Peningkatan Jalan Kabupaten Ruas A-B', 'TACTICAL_PEMDA', 5, 2025, 'Bidang Bina Marga', 'APPROVED');

-- Level 6 (Operational)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (12, 11, 'Pengaspalan Hotmix Jalan Wisata Pantai X', 'OPERATIONAL_PEMDA', 6, 2025, 'Seksi Pembangunan Jalan', 'APPROVED');

-- CABANG A.2: PROMOSI & EVENT (LEVEL 2) -> AKAN DIKERJAKAN DINAS PARIWISATA
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (13, 8, 'Peningkatan Promosi dan Event Pariwisata', 'SUB_SUB_TEMATIK', 2, 2025, 'Branding & Marketing', 'APPROVED');

-- Level 4 (Strategic) - DINAS PARIWISATA (2.16.01)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status, kode_opd)
VALUES (14, 13, 'Meningkatnya Kunjungan Wisatawan Nusantara & Asing', 'STRATEGIC_PEMDA', 4, 2025, 'Dinas Pariwisata', 'APPROVED', '2.16.01');

-- Level 5 (Tactical)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (15, 14, 'Penyelenggaraan Festival Budaya Tahunan', 'TACTICAL_PEMDA', 5, 2025, 'Bidang Pemasaran', 'APPROVED');


-- CABANG B: FOKUS EKONOMI KREATIF (LEVEL 1)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (16, 7, 'Penguatan Ekosistem Ekonomi Kreatif', 'SUB_TEMATIK', 1, 2025, 'Sisi Pemberdayaan Masyarakat', 'APPROVED');

-- CABANG B.1: DIGITALISASI UMKM (LEVEL 2) -> AKAN DIKERJAKAN DINAS KOPERASI/UMKM
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status)
VALUES (17, 16, 'Transformasi Digital UMKM Ekraf', 'SUB_SUB_TEMATIK', 2, 2025, 'Onboarding Marketplace', 'APPROVED');

-- Level 4 (Strategic) - DINAS KOPERASI (2.15.01)
INSERT INTO pohon_kinerja (id, parent_id, nama_pohon, jenis_pohon, level_pohon, tahun, keterangan, status, kode_opd)
VALUES (18, 17, 'Meningkatnya Omzet UMKM Melalui Platform Digital', 'STRATEGIC_PEMDA', 4, 2025, 'Dinas Koperasi & UMKM', 'APPROVED', '2.15.01');


-- =================================================================================
-- 2. SEED DATA: INDIKATOR (LANJUTAN ID DARI V5)
-- Asumsi V5 Indikator berakhir di ID 4. Kita mulai dari 5.
-- =================================================================================

-- Indikator Root (ID 7)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, keterangan, tahun)
VALUES
    (5, 7, 'Kontribusi PDRB Sektor Pariwisata', 'Persentase terhadap total PDRB', 2025),
    (6, 7, 'Lama Tinggal Wisatawan (Length of Stay)', 'Rata-rata hari', 2025);

-- Indikator Dinas PU (ID 10)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, keterangan, tahun)
VALUES
    (7, 10, 'Persentase Jalan Mantap di Kawasan Strategis Pariwisata', 'Data Dinas PU', 2025);

-- Indikator Dinas Pariwisata (ID 14)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, keterangan, tahun)
VALUES
    (8, 14, 'Jumlah Kunjungan Wisatawan', 'Orang per tahun', 2025);

-- Indikator Dinas Koperasi (ID 18)
INSERT INTO indikator (id, pohon_kinerja_id, indikator, keterangan, tahun)
VALUES
    (9, 18, 'Persentase UMKM yang Onboarding Digital', 'Masuk Marketplace/E-Catalog', 2025);

-- =================================================================================
-- 3. SEED DATA: TARGET (LANJUTAN ID DARI V5)
-- Asumsi V5 Target berakhir di ID 3. Kita mulai dari 4.
-- =================================================================================

-- Target PDRB (Indikator 5)
INSERT INTO target (id, indikator_id, nilai, satuan, tahun) VALUES (4, 5, 8.5, 'Persen', 2025);

-- Target Lama Tinggal (Indikator 6)
INSERT INTO target (id, indikator_id, nilai, satuan, tahun) VALUES (5, 6, 2.5, 'Hari', 2025);

-- Target Jalan Mantap (Indikator 7)
INSERT INTO target (id, indikator_id, nilai, satuan, tahun) VALUES (6, 7, 95.0, 'Persen', 2025);

-- Target Kunjungan (Indikator 8)
INSERT INTO target (id, indikator_id, nilai, satuan, tahun) VALUES (7, 8, 150000, 'Orang', 2025);

-- Target UMKM Digital (Indikator 9)
INSERT INTO target (id, indikator_id, nilai, satuan, tahun) VALUES (8, 9, 40.0, 'Persen', 2025);


-- =================================================================================
-- 4. RESET SEQUENCES (SUPER PENTING)
-- Memastikan ID selanjutnya aman
-- =================================================================================

SELECT setval('pohon_kinerja_id_seq', (SELECT MAX(id) FROM pohon_kinerja));
SELECT setval('indikator_id_seq', (SELECT MAX(id) FROM indikator));
SELECT setval('target_id_seq', (SELECT MAX(id) FROM target));