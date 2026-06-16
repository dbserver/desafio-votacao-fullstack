CREATE TABLE voto (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sessao_id UUID NOT NULL REFERENCES sessao(id),
    associado_id VARCHAR(255) NOT NULL,
    opcao VARCHAR(3) NOT NULL CHECK (opcao IN ('SIM', 'NAO')),
    votado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_voto_sessao_associado UNIQUE (sessao_id, associado_id)
);