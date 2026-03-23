# Sistema de Votacao - Frontend (React + TypeScript)

Aplicacao frontend para gestao de pautas, sessoes de votacao, votos de associados e visualizacao de resultados.

## Stack

- React 19
- TypeScript
- Vite
- React Router
- Vitest + Testing Library

## Requisitos

- Node.js 22+
- npm 10+

## Como executar

1. Instale dependencias:

```bash
npm install
```

2. Configure ambiente:

Crie/ajuste o arquivo `.env` na raiz com:

```env
VITE_API_BASE_URL=http://localhost:8080/api/v1
```

3. Rode em desenvolvimento:

```bash
npm start
```

4. Acesse:

- http://localhost:5173

## Scripts

- `npm start`: inicia ambiente de desenvolvimento
- `npm run build`: gera build de producao
- `npm run preview`: serve build localmente
- `npm run typecheck`: validacao de tipos TypeScript
- `npm run lint`: lint da base
- `npm run test`: executa testes em watch mode
- `npm run test:run`: executa testes uma vez (CI)

## Funcionalidades implementadas

- Cadastrar pauta
- Listar pautas
- Abrir sessao de votacao por pauta
	- Duracao customizada em minutos
	- Campo opcional com default backend (1 minuto)
- Listar sessoes da pauta
	- Sessao aberta: card clicavel para votar
	- Sessao encerrada: botao para ver resultado
- Registrar voto
	- Payload: `sessaoVotacaoId`, `cpf`, `vote`
	- Mascara de CPF no input e envio normalizado (somente digitos)
- Consultar resultado da sessao
	- Endpoint: `/sessoes/{sessaoVotacaoId}/results`

## Arquitetura de pastas

```text
src/
	components/      # UI compartilhada (layout, toast)
	features/
		pautas/        # dominio de pautas
		sessoes/       # dominio de sessoes
		votacao/       # dominio de votacao
	hooks/           # hooks globais/transversais
	lib/             # infraestrutura (http client)
	pages/           # composicao de tela/rotas
	types/           # contratos de tipos
```

## Endpoints consumidos

- `GET /pautas`
- `POST /pautas`
- `GET /sessoes/pauta/{pautaId}`
- `POST /sessoes`
- `GET /sessoes/{sessaoVotacaoId}/results`
- `POST /votos`

## Tratamento de erros

- Erros HTTP sao capturados no client central (`requestJson`).
- Toast global apresenta mensagem priorizando retorno de negocio da API (`payload.message`), evitando mensagens genericas por status quando houver contexto.

## Observacoes

- Esta aplicacao assume backend autenticado/autoriza chamadas para fins de teste tecnico.
- Persistencia e regras de negocio (voto unico por associado por pauta, controle de sessao etc.) sao responsabilidades do backend.
