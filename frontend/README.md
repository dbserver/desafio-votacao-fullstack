# 🗳️ Sistema de Votação (Cooperativa) — Frontend

![React 19](https://img.shields.io/badge/React-19-%2361DAFB?logo=react&logoColor=white&style=for-the-badge)
![TypeScript](https://img.shields.io/badge/TypeScript-%20estrito%20-%233178C6?logo=typescript&logoColor=white&style=for-the-badge)
![Vite 8](https://img.shields.io/badge/Vite-8-%23646CFF?logo=vite&logoColor=white&style=for-the-badge)
![Zustand 5](https://img.shields.io/badge/Zustand-5-%235B3A29?style=for-the-badge)
![Tailwind CSS v4](https://img.shields.io/badge/Tailwind%20CSS-v4-%2306B6D4?logo=tailwindcss&logoColor=white&style=for-the-badge)
![React Router v7](https://img.shields.io/badge/React%20Router-v7-%23CA4245?logo=reactrouter&logoColor=white&style=for-the-badge)
![Axios](https://img.shields.io/badge/Axios-%235A29E4?logo=axios&logoColor=white&style=for-the-badge)
![Docker](https://img.shields.io/badge/Docker-%232496ED?logo=docker&logoColor=white&style=for-the-badge)
![Nginx](https://img.shields.io/badge/Nginx-%23269539?logo=nginx&logoColor=white&style=for-the-badge)
![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-%23FE5196?logo=conventionalcommits&logoColor=white&style=for-the-badge)
![Licença MIT](https://img.shields.io/badge/Licen%C3%A7a-MIT-yellow.svg?style=for-the-badge)

---

## 📖 Visão Geral

Interface web moderna desenvolvida para **associados e administradores** de uma cooperativa. A aplicação permite **registrar pautas**, **abrir sessões de votação com tempo delimitado**, **votar (Sim/Não)** e **acompanhar a apuração dos resultados** em tempo real, com gráficos, barras de progresso e badges de status.

O projeto segue o princípio do cooperativismo: *cada associado possui um voto*. A solução consome uma API REST (Spring Boot) versionada em `/v1` — veja o [repositório do desafio](https://github.com) e o `README.md` da raiz para o contexto completo do backend.

> ⚠️ **Atenção**: este repositório contém apenas o **frontend**. O backend Spring Boot vive em `../backend`.

---

## ✨ Funcionalidades

| | Módulo | Descrição |
|---|---|---|
| 🗒️ | **Cadastro de Pautas** | Criação de pautas para deliberação da cooperativa, com título, descrição e data de criação. |
| ⏱️ | **Abertura de Sessão** | Abertura de sessão de votação com **duração parametrizada em minutos** (contagem regressiva do tempo). |
| 🗳️ | **Registro de Voto** | Validação do CPF/ID do associado, seleção da pauta em votação e do voto (**Sim / Não**), com prevenção de votos duplicados e **tratamento amigável dos erros do backend**. |
| 📊 | **Apuração e Resultados** | Exibição dinâmica de votos **SIM**, **NÃO** e **total**, com porcentagens visuais em **barras de progresso** e badges de status da pauta (Em andamento, Aprovada, Reprovada, Empate). |

---

## 🧱 Tecnologias e Stack

| Camada | Tecnologia |
| ------ | ---------- |
| **Linguagem & Core** | React 19 · TypeScript (estrito, com *type guards* para erros `unknown`) · Vite 8 |
| **Estado global** | Zustand 5 — stores separadas por domínio: `usePautaStore`, `useSessaoStore`, `useVotoStore` |
| **Roteamento** | React Router v7 com **Lazy Loading / Code Splitting** |
| **Estilização & UI** | Tailwind CSS v4 (CSS-first) · Lucide React (ícones) · componentes utilitários reutilizáveis (`FeedbackMessage`, `LoadingSpinner`, `BadgeStatus`, `ProgressBar`) |
| **Comunicação HTTP** | Axios com instância central e interceptors de tratamento de exceções |
| **Infra & Deploy** | Docker · Dockerfile multi-stage · Nginx (servindo os assets estáticos da SPA) · Docker Compose |
| **Qualidade** | ESLint (flat config) · Prettier · Conventional Commits |

---

## 🗂️ Estrutura de Pastas

```
src/
├── assets/            # recursos estáticos (fontes, imagens)
├── components/        # componentes reutilizáveis
│   ├── ui/            # primitivas de UI (BadgeStatus, FeedbackMessage, LoadingSpinner, ...)
│   ├── common/        # componentes de domínio (PautaCard, PautaForm, ModalAbrirSessao)
│   └── layout/        # estrutura da aplicação (AppLayout, Header, Sidebar)
├── config/            # constantes globais (env, rotas, navegação)
├── hooks/             # custom hooks (ex.: useOnlineStatus)
├── pages/             # telas da aplicação (GestaoPautas, VotacaoAssociado, PainelResultados, ...)
├── routes/            # configuração do router (index.tsx, com lazy loading)
├── services/          # instância do Axios e cliente HTTP
├── stores/            # stores Zustand (usePautaStore, useSessaoStore, useVotoStore)
├── types/             # interfaces e tipos TypeScript (domain.ts, api.ts, tipagens das stores)
└── utils/             # funções utilitárias (cn, format)
```

---

## 🚀 Como Executar

### Pré-requisitos

- **Node.js 20+** (o `package.json` exige `>=20.19.0`)
- **npm 10+**
- **Docker & Docker Compose** *(opcional — para execução em container)*

### Execução Local (Desenvolvimento)

1. Clone o repositório:

   ```bash
   git clone <url-do-repositorio>
   cd frontend
   ```

2. Instale as dependências:

   ```bash
   npm install
   ```

3. Configure as variáveis de ambiente (opcional — há padrões):

   ```bash
   cp .env.example .env
   ```

4. Suba o servidor de desenvolvimento:

   ```bash
   npm run dev
   ```

   A aplicação ficará disponível em **http://localhost:5173**.

> 💡 Para o fluxo completo, o backend Spring Boot deve estar rodando em `http://localhost:8080` (veja as instruções do backend na raiz do repositório).

### Execução via Docker

A imagem é construída com **Dockerfile multi-stage** (build `node:20-alpine` → serve `nginx:alpine`), pronta para produção:

```bash
docker compose up -d --build
```

Acesse a aplicação em **http://localhost:3000** 🎉

A base URL da API é injetada **em tempo de build** (variáveis `VITE_*` são resolvidas pelo Vite na geração dos assets). Para apontar para outro backend, ajuste o `build.args` do `docker-compose.yml`:

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

### Build de Produção (preview local)

```bash
npm run build      # typecheck + build → gera a pasta dist/
npm run preview    # serve o build localmente
```

---

## ⚙️ Variáveis de Ambiente

| Variável | Descrição | Padrão |
| -------- | --------- | ------ |
| `VITE_API_BASE_URL` | Base URL da API REST (backend Spring Boot), inclui o versionamento `/v1` | `http://localhost:8080/v1` |

---

## 📜 Scripts

| Comando | Descrição |
| ------- | --------- |
| `npm run dev` | Servidor de desenvolvimento (HMR) |
| `npm run build` | `tsc -b` (checagem de tipos) + build de produção (Vite) |
| `npm run preview` | Pré-visualização local do build de produção |
| `npm run typecheck` | Checagem de tipos isolada (`tsc --noEmit`) |
| `npm run lint` / `npm run lint:fix` | ESLint (flat config) |
| `npm run format` / `npm run format:check` | Prettier (escreve / verifica) |

---

## 🔌 Integração com a API

| Método | Endpoint | Descrição |
| ------ | -------- | --------- |
| `GET` | `/v1/pautas` | Lista as pautas cadastradas |
| `POST` | `/v1/pautas` | Cadastra uma nova pauta `{ titulo, descricao }` |
| `GET` | `/v1/pautas/{id}/resultado` | Obtém o resultado da votação de uma pauta |
| `GET` | `/v1/sessoes` | Lista todas as sessões de votação |
| `POST` | `/v1/sessoes/abrir` | Abre uma sessão `{ pautaId, duracaoMinutos }` |
| `POST` | `/v1/votos` | Registra um voto `{ pautaId, associadoId, voto: 'SIM' \| 'NAO' }` |

---

## 🤝 Padrões e Convenções de Código

- **TypeScript estrito** (`strict: true`) com `verbatimModuleSyntax`, `import type` e *type guards* (`err instanceof Error`) ao tratar erros `unknown`.
- **Zustand** com stores tipadas — state + actions definidos em `types/*Store.ts`.
- **Conventional Commits** (`feat(scope): ...`, `fix(scope): ...`) — padronização aplicada no histórico do repositório.
- **ESLint** (flat config) + **Prettier** integrados aos scripts do projeto (`lint`, `format`).

---

## 📄 Licença

Distribuído sob a licença **MIT**. Veja o arquivo [LICENSE](../LICENSE) para mais detalhes.