CREATE TABLE voto (
    id BIGSERIAL PRIMARY KEY,
    sessao_id BIGINT NOT NULL,
    associado_id BIGINT NOT NULL,
    opcao VARCHAR(3) NOT NULL,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_voto_sessao
        FOREIGN KEY (sessao_id)
        REFERENCES sessao(id),

    CONSTRAINT fk_voto_associado
        FOREIGN KEY (associado_id)
        REFERENCES associado(id),

    CONSTRAINT uk_voto_sessao_associado
        UNIQUE (sessao_id, associado_id),

    CONSTRAINT ck_voto_opcao
        CHECK (opcao IN ('SIM', 'NAO'))
);
