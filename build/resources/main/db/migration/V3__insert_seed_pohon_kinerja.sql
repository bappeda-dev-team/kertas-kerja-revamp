
-- TEMATIK
INSERT INTO pohon_kinerja (id_tematik, nama_pohon_kinerja, jenis_pohon_kinerja, level_pohon_kinerja, keterangan,
                           jenis_child)
VALUES ('TEMA-2025-001', 'Peningkatan Kualitas Pendidikan', 'pemda', 1,
        'Fokus pada peningkatan mutu pendidikan dasar hingga menengah', 'sub-tematik');

-- SUB-TEMATIK
INSERT INTO pohon_kinerja (id_tematik, id_sub_tematik, nama_pohon_kinerja, jenis_pohon_kinerja, level_pohon_kinerja, keterangan,
                           jenis_child)
VALUES ('TEMA-2025-001',  'SUB-TEMA-2025-0011', 'Contoh Mock Sub Tematik 1.1', 'pemda', 2,
        'Fokus pada peningkatan mutu pendidikan dasar hingga menengah', 'sub-sub-tematik');

-- SUB-SUB-TEMATIK
INSERT INTO pohon_kinerja (id_sub_tematik, id_sub_sub_tematik, nama_pohon_kinerja, jenis_pohon_kinerja, level_pohon_kinerja, keterangan,
                           jenis_child)
VALUES ('SUB-TEMA-2025-0011',  'SUBSUB-TEMA-2025-00111', 'Contoh Mock Sub Sub Tematik 1.1.1', 'pemda', 3,
        'Fokus pada peningkatan mutu pendidikan dasar hingga menengah', 'strategic');
INSERT INTO pohon_kinerja (id_sub_tematik, id_sub_sub_tematik, nama_pohon_kinerja, jenis_pohon_kinerja, level_pohon_kinerja, keterangan,
                           jenis_child)
VALUES ('SUB-TEMA-2025-0011',  'SUBSUB-TEMA-2025-00112', 'Contoh Mock Sub Sub Tematik 1.1.2', 'pemda', 3,
        'Sample keterangan kosong', 'strategic');

-- STRATEGIC
INSERT INTO pohon_kinerja (id_sub_sub_tematik, id_strategic, nama_pohon_kinerja, jenis_pohon_kinerja, level_pohon_kinerja, keterangan,
                           jenis_child)
VALUES ('SUBSUB-TEMA-2025-00112',  'STRATEGIC-2025-1011', 'Contoh Mock Strategic 1.1.2', 'pemda', 4,
        'Sampel Keterangan buat Strategic', 'tactical');

-- TACTICAL
INSERT INTO pohon_kinerja (id_strategic, id_tactical, nama_pohon_kinerja, jenis_pohon_kinerja, level_pohon_kinerja, keterangan,
                           jenis_child)
VALUES ('STRATEGIC-2025-1011',  'TACTICAL-2025-2011', 'Contoh Mock Tactical 1.2.1', 'pemda', 5,
        'Sampel Keterangan buat Tactical', 'operational');

INSERT INTO pohon_kinerja (id_strategic, id_tactical, nama_pohon_kinerja, jenis_pohon_kinerja, level_pohon_kinerja, keterangan,
                           jenis_child)
VALUES ('STRATEGIC-2025-1011',  'TACTICAL-2025-2012', 'Contoh Mock Tactical 1.2.2', 'pemda', 5,
        'Sampel Keterangan buat Tactical ke-2', 'operational');

-- OPERATIONAL
INSERT INTO pohon_kinerja (id_tactical, id_operational, nama_pohon_kinerja, jenis_pohon_kinerja, level_pohon_kinerja, keterangan,
                           jenis_child)
VALUES ('TACTICAL-2025-2011',  'OPERATIONAL-2025-1234', 'Contoh Mock Operational 1.1.2', 'pemda', 6,
        'Sampel Keterangan buat Operational', '-');

INSERT INTO pohon_kinerja (id_tactical, id_operational, nama_pohon_kinerja, jenis_pohon_kinerja, level_pohon_kinerja, keterangan,
                           jenis_child)
VALUES ('TACTICAL-2025-2011',  'OPERATIONAL-2025-1235', 'Contoh Mock Operational 1.1.3', 'pemda', 6,
        'Sampel Keterangan buat Operational ke-2', '-');


-- INDIKATOR

INSERT INTO indikator_pohon_kinerja (id_tematik, indikator, tahun, target)
VALUES ('TEMA-2025-001', 'Persentase Penurunan Angka Kemiskinan', 2025, '[
  {
    "nilai": 5,
    "satuan": "persen",
    "keterangan": "Target penurunan sebesar 5% dibanding tahun sebelumnya"
  }
]');

INSERT INTO indikator_pohon_kinerja (id_sub_tematik, indikator, tahun, target)
VALUES ('SUB-TEMA-2025-0011',
        'Persentase Penurunan Angka Pengangguran',
        2025,
        '[
          {
            "nilai": 9,
            "satuan": "persen",
            "keterangan": "Target penurunan sebesar 5% dibanding tahun sebelumnya tambahan"
          }
        ]');

INSERT INTO indikator_pohon_kinerja (id_sub_sub_tematik, indikator, tahun, target)
VALUES ('SUBSUB-TEMA-2025-00111',
        'Persentase Indikator Buat Sub Sub Tematik',
        2025,
        '[
          {
            "nilai": 9,
            "satuan": "persen",
            "keterangan": "Tidak ada"
          }
        ]');

INSERT INTO indikator_pohon_kinerja (id_sub_sub_tematik, indikator, tahun, target)
VALUES ('SUBSUB-TEMA-2025-00112',
        'Indikator Buat Sub Tematik ke-2',
        2025,
        '[
          {
            "nilai": 9,
            "satuan": "persen",
            "keterangan": "Tidak ada"
          }
        ]');

INSERT INTO indikator_pohon_kinerja (id_strategic, indikator, tahun, target)
VALUES ('STRATEGIC-2025-1011',
        'Indikator Buat Strategic ke-1',
        2025,
        '[
          {
            "nilai": 95,
            "satuan": "persen",
            "keterangan": "Tidak ada"
          }
        ]');

INSERT INTO indikator_pohon_kinerja (id_tactical, indikator, tahun, target)
VALUES ('TACTICAL-2025-2011',
        'Indikator Buat Tactical ke-1',
        2025,
        '[
          {
            "nilai": 75,
            "satuan": "persen",
            "keterangan": "Tidak ada"
          }
        ]');

INSERT INTO indikator_pohon_kinerja (id_operational, indikator, tahun, target)
VALUES ('OPERATIONAL-2025-1234',
        'Indikator Buat Operational ke-1',
        2025,
        '[
          {
            "nilai": 75,
            "satuan": "persen",
            "keterangan": "Tidak ada"
          }
        ]');

INSERT INTO indikator_pohon_kinerja (id_operational, indikator, tahun, target)
VALUES ('OPERATIONAL-2025-1234',
        'Indikator Buat Operational ke-2',
        2025,
        '[
          {
            "nilai": 75,
            "satuan": "persen",
            "keterangan": "Tidak ada"
          }
        ]');


