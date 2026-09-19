CREATE TABLE sessao (
    id BIGSERIAL PRIMARY KEY,
    pauta_id BIGINT NOT NULL,
    inicio TIMESTAMPTZ NOT NULL,
    fim TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_sessao_pauta
        FOREIGN KEY (pauta_id)
        REFERENCES pauta(id),
        
    CONSTRAINT uk_sessao_pauta UNIQUE (pauta_id),
    CONSTRAINT ck_sessao_periodo CHECK (fim > inicio)
);
