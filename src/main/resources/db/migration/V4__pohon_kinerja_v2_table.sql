    CREATE TABLE IF NOT EXISTS pohon_kinerja_v2
    (
        id BIGSERIAL PRIMARY KEY,
        id_parent VARCHAR(255),
        nama_pohon VARCHAR(255) NOT NULL,
        tipe_pohon VARCHAR(255) NOT NULL,
        jenis_pohon VARCHAR(255) NOT NULL,
        level_pohon INTEGER NOT NULL,
        indikator JSONB,
        tahun INTEGER NOT NULL,
        keterangan VARCHAR(255),
        kode_opd VARCHAR(255),
        status VARCHAR(255) NOT NULL,
        tagging JSONB,
        created_at TIMESTAMP NOT NULL,
        updated_at TIMESTAMP NOT NULL
    )


    -- https://api-ekak.zeabur.app/pohon_kinerja_opd/create
-- {
--     "nama_pohon": "tes tactical baru",
--     "Keterangan": "tidak ada",
--     "jenis_pohon": "Tactical",
--     "level_pohon": 5,
--     "parent": 9346,
--     "tahun": "2025",
--     "kode_opd": "5.01.5.05.0.00.01.0000",
--     "indikator": [
--         {
--             "indikator": "andaikan bumi berputar",
--             "target": [
--                 {
--                     "target": "100",
--                     "satuan": "persen"
--                 }
--             ]
--         },
--         {
--             "indikator": "cuma",
--             "target": [
--                 {
--                     "target": "90",
--                     "satuan": "dokumen"
--                 }
--             ]
--         }
--     ],
--     "tagging": [
--         {
--             "nama_tagging": "Program Unggulan Bupati",
--             "keterangan_tagging_program": [
--                 {
--                     "kode_program_unggulan": "PRG-UNG-73198f",
--                     "tahun": "2025"
--                 }
--             ]
--         }
--     ]
-- }
--
--
-- {
--     "nama_pohon": "Tes sub sub pemda",
--     "Keterangan": "-",
--     "jenis_pohon": "Sub Sub Tematik",
--     "level_pohon": 2,
--     "parent": 2487,
--     "tahun": "2025",
--     "kode_opd": null,
--     "status": "",
--     "indikator": [
--         {
--             "indikator": "indikator contoh",
--             "target": [
--                 {
--                     "target": "100",
--                     "satuan": "%"
--                 }
--             ]
--         },
--         {
--             "indikator": "contoh indikator 2",
--             "target": [
--                 {
--                     "target": "90",
--                     "satuan": "docs"
--                 }
--             ]
--         }
--     ],
--     "tagging": []
-- }