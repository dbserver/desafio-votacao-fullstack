# 🗳️ Sistema de Votação (Cooperativa) — Fullstack

> Solução fullstack para gerenciamento e participação em **sessões de votação** de uma cooperativa, composta por uma **API REST** (Java + Spring Boot), uma **interface web** (React + TypeScript) e uma **suíte de testes de performance** (k6).

![Java 17](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)
![Spring Boot 4.1.1](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?logo=spring&logoColor=white)
![React 19](https://img.shields.io/badge/React-19-%2361DAFB?logo=react&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-estrito-%233178C6?logo=typescript&logoColor=white)
![Vite 8](https://img.shields.io/badge/Vite-8-%23646CFF?logo=vite&logoColor=white)
![PostgreSQL 16](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-%232496ED?logo=docker&logoColor=white)
![k6](https://img.shields.io/badge/k6-Testes_de_Carga-%237D64FF?logo=grafana&logoColor=white)
![Licença MIT](https://img.shields.io/badge/Licen%C3%A7a-MIT-yellow.svg)

---

## 📑 Índice

- [🎯 Objetivo](#-objetivo)
- [🏛️ Resumo da Solução e Arquitetura](#️-resumo-da-solução-e-arquitetura)
- [🧱 Stack Tecnológica](#-stack-tecnológica)
- [✨ Funcionalidades Principais](#-funcionalidades-principais)
- [▶️ Como Executar o Projeto](#️-como-executar-o-projeto)
- [🔗 URLs da API e do Frontend](#-urls-da-api-e-do-frontend)
- [📡 Documentação da API](#-documentação-da-api)
- [⚙️ Justificativa das Escolhas Técnicas](#️-justificativa-das-escolhas-técnicas)
- [🏆 Tarefas Bônus](#-tarefas-bônus)
- [🧪 Testes Automatizados](#-testes-automatizados)
- [🗂️ Estrutura do Repositório](#️-estrutura-do-repositório)
- [🚀 Roadmap e Melhorias Futuras](#-roadmap-e-melhorias-futuras)
- [📄 Licença](#-licença)

---

## 🎯 Objetivo

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias por votação. Este projeto entrega uma solução web para **gerenciar e participar dessas sessões**, com:

- 📋 **Cadastro de pautas** de deliberação;
- ⏱️ **Abertura de sessões de votação** com duração determinada (ou **1 minuto** por default);
- 🗳️ **Registro de votos** (`Sim`/`Não`) por associado, com **voto único por pauta**;
- 🏆 **Contabilização e resultado da votação** por pauta.

A solução foi construída com uma **API REST** (Java + Spring Boot) consumida por um **frontend** (React), com **persistência em PostgreSQL** — as pautas e os votos não são perdidos com o restart da aplicação. Para o exercício, a segurança das interfaces foi abstraída (qualquer chamada é considerada autorizada).

---

## 🏛️ Resumo da Solução e Arquitetura

O projeto é um **monorepo** com três módulos:

| Módulo | Diretório | Descrição |
| --- | --- | --- |
| 🔙 **Backend** | [`backend/`](backend/) | API RESTful em Java 17 + Spring Boot 4.1.x com persistência em PostgreSQL (H2 nos testes). |
| 🖥️ **Frontend** | [`frontend/`](frontend/) | SPA em React 19 + TypeScript estrito, construída com Vite e servida por Nginx em produção. |
| 🚀 **Performance** | [`perf/`](perf/) | Suíte de testes de carga e estresse (Grafana k6) para cenários com centenas de milhares de votos. |

### Fluxo da arquitetura

```
┌───────────────┐   HTTPS (Axios)   ┌──────────────────────────────────────────────┐
│   Frontend    │ ─────────────────▶ │                 Backend (Spring Boot)         │
│  React 19     │   /v1/**          │  Controller → Service → Repository (JPA)      │
│  + Vite/Nginx │ ◀───────────────── │       │                        │              │
└───────────────┘   JSON (DTOs)     │       ▼                        ▼              │
                                    │  CpfValidationClient    PostgreSQL 16         │
                                    │  (mock → integração      (persistência)       │
                                    │   externa de CPF)                             │
                                    └──────────────────────────────────────────────┘
```

- O **frontend** consome apenas o contrato público da API (`/v1/**`) por meio de uma instância central do Axios, sem conhecer a persistência.
- Os **controllers** expõem os endpoints REST; os **services** concentram as regras de negócio (abertura de sessão, unicidade do voto, habilitação do CPF) e os **repositories** (Spring Data JPA) abstraem o acesso ao banco.
- **DTOs** isolam o contrato público da API das entidades de persistência, e o pacote `client` encapsula a integração externa de validação de CPF (mock).

---

## 🧱 Stack Tecnológica

| Camada | Tecnologia |
| --- | --- |
| ☕ **Backend** | Java 17 · Spring Boot 4.1.x (Spring Web, Data JPA, Validation) · Lombok · springdoc-openapi (Swagger UI) |
| 🗄️ **Banco de Dados** | PostgreSQL 16 (execução) · H2 em memória (testes automatizados) |
| 🧰 **Build Backend** | Maven 3.8+ (com wrapper `./mvnw`) |
| ⚛️ **Frontend** | React 19 · TypeScript (estrito) · Vite 8 · Zustand 5 (estado global) · React Router v7 (lazy loading) · Tailwind CSS v4 · Axios · Lucide React · Radix UI |
| 🐳 **Infraestrutura** | Docker & Docker Compose · Dockerfile multi-stage do frontend · Nginx (servindo a SPA) |
| 🧪 **Qualidade** | JUnit 5 + Mockito · ESLint (flat config) · Prettier · Conventional Commits · Grafana k6 |

---

## ✨ Funcionalidades Principais

| Domínio | Descrição |
| --- | --- |
| 📋 **Gestão de Pautas** | Cadastrar e listar pautas de deliberação. |
| ⏱️ **Gestão de Sessões de Votação** | Abrir sessão para uma pauta com duração customizada em minutos ou **1 minuto** por default (uma sessão por pauta). |
| 🗳️ **Registro e Validação de Votos** | Voto `SIM`/`NAO` com validação de CPF (`ABLE_TO_VOTE`/`UNABLE_TO_VOTE`) e **impedimento de voto duplicado** do mesmo associado na mesma pauta. |
| 🏆 **Resultado e Apuração** | Endpoint dedicado que retorna `votosSim`, `votosNao`, `totalVotos` e o status consolidado (`SESSÃO EM ANDAMENTO`, `APROVADA`, `REPROVADA`, `EMPATE`). |

---

## ▶️ Como Executar o Projeto

### ✅ Pré-requisitos

| Ferramenta | Versão | Observação |
| --- | --- | --- |
| ☕ **JDK** | 17 (ou 21) | Necessário para o backend |
| 🧰 **Maven** | 3.8+ | Ou use o wrapper `./mvnw` incluído |
| 🟢 **Node.js** | ≥ 20.19.0 | Necessário para o frontend |
| 📦 **npm** | 10+ | Gerenciador de pacotes |
| 🐳 **Docker + Docker Compose** | — | Para PostgreSQL e/ou para rodar o frontend e os testes de carga em containers |

### 1️⃣ Execução sem Docker

> ⚠️ O backend executa com **PostgreSQL**. Sem Docker, instale o **PostgreSQL 16** localmente e crie o banco/usuário abaixo (ou ajuste `backend/src/main/resources/application.yml`).

**Credenciais esperadas:**

| Variável | Valor |
| --- | --- |
| Banco (`url`) | `jdbc:postgresql://localhost:5432/votacao` |
| Usuário | `votacao` |
| Senha | `votacao` |

**Backend (API):**

```bash
# 1. A partir da raiz do repositório
cd backend

# 2. Compilar o projeto (inclui a execução dos testes, que rodam em H2 — sem banco externo)
./mvnw clean install

# 3. Subir a aplicação conectada ao PostgreSQL local
./mvnw spring-boot:run
```

A API sobe em **http://localhost:8080** com o schema das tabelas criado/atualizado automaticamente (`ddl-auto: update`).

**Frontend (SPA):**

```bash
# 1. A partir da raiz do repositório
cd frontend

# 2. Instalar as dependências
npm install

# 3. (Opcional) Configurar a URL da API — há padrão
cp .env.example .env

# 4. Subir o servidor de desenvolvimento
npm run dev
```

A aplicação fica disponível em **http://localhost:5173** com hot reload. O fluxo completo depende do backend rodando em `http://localhost:8080`.

> 💡 Build de produção local: `npm run build` (typecheck + build → pasta `dist/`) e `npm run preview` para servir o build.

### 2️⃣ Execução com Docker

**Backend — PostgreSQL via Docker Compose:**

```bash
cd backend
docker compose up -d
```

Cria o container `votacao-postgres` (imagem `postgres:16-alpine`) com banco, usuário e senha `votacao` expostos na porta `5432`. Em seguida, executa a aplicação com `./mvnw spring-boot:run` dentro de `backend/`.

```bash
# Para encerrar o PostgreSQL (apague também os dados com -v se quiser)
docker compose down -v
```

> O backend em si ainda não possui Dockerfile próprio — isso está mapeado no [Roadmap](#-roadmap-e-melhorias-futuras). A aplicação Java roda via Maven e conecta ao PostgreSQL containerizado.

**Frontend — build multi-stage + Nginx:**

```bash
cd frontend
docker compose up -d --build
```

O `Dockerfile` multi-stage (build `node:20-alpine` → serve `nginx:alpine`) gera os assets estáticos da SPA e o Nginx os serve em **http://localhost:3000**.

A URL da API é injetada **em tempo de build** (variáveis `VITE_*`). O padrão aponta para `http://localhost:8080/v1`; para outro backend, ajuste o `build.args` do `frontend/docker-compose.yml`:

```yaml
services:
  frontend:
    build:
      context: .
      dockerfile: Dockerfile
      args:
        VITE_API_BASE_URL: http://localhost:8080/v1   # 👈 URL do backend
    ports:
      - "3000:80"
```

**Performance — suíte k6:**

```bash
cd perf
docker compose up --build --exit-code-from k6-runner
```

> Requer a API rodando na porta `8080` e uma pauta com ID `1` com sessão de votação aberta. Veja [Tarefa Bônus 2](#-tarefa-bônus-2) e o [`perf/Readme.md`](perf/Readme.md).

### 3️⃣ Fluxo completo resumido

| Passo | Ação | Resultado |
| --- | --- | --- |
| 1 | Subir PostgreSQL (Docker ou local) | Banco `votacao` na porta `5432` |
| 2 | `cd backend && ./mvnw spring-boot:run` | API em `http://localhost:8080` |
| 3 | `cd frontend && npm run dev` (ou `docker compose up -d --build`) | App em `http://localhost:5173` (ou `:3000`) |
| 4 | Abrir a aplicação no navegador | Cadastrar pauta → abrir sessão → votar → ver resultado |

---

## 🔗 URLs da API e do Frontend

| Recurso | URL |
| --- | --- |
| 🔙 **API Backend** (base) | `http://localhost:8080` |
| 📄 **Swagger UI** (documentação interativa) | `http://localhost:8080/swagger-ui.html` |
| 📃 **OpenAPI JSON** (spec) | `http://localhost:8080/v3/api-docs` |
| 🗄️ **PostgreSQL** (container/local) | `localhost:5432` (db/usuario/senha: `votacao`) |
| 🖥️ **Frontend — desenvolvimento** | `http://localhost:5173` |
| 🖥️ **Frontend — Docker/Nginx** | `http://localhost:3000` |
| 🧪 **Coleção de requisições** | [`backend/api-tests.http`](backend/api-tests.http) |

---

## 📡 Documentação da API

A API é **versionada por URI Path** (`/v1`). Todos os contratos abaixo usam **JSON**.

### 📋 Pautas

| Método | Rota | Descrição | Parâmetros |
| --- | --- | --- | --- |
| `POST` | `/v1/pautas` | Cadastra uma nova pauta | Body: `titulo`\*, `descricao` |
| `GET` | `/v1/pautas` | Lista todas as pautas cadastradas | — |
| `GET` | `/v1/pautas/{pautaId}/resultado` | Retorna a apuração/resultado da votação | Path: `pautaId` |

### ⏱️ Sessões de Votação

| Método | Rota | Descrição | Parâmetros |
| --- | --- | --- | --- |
| `POST` | `/v1/sessoes/abrir` | Abre a sessão de votação de uma pauta | Body: `pautaId`\*, `duracaoMinutos` (default: 1) |
| `GET` | `/v1/sessoes` | Lista todas as sessões de votação | — |

### 🗳️ Votos

| Método | Rota | Descrição | Parâmetros |
| --- | --- | --- | --- |
| `POST` | `/v1/votos` | Registra o voto de um associado em uma pauta | Body: `pautaId`\*, `associadoId`\*, `voto`\* (`SIM`/`NAO`) |

> \* Campo obrigatório.

### 📥 Exemplos de payloads

**Cadastrar pauta — `POST /v1/pautas`** → `201 Created`

```json
{
  "titulo": "Aprovação do Orçamento Anual 2026",
  "descricao": "Votação para deliberar sobre os custos operacionais da cooperativa."
}
```

**Abrir sessão — `POST /v1/sessoes/abrir`** → `201 Created` (sem `duracaoMinutos`, a sessão dura **1 minuto**)

```json
{ "pautaId": 1, "duracaoMinutos": 3 }
```

**Registrar voto — `POST /v1/votos`** → `201 Created`

```json
{ "pautaId": 1, "associadoId": "12345678901", "voto": "SIM" }
```

**Resultado — `GET /v1/pautas/1/resultado`** → `200 OK`

```json
{
  "pautaId": 1,
  "tituloPauta": "Aprovação do Orçamento Anual 2026",
  "votosSim": 2,
  "votosNao": 1,
  "totalVotos": 3,
  "resultado": "APROVADA",
  "sessaoEncerrada": true
}
```

Possíveis valores de `resultado`: `SESSÃO EM ANDAMENTO`, `APROVADA`, `REPROVADA`, `EMPATE`.

### ⚠️ Tratamento de Erros

O `GlobalExceptionHandler` (`@RestControllerAdvice`) centraliza o tratamento e padroniza o corpo de erro.

| Status HTTP | Cenário |
| --- | --- |
| `400` | Regra de negócio violada (voto duplicado, sessão encerrada, associado inabilitado) ou falha de validação `@Valid` |
| `404` | Pauta/sessão inexistente ou **CPF inválido** |
| `409` | Violação de unicidade no banco (voto duplicado na mesma pauta) |
| `500` | Erro interno inesperado |

> Para payloads completos de request/response, cenários de erro e detalhes da arquitetura do backend, consulte o [`backend/README.md`](backend/README.md) e o arquivo [`api-tests.http`](backend/api-tests.http), que traz uma coleção pronta para IDE ou CLI.

---

## ⚙️ Justificativa das Escolhas Técnicas

### Backend

- **Java 17 + Spring Boot 4.1.x**: ecossistema maduro e produtivo para APIs REST; o Boot gerencia dependências (BOM), configuração e servlet embutido, reduzindo *boilerplate* e mantendo o foco nas regras de negócio.
- **PostgreSQL 16**: banco relacional robusto, com integridade referencial e constraints — requisito central do desafio é a **persistência sem perda com restart**. O **H2 em memória** fica isolado nos testes automatizados, garantindo execução sem banco externo.
- **DTOs**: desacoplam o contrato público da API (`PautaResponse`, `VotoResponse`, ...) das entidades JPA, evitando expor detalhes de persistência e permitindo evoluir cada lado de forma independente.
- **Arquitetura em camadas** (controller → service → repository): regras de negócio concentradas e testáveis de forma unitária; acesso a dados abstraído pelo Spring Data JPA.
- **Barreira dupla contra voto duplicado**: verificação em memória (`existsByPautaIdAndAssociadoId`) **e** `UNIQUE CONSTRAINT (pauta_id, associado_id)` na tabela `votos` — protege inclusive contra condições de corrida em carga alta.
- **Pool HikariCP configurado** (`maximum-pool-size: 50`): preparação para o cenário de alta carga da [Tarefa Bônus 2](#-tarefa-bônus-2).
- **Logs com Slf4j**: trilha operacional (INFO em operações, WARN em recusas de negócio, DEBUG em detalhes) sem sacrificar legibilidade.
- **`@RestControllerAdvice` global**: tratamento uniforme de `400/404/409/500` com corpo de erro padronizado.
- **CORS configurado** e **documentação OpenAPI 3 / Swagger UI** como contrato vivo da API.

### Frontend

- **React 19 + TypeScript estrito**: componentes declarativos com segurança de tipos em toda a camada (types + *type guards* para erros `unknown`), reduzindo bugs de contrato com a API.
- **Vite 8**: build rápido e DX moderna; integração nativa com React e Tailwind via plugins.
- **Zustand 5**: estado global leve e tipado, com **stores separadas por domínio** (`usePautaStore`, `useSessaoStore`, `useVotoStore`) — sem re-renderizações desnecessárias e com código simples de manter.
- **React Router v7 com lazy loading**: rotas carregadas sob demanda (code splitting), melhorando o tempo de carregamento inicial.
- **Tailwind CSS v4 + componentes utilitários reutilizáveis** (`BadgeStatus`, `ProgressBar`, `FeedbackMessage`, `LoadingSpinner`...): layout responsivo e consistente com pouco esforço de CSS.
- **Axios com instância central**: URL base única (`VITE_API_BASE_URL`) e interceptors para tratamento padronizado de erros da API.
- **Docker multi-stage + Nginx**: a SPA é um artefato estático servido por Nginx — deployment simples, leve e stateless.

### Qualidade

- **Conventional Commits** no histórico do repositório (`feat(scope)`, `fix(scope)`, `docs(scope)`, `test(scope)`...).
- **ESLint (flat config) + Prettier** integrados aos scripts do frontend (`lint`, `format`).
- **Testes automatizados** (JUnit 5 + Mockito) cobrindo serviços, fatias de controller (`@WebMvcTest`), mock de CPF e smoke test do contexto Spring ([detalhes](#-testes-automatizados)).

---

## 🏆 Tarefas Bônus

### Bônus 1 — Integração com sistemas externos (validação de CPF)

A integração é encapsulada pela interface **`CpfValidationClient`** com uma implementação **mock** (`CpfValidationClientMock`) que simula o serviço externo:

| Entrada (CPF) | Comportamento |
| --- | --- |
| Menos de 11 dígitos | ❌ `404 Not Found` — *"CPF inválido ou não encontrado na base federal"* |
| 11 dígitos e último dígito **par** | ✅ Status `ABLE_TO_VOTE` — associado habilitado a votar |
| 11 dígitos e último dígito **ímpar** | 🚫 Status `UNABLE_TO_VOTE` — voto recusado com `400` |

```json
// CPF OK para votar
{ "status": "ABLE_TO_VOTE" }

// CPF não habilitado a votar
{ "status": "UNABLE_TO_VOTE" }
```

> Para fins de exercício, a validação é simulada e determinística (dígito par/ímpar) para facilitar testes. Como o contrato parte de uma **interface**, a implementação mock pode ser substituída por uma integração real (ex.: serviço de CPF na nuvem) **sem alterar a camada de negócio**.

### Bônus 2 — Performance

A suíte [`perf/`](perf/) usa **Grafana k6** para validar a API em cenários de alta carga (centenas de milhares de votos):

- **Curva de carga** (`load-test.js`): rampa de subida de `0 → 50 VUs` em `10s`, pico de estresse mantendo **`200 VUs` simultâneos** por `30s`, e rampa de descida em `10s`.
- **Critérios de aceite (thresholds)**:
  - `http_req_duration`: **95% das requisições < 200 ms** (`p(95) < 200`);
  - `http_req_failed`: menos de **1%** de falhas HTTP (`rate < 0.01`).
- O script gera **CPFs sintéticos válidos** para o mock (terminados em dígito par → `ABLE_TO_VOTE`), e loga cada requisição com falha para diagnóstico.

**Como executar:**

```bash
cd perf
docker compose up --build --exit-code-from k6-runner
```

> Pré-requisitos: API rodando na porta `8080` e uma pauta com ID `1` com sessão de votação aberta. O `docker-compose.yml` usa `host.docker.internal` para alcançar a API no host.

O [`perf/Readme.md`](perf/Readme.md) traz o guia completo de execução, diagnósticos (erros de HikariCP, duplicidade de voto, pauta/sessão fechada) e resolução de gargalos.

### Bônus 3 — Versionamento da API: estratégia adotada

> **Estratégia escolhida: URI Path Versioning** (`/v1/pautas`, `/v1/sessoes`, `/v1/votos`).

**Justificativa:**

1. **Clareza e legibilidade**: a versão é explícita na URL, visível em logs, dashboards, links compartilhados e nas chamadas do próprio frontend (`VITE_API_BASE_URL=http://localhost:8080/v1`) — qualquer pessoa entende qual versão do contrato está sendo consumida sem inspecionar headers.
2. **Facilidade de roteamento**: no Spring Boot, a versão é declarada diretamente no mapeamento (`@RequestMapping("/v1/...")`), sem configuração extra de `ContentNegotiation`, interceptors ou middlewares. O frontend aponta explicitamente para `/v1`, tornando o roteamento trivial e determinístico.
3. **Desacoplamento**: cada versão da API expõe seu próprio caminho e contrato estável. Uma evolução com *breaking changes* simplesmente adiciona `/v2/...` coexistindo com `/v1` — consumidores antigos continuam funcionando sem alteração, e clientes novos podem migrar no seu próprio ritmo.
4. **Infraestrutura amigável**: URL versionada funciona bem com **cache**, **CDNs** e **proxies** (cada versão é um recurso distinto), e facilita *A/B testing* de contrato entre versões.

**Por que não as alternativas:**

| Estratégia | Motivo da não escolha |
| --- | --- |
| **Query Parameter** (`/pautas?version=1`) | A versão vira dado auxiliar, fácil de apagar/esquecer em logs, cache e links; a URL não é autoexplicativa. |
| **Header / Media Type** (`Accept: application/vnd.votacao.v1+json`) | Mais complexo de consumir (exige cabeçalho especial em todo cliente e configuração de negociação de conteúdo), e menos visível/depurável que a URL. |
| **Subdomínio/domínio próprio** (`v1.api.…`) | Bom para escala grande, porém **over-engineering** para este projeto: adiciona custo de infraestrutura e DNS sem ganho proporcional. |

> Conclusão: para uma API REST pública deste porte, o **URI Path Versioning** entrega o melhor equilíbrio entre simplicidade, clareza, facilidade de roteamento e desacoplamento entre as versões — alinhado ao requisito de evitarmos *over-engineering*.

---

## 🧪 Testes Automatizados

A suíte do backend usa **JUnit 5 + Mockito**, com testes unitários de serviços, testes de fatias de controller (`@WebMvcTest`) e testes do mock de CPF:

| Classe de teste | Escopo |
| --- | --- |
| `PautaServiceTest` | Regras de negócio de pautas |
| `SessaoVotacaoServiceTest` | Abertura/duração de sessões e regras de negócio |
| `VotoServiceTest` | Registro de votos, validações e apuração de resultado |
| `PautaControllerTest` | Contrato HTTP dos endpoints de pauta (`@WebMvcTest`) |
| `SessaoVotacaoControllerTest` | Contrato HTTP dos endpoints de sessão (`@WebMvcTest`) |
| `VotoControllerTest` | Contrato HTTP do endpoint de voto (`@WebMvcTest`) |
| `CpfValidationClientMockTest` | Comportamento do mock de validação de CPF |
| `VotacaoApplicationTests` | Smoke test de carregamento do contexto Spring (`@SpringBootTest`, H2) |

```bash
cd backend
./mvnw test
```

> Os testes rodam em **H2 em memória**, sem necessidade de PostgreSQL.

---

## 🗂️ Estrutura do Repositório

```
desafio-votacao-fullstack/
├── backend/                          # API REST (Java 17 + Spring Boot)
│   ├── docker-compose.yml            # PostgreSQL 16 via Docker Compose
│   ├── api-tests.http                # Coleção de requisições HTTP prontas
│   ├── pom.xml                       # Build Maven
│   └── src/
│       ├── main/java/com/dbserver/votacao/
│       │   ├── config/               # CORS e OpenAPI/Swagger
│       │   ├── controller/           # Pauta, SessaoVotacao e Voto
│       │   ├── dto/                  # request/ e response/ (contrato da API)
│       │   ├── exception/            # GlobalExceptionHandler + exceções de domínio
│       │   ├── model/                # Entidades JPA + enums
│       │   ├── repository/           # Spring Data JPA
│       │   ├── service/              # Regras de negócio
│       │   └── client/               # CpfValidationClient (interface + mock)
│       ├── main/resources/application.yml   # PostgreSQL (execução)
│       └── test/                     # Testes JUnit 5 + Mockito (H2)
├── frontend/                         # SPA (React 19 + TypeScript + Vite)
│   ├── Dockerfile                    # Build multi-stage → Nginx
│   ├── docker-compose.yml            # Serve a SPA na porta 3000
│   └── src/
│       ├── components/               # ui/, common/ e layout/
│       ├── config/                   # env, rotas, navegação
│       ├── hooks/                    # custom hooks
│       ├── pages/                    # GestaoPautas, VotacaoAssociado, PainelResultados...
│       ├── routes/                   # Router com lazy loading
│       ├── services/                 # Axios central + cliente HTTP
│       ├── stores/                   # Zustand por domínio
│       ├── types/                    # Tipos TypeScript (domínio, API, stores)
│       └── utils/                    # funções utilitárias
└── perf/                             # Testes de carga e performance (k6 + Docker)
    ├── load-test.js                  # Script k6 (stages + thresholds)
    ├── docker-compose.yml            # Container k6-runner
    └── Readme.md                     # Guia de execução e diagnóstico
```

---

## 🚀 Roadmap e Melhorias Futuras

- [ ] Migrations versionadas com **Flyway** (ou Liquibase) no lugar de `ddl-auto`
- [ ] **Dockerfile próprio do backend** + Compose completo (app + banco em orquestração)
- [ ] Integração **real** com serviço de validação de CPF (substituir o mock)
- [ ] **MapStruct** para mapeamento DTO ↔ Entidade
- [ ] Consultas de apuração otimizadas para cenários de **centenas de milhares de votos** (índices compostos/reporting)
- [ ] Autenticação e autorização (JWT/OAuth2)
- [ ] Testes e2e do frontend (ex.: Playwright/Cypress)

---

## 📄 Licença

Distribuído sob a licença **MIT**. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 📚 Leia também

| Documento | Descrição |
| --- | --- |
| [`backend/README.md`](backend/README.md) | Documentação detalhada da API (payloads completos, banco, erros) |
| [`frontend/README.md`](frontend/README.md) | Documentação do frontend (variáveis de ambiente, scripts, estrutura) |
| [`perf/Readme.md`](perf/Readme.md) | Guia de testes de carga, diagnósticos e resolução de gargalos |
| [`backend/api-tests.http`](backend/api-tests.http) | Coleção de requisições para validar o fluxo completo da API |

---

<p align="center">Feito com ☕ , ⚛️ e 🗳️ — Desafio Técnico · Fullstack (Spring Boot + React)</p>