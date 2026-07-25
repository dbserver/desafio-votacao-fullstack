CREATE TABLE pautas(
    id          BIGSERIAL PRIMARY KEY,
    titulo      VARCHAR(255) NOT NULL,
    descricao   VARCHAR(1000),
    dth_criacao TIMESTAMPTZ NOT NULL
);

CREATE TABLE sessao_votacao (
    id         BIGSERIAL PRIMARY KEY,
    pauta_id   BIGINT NOT NULL,
    dth_inicio TIMESTAMPTZ NOT NULL,
    dth_fim    TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_sessao_vot_pauta
       FOREIGN KEY (pauta_id)
    REFERENCES pautas(id),

    CONSTRAINT uk_sessao_vot_pauta
        UNIQUE (pauta_id),

    CONSTRAINT ck_sessao_vot_pauta
         CHECK (dth_fim > dth_inicio)
);