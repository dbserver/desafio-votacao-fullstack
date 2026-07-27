# Primeiro teste de performance

Esta etapa valida o ambiente com 1.000 votos e 10 consultas ao endpoint de
resultado. Ela ainda não representa a baseline definitiva.

## Pré-requisitos

- API iniciada em `http://localhost:8080`;
- PostgreSQL iniciado em `localhost:5433`;
- k6 instalado e disponível no terminal.

Confirme o k6:

```powershell
k6 version
```

## 1. Criar a massa de dados

Abra o pgAdmin, conecte ao banco `voting`, abra o Query Tool e execute:

```text
performance/data/seed-resultado-1k.sql
```

Ao final, a consulta deve retornar:

```text
total_votos = 1000
votos_sim   = 500
votos_nao   = 500
```

Copie o valor retornado na coluna `pauta_id`.

O script pode ser executado novamente. Ele remove somente a pauta chamada
`[PERF] Resultado 1K` e recria sua sessão e seus votos.

## 2. Conferir o endpoint manualmente

Substitua `ID_DA_PAUTA` pelo valor retornado pelo seed:

```powershell
curl.exe http://localhost:8080/api/v1/resultados/ID_DA_PAUTA
```

O endpoint deve responder com HTTP 200 e informar 500 votos SIM e 500 votos
NÃO.

## 3. Executar o smoke test

Na raiz do projeto, execute:

```powershell
k6 run -e PAUTA_ID=ID_DA_PAUTA performance/scripts/resultado-smoke.js
```

Se a API estiver em outro endereço:

```powershell
k6 run `
  -e BASE_URL=http://localhost:8080 `
  -e PAUTA_ID=ID_DA_PAUTA `
  performance/scripts/resultado-smoke.js
```

## 4. Como saber se funcionou

O resumo deve apresentar:

- `checks` em 100%;
- `http_req_failed` em 0%;
- 10 iterações concluídas;
- os limites do teste com resultado verde.

O `http_req_duration p(95)` já aparecerá, mas ainda não deve ser usado para
concluir que a aplicação é rápida ou lenta. Com apenas 10 requisições, este
teste confirma que rota, dados e ferramenta estão funcionando corretamente.

Depois do smoke test, o próximo passo é executar uma carga com aquecimento,
duração fixa e múltiplos usuários virtuais para registrar a baseline.
