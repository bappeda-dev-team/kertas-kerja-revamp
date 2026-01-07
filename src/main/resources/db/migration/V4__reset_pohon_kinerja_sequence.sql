-- V4__reset_pohon_kinerja_sequence.sql

-- 1. Hapus semua data kotor/lama agar tidak konflik
TRUNCATE TABLE pohon_kinerja RESTART IDENTITY CASCADE;

-- 2. Paksa Sequence ID agar mulai dari 1 lagi
ALTER TABLE pohon_kinerja ALTER COLUMN id RESTART WITH 1;

-- Pastikan sequence anak-anaknya juga aman (opsional tapi bagus untuk safety)
SELECT setval(pg_get_serial_sequence('indikator', 'id'), 1, false);
SELECT setval(pg_get_serial_sequence('target', 'id'), 1, false);