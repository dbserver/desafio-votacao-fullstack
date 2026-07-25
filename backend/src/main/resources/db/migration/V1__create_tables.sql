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

CREATE TABLE associados(
    id          BIGSERIAL PRIMARY KEY,
    cpf         VARCHAR(11) NOT NULL,
    dth_criacao TIMESTAMPTZ NOT NULL,

    CONSTRAINT uk_associado_cpf
        UNIQUE (cpf)
);

CREATE TABLE votos(
    id           BIGSERIAL PRIMARY KEY,
    sessao_id    BIGINT NOT NULL,
    associado_id BIGINT NOT NULL,
    opcao        VARCHAR(3) NOT NULL,
    dth_voto     TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_voto_sessao
       FOREIGN KEY (sessao_id)
    REFERENCES sessao_votacao(id),

    CONSTRAINT fk_voto_associado
       FOREIGN KEY (associado_id)
    REFERENCES associados(id),

    CONSTRAINT uk_voto_sessao_associado
        UNIQUE (sessao_id, associado_id),

    CONSTRAINT ck_voto_opcao
    CHECK (opcao IN ('SIM', 'NAO'))
);