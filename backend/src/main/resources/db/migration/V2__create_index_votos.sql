CREATE INDEX IF NOT EXISTS idx_votos_sessao_opcao
    ON votos (sessao_id, opcao);