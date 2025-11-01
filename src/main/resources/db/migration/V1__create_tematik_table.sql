 CREATE TABLE IF NOT EXISTS tematik (
     id BIGSERIAL PRIMARY KEY,
     id_tematik VARCHAR(255) NOT NULL UNIQUE,
     nama_pohon_kinerja VARCHAR(255) NOT NULL UNIQUE,
     jenis_pohon_kinerja VARCHAR(255) NOT NULL,
     level_pohon_kinerja VARCHAR(255) NOT NULL,
     keterangan VARCHAR(255),
     jenis_child VARCHAR(20) NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 );