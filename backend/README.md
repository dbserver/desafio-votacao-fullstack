# 🗳️ API do Sistema de Votação (Cooperativa)

> API RESTful em **Java + Spring Boot** para gerenciamento de **pautas**, controle de **sessões de votação com tempo limite** e **apuração centralizada** dos votos dos associados de uma cooperativa.

![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36?logo=apache-maven&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)
![H2](https://img.shields.io/badge/H2-Testes-FF6600)
![Swagger](https://img.shields.io/badge/OpenAPI_3-Swagger_UI-85EA2D)
![JUnit 5](https://img.shields.io/badge/JUnit_5-Mockito-25A162)

---

## 📑 Índice

- [📌 Visão Geral](#-visão-geral)
- [✨ Funcionalidades Principais](#-funcionalidades-principais)
- [🧱 Tech Stack e Tecnologias](#-tech-stack-e-tecnologias)
- [📡 Endpoints da API](#-endpoints-da-api)
- [📥 Exemplos de Payloads](#-exemplos-de-payloads)
- [🏛️ Arquitetura e Estrutura do Projeto](#️-arquitetura-e-estrutura-do-projeto)
- [▶️ Como Executar o Projeto](#️-como-executar-o-projeto)
- [🗄️ Configuração do Banco de Dados](#️-configuração-do-banco-de-dados)
- [⚠️ Tratamento de Erros](#️-tratamento-de-erros)
- [🪪 Validação de CPF (Mock)](#-validação-de-cpf-mock)
- [🧪 Testes Automatizados](#-testes-automatizados)
- [🚀 Roadmap e Melhorias Futuras](#-roadmap-e-melhorias-futuras)
- [📚 Referências e Arquivos Úteis](#-referências-e-arquivos-úteis)

---

## 📌 Visão Geral

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias por votação. Este projeto entrega o **backend** desse fluxo de forma simples e performática:

- 📋 **Cadastro de pautas** de deliberação
- ⏱️ **Abertura de sessões de votação** com duração configurável em minutos (ou **1 minuto** por default)
- 🗳️ **Registro de votos** (`SIM` / `NAO`) com validação de CPF/habilitação e **voto único por associado** em cada pauta
- 🏆 **Apuração centralizada** do resultado, com contagem de votos e status consolidado

A aplicação segue uma **arquitetura em camadas** (controller → service → repository), com documentação **OpenAPI 3 / Swagger UI**, **tratamento global de exceções**, suporte a **PostgreSQL** na execução e **H2** isolado nos testes automatizados.

---

## ✨ Funcionalidades Principais

| Domínio | Descrição |
| --- | --- |
| 📋 **Gestão de Pautas** | Cadastrar novas pautas e listar as existentes (`POST /v1/pautas`, `GET /v1/pautas`). |
| ⏱️ **Gestão de Sessões de Votação** | Abrir sessão de votação para uma pauta com duração customizada em minutos ou valor default (`POST /v1/sessoes/abrir`). |
| 🗳️ **Registro e Validação de Votos** | Registrar votos `SIM`/`NAO` com validação de CPF (`ABLE_TO_VOTE`/`UNABLE_TO_VOTE`), impedindo **votos duplicados** do mesmo associado na mesma pauta (`POST /v1/votos`). |
| 🏆 **Resultado e Apuração** | Endpoint dedicado que retorna `votosSim`, `votosNao`, `totalVotos` e o status consolidado (`SESSÃO EM ANDAMENTO`, `APROVADA`, `REPROVADA`, `EMPATE`) — `GET /v1/pautas/{pautaId}/resultado`. |
| 🛡️ **Tratamento Global de Exceções** | `@RestControllerAdvice` centralizado com corpo de erro padronizado (404, 400, 409 e 500). |
| 📖 **Documentação Interativa** | Swagger UI disponível em `/swagger-ui.html`. |

---

## 🧱 Tech Stack e Tecnologias

| Categoria | Tecnologia |
| --- | --- |
| ☕ Linguagem | Java 17 (compatível com JDK 17/21) |
| 🍃 Framework | Spring Boot 4.1.x — Spring Web/WebMVC, Spring Data JPA, Spring Validation |
| 🗄️ Banco de Dados | **PostgreSQL 16** (execução/apuração) · **H2** (testes automatizados) |
| ✨ Mapeamento / Boilerplate | Lombok |
| 📖 Documentação da API | springdoc-openapi 3.x → Swagger UI (`/swagger-ui.html`) e OpenAPI JSON (`/v3/api-docs`) |
| 🛡️ Tratamento de Erros | `@RestControllerAdvice` global — regras de negócio (`BusinessException`), validações `@Valid`, conflitos de integridade, 404 e 500 |
| 🧪 Testes | JUnit 5, Mockito, `@WebMvcTest` |
| 🧰 Build & Dependências | Maven 3.8+ (com Wrapper `./mvnw`) |
| 🌐 Integração Externa | `CpfValidationClient` com implementação **mock** para validação de CPF |
| 🐳 Infraestrutura | Docker Compose para o PostgreSQL local |

---

## 📡 Endpoints da API

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

---

## 📥 Exemplos de Payloads

### 📋 1. Cadastrar Pauta — `POST /v1/pautas`

**Request**

```json
{
  "titulo": "Aprovação do Orçamento Anual 2026",
  "descricao": "Votação para deliberar sobre a aprovação dos custos operacionais e investimentos da cooperativa."
}
```

**Response — `201 Created`**

```json
{
  "id": 1,
  "titulo": "Aprovação do Orçamento Anual 2026",
  "descricao": "Votação para deliberar sobre a aprovação dos custos operacionais e investimentos da cooperativa.",
  "dataCriacao": "2026-09-19T20:30:00"
}
```

### ⏱️ 2. Abrir Sessão de Votação — `POST /v1/sessoes/abrir`

Com duração customizada (**3 minutos**) ou sem `duracaoMinutos` (**default: 1 minuto**):

**Request**

```json
{
  "pautaId": 1,
  "duracaoMinutos": 3
}
```

**Response — `201 Created`**

```json
{
  "id": 1,
  "pautaId": 1,
  "tituloPauta": "Aprovação do Orçamento Anual 2026",
  "dataAbertura": "2026-09-19T20:00:00",
  "dataEncerramento": "2026-09-19T20:03:00",
  "aberta": true
}
```

### 🗳️ 3. Registrar Voto — `POST /v1/votos`

**Request**

```json
{
  "pautaId": 1,
  "associadoId": "12345678901",
  "voto": "SIM"
}
```

**Response — `201 Created`**

```json
{
  "id": 1,
  "pautaId": 1,
  "associadoId": "12345678901",
  "voto": "SIM",
  "dataVoto": "2026-09-19T20:45:00"
}
```

### 🏆 4. Resultado e Apuração — `GET /v1/pautas/{pautaId}/resultado`

**Response — `200 OK`**

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

**Possíveis valores de `resultado`:**

| Status | Significado |
| --- | --- |
| `SESSÃO EM ANDAMENTO` | A sessão ainda está aberta para receber votos |
| `APROVADA` | Sessão encerrada com mais votos `SIM` |
| `REPROVADA` | Sessão encerrada com mais votos `NAO` |
| `EMPATE` | Sessão encerrada com empate (ou sem votos) |

---

## 🏛️ Arquitetura e Estrutura do Projeto

O projeto segue princípios de **Arquitetura em Camadas**: os controllers expõem a API REST, os services concentram as **regras de negócio** e validações, e os repositories abstraem o acesso a dados (Spring Data JPA). DTOs isolam o contrato público da API das entidades de persistência, e o pacote `client` encapsula integrações externas (validação de CPF).

```
backend/
├── docker-compose.yml                 # PostgreSQL local via Docker Compose
├── pom.xml                            # Build Maven + dependências
└── src/
    ├── main/
    │   ├── java/com/dbserver/votacao/
    │   │   ├── config/                # Configurações do Spring (Cors, OpenAPI)
    │   │   ├── controller/            # Endpoints REST (PautaController, SessaoVotacaoController, VotoController)
    │   │   ├── dto/
    │   │   │   ├── request/           # PautaRequest, AbrirSessaoRequest, VotoRequest
    │   │   │   └── response/          # PautaResponse, SessaoVotacaoResponse, VotoResponse, ResultadoVotacaoResponse, CpfStatusResponse
    │   │   ├── exception/             # GlobalExceptionHandler, ErrorResponse e exceções de domínio
    │   │   ├── model/                 # Entidades JPA (Pauta, SessaoVotacao, Voto) + enums (OpcaoVoto, StatusVoto)
    │   │   ├── repository/            # Interfaces Spring Data JPA (PautaRepository, SessaoVotacaoRepository, VotoRepository)
    │   │   ├── service/               # Camada de regras de negócio (PautaService, SessaoVotacaoService, VotoService)
    │   │   └── client/                # Integração externa (CpfValidationClient + impl mock)
    │   └── resources/
    │       └── application.yml        # Configuração PostgreSQL (execução)
    └── test/
        ├── java/com/dbserver/votacao/ # Testes unitários e de fatias (@WebMvcTest)
        └── resources/
            └── application.yml        # Configuração H2 em memória (testes)
```

---

## ▶️ Como Executar o Projeto

### ✅ Pré-requisitos

- ☕ **Java 17** (ou 21) JDK
- 🧰 **Maven 3.8+** (ou utilize o Wrapper `./mvnw` incluído no projeto)
- 🐳 **Docker + Docker Compose** — para subir o PostgreSQL localmente
- (Opcional) **PostgreSQL 16** instalado localmente na porta `5432`

> 💡 **Sobre o banco:** a aplicação executa com **PostgreSQL**. Os **testes automatizados** continuam rodando em **H2 em memória**, sem necessidade de banco externo.

### 1️⃣ Subir o banco de dados (PostgreSQL via Docker Compose)

```bash
docker compose up -d
```

Isso cria o container `votacao-postgres` com as seguintes credenciais:

| Variável | Valor |
| --- | --- |
| Banco (`POSTGRES_DB`) | `votacao` |
| Usuário (`POSTGRES_USER`) | `votacao` |
| Senha (`POSTGRES_PASSWORD`) | `votacao` |
| Porta exposta | `5432` |

> Para encerrar: `docker compose down` (remova também o volume com `docker compose down -v` se quiser apagar os dados).

### 2️⃣ Compilar o projeto (inclui os testes em H2)

```bash
./mvnw clean install
```

> Os testes rodam em **H2 em memória** — nenhuma conexão com PostgreSQL é necessária nessa etapa.

### 3️⃣ Executar a aplicação

```bash
./mvnw spring-boot:run
```

> A aplicação sobe em `http://localhost:8080` conectada ao PostgreSQL (`jdbc:postgresql://localhost:5432/votacao`). O schema das tabelas é criado/atualizado automaticamente (`ddl-auto: update`).

### 4️⃣ Acessar a documentação

| Recurso | URL |
| --- | --- |
| 🌐 Swagger UI | `http://localhost:8080/swagger-ui.html` |
| 📄 OpenAPI JSON | `http://localhost:8080/v3/api-docs` |

> O arquivo **[`api-tests.http`](api-tests.http)** contém uma coleção pronta de requisições (criar pauta → abrir sessão → registrar votos → consultar resultado → cenários de erro) para testar o fluxo completo direto pela IDE (IntelliJ/VSCode) ou CLI.

---

## 🗄️ Configuração do Banco de Dados

### Dependência Maven (`pom.xml`)

Para usar PostgreSQL, basta adicionar o driver (a versão é gerenciada pelo BOM do Spring Boot):

```xml
<!-- PostgreSQL para execução/apuração (testes continuam em H2) -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Produção / Execução — `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/votacao
    username: votacao
    password: votacao
    driverClassName: org.postgresql.Driver
  h2:
    console:
      enabled: false
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
```

### Testes — `src/test/resources/application.yml` (H2 em memória)

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    driverClassName: org.h2.Driver
    username: sa
    password:
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
```

> O arquivo de configuração de testes sobrescreve o de produção durante `mvn test`, garantindo o isolamento total do banco nos testes.

---

## ⚠️ Tratamento de Erros

Todas as exceções são tratadas de forma centralizada pelo `GlobalExceptionHandler` (`@RestControllerAdvice`), com corpo de resposta padronizado.

| Status HTTP | Cenário | Exceção / Fonte |
| --- | --- | --- |
| `400` | Regra de negócio violada (voto duplicado, sessão encerrada, associado inabilitado) | `BusinessException` |
| `400` | Validação `@Valid` — campos obrigatórios ausentes ou inválidos | `MethodArgumentNotValidException` |
| `404` | Recurso não encontrado (pauta/sessão inexistente) ou CPF inválido | `ResourceNotFoundException` |
| `409` | Violação de unicidade no banco (voto duplicado na mesma pauta) | `DataIntegrityViolationException` |
| `500` | Erro interno inesperado | `Exception` (genérico) |

**Exemplo de corpo de erro (validação `@Valid`) — `400 Bad Request`:**

```json
{
  "timestamp": "2026-09-19T20:45:00",
  "status": 400,
  "message": "Falha na validação dos campos da requisição.",
  "path": "/v1/votos",
  "errors": {
    "pautaId": "O ID da pauta é obrigatório.",
    "voto": "A opção de voto (SIM/NAO) é obrigatória."
  }
}
```

> O campo `errors` (mapa campo → mensagem) só é incluído quando há falhas de validação de campos.

---

## 🪪 Validação de CPF (Mock)

A integração com o serviço externo de validação de CPF é feita pela interface `CpfValidationClient`, com uma implementação **mock** (`CpfValidationClientMock`) que simula o comportamento:

| Entrada (CPF) | Comportamento |
| --- | --- |
| Menos de 11 dígitos | ❌ `404 Not Found` — *"CPF inválido ou não encontrado na base federal"* |
| 11 dígitos e último dígito **par** | ✅ Status `ABLE_TO_VOTE` — associado habilitado a votar |
| 11 dígitos e último dígito **ímpar** | 🚫 Status `UNABLE_TO_VOTE` — voto recusado com `400` |

> Para fins de exercício, a validação é simulada. A interface permite trocar o mock por uma integração real (ex.: serviço de CPF na nuvem) sem alterar a camada de negócio.

---

## 🧪 Testes Automatizados

A suíte utiliza **JUnit 5 + Mockito**, com testes unitários de serviços, testes de fatias de controller (`@WebMvcTest`) e testes do mock de CPF:

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

Execute com:

```bash
./mvnw test
```

---

## 🚀 Roadmap e Melhorias Futuras

- [ ] Migrations versionadas com **Flyway** (ou Liquibase) no lugar de `ddl-auto`
- [ ] **Dockerfile** da aplicação + Compose completo (app + banco em orquestração)
- [ ] Integração **real** com serviço de validação de CPF (substituir o mock)
- [ ] **MapStruct** para mapeamento DTO ↔ Entidade
- [ ] Testes de **performance** (centenas de milhares de votos)
- [ ] Autenticação e autorização (JWT/OAuth2)

---

## 📚 Referências e Arquivos Úteis

| Arquivo/Recurso | Descrição |
| --- | --- |
| [`api-tests.http`](api-tests.http) | Coleção de requisições HTTP para validar o fluxo completo da API |
| [`docker-compose.yml`](docker-compose.yml) | Infraestrutura local do PostgreSQL |
| `/swagger-ui.html` | Documentação interativa da API (OpenAPI 3) |
| `/v3/api-docs` | Spec OpenAPI em JSON |

---

<p align="center">Feito com ☕ e 🗳️ — Desafio Técnico · Backend Java/Spring Boot</p>