CREATE TABLE sessao (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pauta_id UUID NOT NULL REFERENCES pauta(id),
    aberta_em TIMESTAMP NOT NULL DEFAULT NOW(),
    expira_em TIMESTAMP NOT NULL
);