ALTER TABLE sessao
    ADD COLUMN encerrada_em TIMESTAMP NULL;

CREATE INDEX idx_sessao_expira_em ON sessao(expira_em);
