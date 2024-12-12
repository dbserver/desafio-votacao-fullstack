CREATE TABLE voto (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    pauta_id UUID NOT NULL REFERENCES pauta(id),
    associado_id UUID NOT NULL,
    voto VARCHAR(3) NOT NULL CHECK (voto IN ('SIM', 'NAO')),
    UNIQUE (pauta_id, associado_id)
);