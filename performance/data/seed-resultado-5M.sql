BEGIN;

-- Remove apenas uma execução anterior desta massa de performance.
DELETE FROM votos
WHERE sessao_id IN (
    SELECT s.id
    FROM sessao_votacao s
    JOIN pautas p ON p.id = s.pauta_id
    WHERE p.titulo = '[PERF] Resultado 5M'
);

DELETE FROM sessao_votacao
WHERE pauta_id IN (
    SELECT id
    FROM pautas
    WHERE titulo = '[PERF] Resultado 5M'
);

DELETE FROM pautas
WHERE titulo = '[PERF] Resultado 5M';

DO $$
DECLARE
    v_pauta_id  BIGINT;
    v_sessao_id BIGINT;
BEGIN
    INSERT INTO pautas (
        titulo,
        descricao,
        dth_criacao
    )
    VALUES (
        '[PERF] Resultado 5M',
        'Massa reproduzível com 10 milhão de votos para teste de performance',
        CURRENT_TIMESTAMP
    )
    RETURNING id INTO v_pauta_id;

    INSERT INTO sessao_votacao (
        pauta_id,
        dth_inicio,
        dth_fim
    )
    VALUES (
        v_pauta_id,
        CURRENT_TIMESTAMP - INTERVAL '1 hour',
        CURRENT_TIMESTAMP + INTERVAL '1 hour'
    )
    RETURNING id INTO v_sessao_id;

    INSERT INTO associados (
        cpf,
        dth_criacao
    )
    SELECT
        (LPAD(numero::TEXT, 11, '0'))::TEXT,
        CURRENT_TIMESTAMP
    FROM generate_series(1, 5000000) AS serie(numero)
    ON CONFLICT (cpf) DO NOTHING;

    INSERT INTO votos (
        sessao_id,
        associado_id,
        opcao,
        dth_voto
    )
    SELECT
        v_sessao_id,
        a.id,
        CASE
            WHEN numero % 2 = 0 THEN 'SIM'
            ELSE 'NAO'
        END,
        CURRENT_TIMESTAMP
    FROM generate_series(1, 5000000) AS serie(numero)
    JOIN associados a
      ON a.cpf = LPAD(numero::TEXT, 11, '0');
END
$$;

COMMIT;

SELECT
    p.id AS pauta_id,
    COUNT(v.id) AS total_votos,
    COUNT(v.id) FILTER (WHERE v.opcao = 'SIM') AS votos_sim,
    COUNT(v.id) FILTER (WHERE v.opcao = 'NAO') AS votos_nao
FROM pautas p
JOIN sessao_votacao s ON s.pauta_id = p.id
JOIN votos v ON v.sessao_id = s.id
WHERE p.titulo = '[PERF] Resultado 5M'
GROUP BY p.id;