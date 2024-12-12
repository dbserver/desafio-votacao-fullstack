CREATE TABLE IF NOT EXISTS sessao_votacao (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    pauta_id UUID NOT NULL,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP NOT NULL,
    FOREIGN KEY (pauta_id) REFERENCES pauta(id)
);