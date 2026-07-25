CREATE TABLE pautas(
    id          BIGSERIAL PRIMARY KEY,
    titulo      VARCHAR(255) NOT NULL,
    descricao   VARCHAR(1000),
    dth_criacao TIMESTAMPTZ NOT NULL
)