CREATE TABLE IF NOT EXISTS indikator_pohon_kinerja
(
    id             BIGSERIAL PRIMARY KEY,
    id_tematik     VARCHAR(255),
    id_sub_tematik VARCHAR(255),
    id_sub_sub_tematik VARCHAR(255),
    id_strategic    VARCHAR(255),
    id_tactical VARCHAR(255),
    id_operational VARCHAR(255),
    indikator      VARCHAR(255) NOT NULL,
    tahun          INT          NOT NULL,
    target         JSONB,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)