# Voting API - Sistema de Votação para Cooperativas

API REST desenvolvida em **Java 17 + Spring Boot** para gerenciamento de sessões de votação em cooperativas.

O sistema permite cadastrar pautas, abrir sessões de votação, receber votos de associados e contabilizar o resultado da votação de forma performática e escalável.

---

## Tecnologias utilizadas

* Java 17
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Flyway (migrations)
* Docker
* Docker Compose
* Swagger / OpenAPI
* H2 Database (testes)
* Maven
* JUnit 5

---

## Arquitetura do Projeto

O projeto foi desenvolvido seguindo boas práticas de arquitetura em APIs REST:

```
controller
service
repository
entity
dto
external
exception
config
```

Princípios utilizados:

* Separação de responsabilidades
* DTO desde o início do projeto
* Uso de record (Java 17)
* Tratamento global de exceções
* Código limpo e organizado
* Versionamento de API
* Estrutura preparada para evolução futura

---

## Funcionalidades da API

A API permite:

* Cadastrar uma nova pauta
* Abrir uma sessão de votação para uma pauta
* Receber votos de associados
* Impedir votos duplicados
* Encerrar sessões automaticamente
* Contabilizar votos (Sim / Não)
* Retornar resultado da votação
* Integração com serviço externo fake para validação de CPF
* Suporte a alto volume de votos

---

## Versionamento da API

A API foi versionada utilizando prefixo na URL:

```
/api/v1
```

Exemplo:

```
POST /api/v1/agenda
POST /api/v1/agenda/{id}/session
POST /api/v1/votos
GET  /api/v1/agenda/{id}/result
```

---

## Como executar o projeto

### Pré-requisitos

* Docker
* Docker Compose
* Java 17
* Maven

---

### 1) Entrar na pasta do projeto

```
cd voting-api
```

---

### 2) Gerar o jar da aplicação

```
mvn clean package
```

---

### 3) Subir a aplicação com Docker

```
docker-compose up --build
```

---

## Acessar a documentação da API

Após subir o projeto:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Banco de dados

O projeto utiliza:

* PostgreSQL em ambiente Docker
* H2 Database para testes automatizados

As migrations são gerenciadas com Flyway.

---

## Performance

A API foi preparada para cenários com alto volume de votos.

Melhorias aplicadas:

* Índice no banco de dados
* Constraint para evitar votos duplicados
* Query otimizada para contagem de votos
* Contagem feita diretamente no banco (sem carregar todos os registros na memória)

---

## Integração com serviço externo (Bônus)

Foi implementado um client fake para simular validação de CPF.

O serviço retorna:

* ABLE_TO_VOTE
* UNABLE_TO_VOTE

Isso permite simular integração com sistemas externos.

---

## Testes

O projeto possui:

* Testes unitários
* Testes de service
* Testes utilizando H2 Database

---

## Boas práticas aplicadas

* Conventional Commits
* Versionamento semântico
* Código limpo
* DTO desde o início
* Record (Java 17)
* Arquitetura em camadas
* Tratamento global de exceções
* Documentação com Swagger
* Docker para execução simples

---

# 🗳️ Voting Frontend

Sistema de votação moderno e responsivo desenvolvido com React e TypeScript. Permite que usuários registrem votos em pautas compartilhadas com interface intuitiva e segura.

## 📋 Descrição

O **Voting Frontend** é a interface web para um sistema de votação distribuído. Permite:

- 📝 Visualizar pautas de votação em tempo real
- 🗳️ Registrar votos anônimos e seguros
- ⏱️ Acompanhar o tempo restante da sessão de votação
- 📊 Compartilhar links de votação via clipboard
- 🔒 Validações de segurança (CPF único por voto)
- 📱 Interface responsiva para desktop e mobile

## 🛠️ Tecnologias Utilizadas

- **React** (19.2.4) - Framework UI
- **TypeScript** (5.9.3) - Tipagem estática
- **Vite** (8.0.1) - Bundler e dev server
- **React Router DOM** (7.13.1) - Roteamento
- **Tailwind CSS** (4.2.2) - Estilização
- **Axios** (1.13.6) - Cliente HTTP
- **ESLint** (9.39.4) - Linting e qualidade de código

## 📋 Pré-requisitos

Antes de começar, certifique-se de que você tem instalado:

- **Node.js** (versão 16.0.0 ou superior)
- **npm** (versão 7.0.0 ou superior)

## 🚀 Instalação

1. **Clone o repositório ou extraia os arquivos:**

```bash
cd voting-frontend
```

2. **Instale as dependências:**

```bash
npm install
```

## 💻 Executando Localmente

### Modo Desenvolvimento

Para iniciar o servidor de desenvolvimento com Hot Module Replacement (HMR):

```bash
npm run dev
```

O aplicativo estará disponível em:
- **http://localhost:5173** (padrão Vite)
- Ou a URL exibida no terminal

### Build para Produção

Para compilar a aplicação para produção:

```bash
npm run build
```

Os arquivos otimizados serão gerados na pasta `dist/`.

### Preview de Produção

Para visualizar o build de produção localmente:

```bash
npm run preview
```

### Linting

Para verificar a qualidade do código:

```bash
npm run lint
```

## 📁 Estrutura do Projeto

```
src/
├── components/      # Componentes reutilizáveis
├── layouts/         # Layouts da aplicação
├── pages/           # Páginas principais
├── routes/          # Configuração de rotas
├── services/        # Serviços (API, lógica)
├── types/           # Tipos TypeScript
├── styles/          # Estilos globais
└── App.tsx          # Componente raiz
```

## 🔗 Endpoints da API

A aplicação se conecta com os seguintes serviços:

- **Obter Pauta**: `GET /api/agendas/:id`
- **Registrar Voto**: `POST /api/votes`
- **Obter Resultado**: `GET /api/votes/result/:agendaId`

> Configure a URL base da API no arquivo `src/services/api.ts`

## ⚙️ Configuração

### Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto (opcional):

```env
VITE_API_URL=http://localhost:8080
```

> Se não configurado, a aplicação usa a URL padrão definida em `src/services/api.ts`

## 🧪 Funcionalidades Principais

- ✅ Listagem de pautas
- ✅ Detalhes da pauta com timer em tempo real
- ✅ Registro de voto com validação de CPF
- ✅ Modal de confirmação de voto
- ✅ Compartilhamento de link de votação
- ✅ Exibição de resultado de votos
- ✅ Sessão de votação com tempo limite

