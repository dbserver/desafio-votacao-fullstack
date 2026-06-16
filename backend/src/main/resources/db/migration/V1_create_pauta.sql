CREATE TABLE pauta (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW()
);