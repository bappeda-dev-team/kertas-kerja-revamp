CREATE TABLE IF NOT EXISTS indikator
(
    id             BIGSERIAL PRIMARY KEY,
    id_tematik     VARCHAR(255) NOT NULL,
    id_sub_tematik VARCHAR(255),
    indikator      VARCHAR(255) NOT NULL,
    tahun          INT          NOT NULL,
    target         JSONB,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT indikator_id_tematik
        FOREIGN KEY (id_tematik)
            REFERENCES tematik (id_tematik)
            ON DELETE CASCADE,
    CONSTRAINT indikator_id_sub_tematik
        FOREIGN KEY (id_sub_tematik)
            REFERENCES sub_tematik (id_sub_tematik)
            ON DELETE SET NULL
);