# Votacao Cooperativa

Sistema web para gerenciar pautas, abrir sessoes de votacao, registrar votos e acompanhar o resultado em tempo real.

## Stack

- Backend: Java 21, Spring Boot, JPA, Flyway, WebSocket, Swagger
- Frontend: React 18, Vite, TypeScript, Tailwind CSS, Axios, React Router
- Banco: PostgreSQL

## Decisões de arquitetura

- Controllers ficam finos e delegam as regras para services, o que facilita manutenção e testes.
- A listagem de pautas usa paginação para evitar carregar mais dados do que o necessário.
- As criações retornam `201 Created` com `Location`, apontando para o recurso recém-criado.
- O encerramento automático da sessão é feito no backend por scheduler, evitando depender do frontend para encerrar votação.
- O resultado em tempo real é publicado por WebSocket, enquanto a API REST continua cuidando do estado e da persistência.
- Os testes usam banco em memória para validar contratos e regras sem depender do PostgreSQL local.

## Performance

- A listagem é paginada para manter o payload pequeno.
- O resultado da votação é calculado com consultas agregadas simples no banco.
- Foram adicionados índices leves em campos de busca frequente para ajudar na escalabilidade do fluxo de sessão e apuração.
- O WebSocket envia atualização somente quando há mudança de voto ou encerramento de sessão, sem polling.

## Versionamento da API

- A estratégia adotada é versionamento por URI, com o prefixo `/api/v1`.
- Isso mantém compatibilidade entre versões e facilita evoluções futuras sem quebrar consumidores antigos.
- Se houver uma mudança incompatível, a próxima versão pode entrar como `/api/v2` sem alterar o contrato atual.

## Logs e observabilidade

- Cada requisição recebe ou reaproveita um `X-Request-Id`, que também volta na resposta.
- O console inclui o `requestId` no padrão de log para facilitar a correlação entre chamadas, erros e ações do scheduler.
- O backend registra inicio e fim das requisições, alem de excecoes inesperadas com status e contexto da operacao.
- O nivel de log pode ser ajustado com `LOG_LEVEL_APP`.
- Os timestamps da aplicação seguem o horário do Brasil (`America/Sao_Paulo`) para persistência e exibição.

## Funcionalidades entregues

- Cadastro de pauta
- Listagem paginada de pautas em tabela
- Detalhe da pauta com abertura de sessao
- Votacao por `SIM` e `NAO`
- Contagem regressiva baseada em `expiraEm`
- Encerramento automatico da sessao quando o tempo termina
- Resultado da votacao com atualizacao em tempo real via WebSocket
- Tela inicial de login visual
- Bonus de CPF com client fake e endpoint de status

## Como executar localmente

### 1. Banco de dados

O backend usa PostgreSQL por padrao. Voce pode subir tudo com Docker Compose ou rodar o banco localmente com as mesmas credenciais.

### 2. Backend

```bash
cd backend
./mvnw spring-boot:run
```

Se quiser sobrescrever a conexao com o banco:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/votacao
export SPRING_DATASOURCE_USERNAME=votacao
export SPRING_DATASOURCE_PASSWORD=votacao
./mvnw spring-boot:run
```

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

Se necessario, copie `frontend/.env.example` para `frontend/.env` e ajuste as URLs da API e do WebSocket.

## Executar com Docker

```bash
docker compose up --build
```

Servicos expostos:

- Frontend: `http://localhost:3000`
- Backend: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui.html`

O `docker-compose.yml` sobe:

- PostgreSQL com volume persistente
- Backend Spring Boot
- Frontend compilado em Nginx

## Variaveis de ambiente

### Backend

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`
- `VOTACAO_SESSAO_VERIFICACAO_MS`
- `LOG_LEVEL_APP`

### Frontend

- `VITE_API_BASE_URL`
- `VITE_WS_URL`

## Testes

### Backend

```bash
cd backend
./mvnw test
```

Os testes usam perfil `test` com banco em memoria e nao dependem do PostgreSQL local.

Os testes automatizados cobrem os fluxos principais da API, incluindo:

- criação e paginação de pautas;
- abertura de sessão com duração padrão;
- registro de voto;
- bloqueio de voto duplicado;
- votação quando o CPF não está autorizado;
- distinção entre sessão inexistente e sessão encerrada;
- encerramento automático e publicação do resultado.

### Frontend

```bash
cd frontend
npm run build
```

## API principal

- `POST /api/v1/pautas`
- `GET /api/v1/pautas`
- `GET /api/v1/pautas/{id}`
- `POST /api/v1/pautas/{id}/sessao`
- `GET /api/v1/pautas/{id}/sessao`
- `GET /api/v1/sessoes/{id}`
- `POST /api/v1/sessoes/{sessaoId}/votos`
- `GET /api/v1/sessoes/{sessaoId}/votos/{votoId}`
- `GET /api/v1/sessoes/{sessaoId}/resultado`

## Bonus de CPF

Foi adicionado um status fake de CPF para demonstracao do bonus.

- `GET /api/v1/cpfs/{cpf}`
- Retorno valido: `{ "status": "ABLE_TO_VOTE" }`
- Retorno invalido: `404 Not Found`
- O resultado e aleatorio, entao o mesmo CPF pode mudar entre chamadas

## Observacoes

- A persistencia das pautas, sessoes e votos usa PostgreSQL.
- O frontend consome a API por `VITE_API_BASE_URL`.
- O resultado em tempo real usa SockJS + STOMP em `VITE_WS_URL`.
- O login e visual, sem autenticacao real.
