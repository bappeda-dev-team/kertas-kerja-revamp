ALTER TABLE pohon_kinerja ALTER COLUMN tahun TYPE INTEGER USING tahun::integer;
ALTER TABLE indikator ALTER COLUMN tahun TYPE INTEGER USING tahun::integer;
ALTER TABLE target ALTER COLUMN tahun TYPE INTEGER USING tahun::integer;