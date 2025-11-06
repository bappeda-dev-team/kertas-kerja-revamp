CREATE TABLE IF NOT EXISTS pohon_kinerja
(
    id                  BIGSERIAL PRIMARY KEY,
    id_tematik          VARCHAR(255),
    id_sub_tematik      VARCHAR(255),
    id_sub_sub_tematik  VARCHAR(255),
    id_strategic        VARCHAR(255),
    id_tactical         VARCHAR(255),
    id_operational      VARCHAR(255),
    nama_pohon_kinerja  VARCHAR(255) NOT NULL,
    jenis_pohon_kinerja VARCHAR(255) NOT NULL,
    level_pohon_kinerja INTEGER,
    keterangan          VARCHAR(255),
    jenis_child         VARCHAR(20)  NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)