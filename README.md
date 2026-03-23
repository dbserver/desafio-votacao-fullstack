# Desafio Votação - Fullstack

Sistema web para gerenciamento e participação em sessões de votação de cooperativas, construído com Java Spring Boot e React.

## Tecnologias Utilizadas

### Backend

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- PostgreSQL (produção) / H2 (desenvolvimento/testes)
- Bean Validation
- Springdoc OpenAPI (Swagger UI)
- Lombok
- JUnit 5 + Mockito + AssertJ

### Frontend

- React 18 + TypeScript
- Material UI 5
- React Router 6
- Axios
- Vite

### Infraestrutura

- Docker + Docker Compose

---

## Como Executar

### Opção 1: Docker Compose (recomendado)

Sobe backend, frontend e PostgreSQL de uma vez:

```bash
docker-compose up --build
```

| Serviço  | URL                                   |
| -------- | ------------------------------------- |
| Frontend | http://localhost:3000                 |
| Backend  | http://localhost:8080                 |
| Swagger  | http://localhost:8080/swagger-ui.html |

### Opção 2: Execução Local

#### Backend

Requisitos: **Java 17+**, **Maven 3.9+**

```bash
cd backend

# Com banco H2 (sem necessidade de instalar PostgreSQL)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Com PostgreSQL (configurar application.yml)
mvn spring-boot:run
```

O backend estará disponível em `http://localhost:8080`.

#### Frontend

Requisitos: **Node.js 18+**

```bash
cd frontend
npm install
npm run dev
```

O frontend estará disponível em `http://localhost:3000`.

---

## API Endpoints

Base URL: `/api/v1`

| Método | Endpoint                            | Descrição               |
| ------ | ----------------------------------- | ----------------------- |
| POST   | `/pautas`                           | Cadastrar nova pauta    |
| GET    | `/pautas`                           | Listar todas as pautas  |
| GET    | `/pautas/{id}`                      | Buscar pauta por ID     |
| POST   | `/sessoes`                          | Abrir sessão de votação |
| GET    | `/sessoes/pauta/{pautaId}`          | Buscar sessão por pauta |
| POST   | `/pautas/{pautaId}/votos`           | Registrar voto          |
| GET    | `/pautas/{pautaId}/votos/resultado` | Resultado da votação    |

Documentação interativa: `http://localhost:8080/swagger-ui.html`

### Exemplos de Requisições

**Criar Pauta:**

```json
POST /api/v1/pautas
{
  "titulo": "Aprovação do orçamento 2024",
  "descricao": "Votação para aprovação do orçamento anual"
}
```

**Abrir Sessão (duração em minutos, padrão: 1):**

```json
POST /api/v1/sessoes
{
  "pautaId": 1,
  "duracaoMinutos": 5
}
```

**Registrar Voto:**

```json
POST /api/v1/pautas/1/votos
{
  "associadoId": "12345678909",
  "voto": "SIM"
}
```

---

## Testes

```bash
cd backend
mvn test
```

Os testes cobrem:

- **Unitários**: Services (PautaService, SessaoVotacaoService, VotoService)
- **Integração**: Controllers com MockMvc e banco H2
- **Cenários**: criação, validação, erros de negócio, resultados

---

## Decisões Técnicas

### Arquitetura

Arquitetura em camadas (Controller → Service → Repository), adequada à complexidade do projeto. Mantém boa separação de responsabilidades sem over-engineering.

### Persistência

PostgreSQL para produção por ser robusto e confiável. Profile `dev` com H2 file-based para desenvolvimento local sem dependências externas. Os dados persistem entre restarts em ambos os cenários.

### Controle de Sessão de Votação

A verificação se a sessão está aberta é feita comparando `LocalDateTime.now()` com o `dataFim` da sessão. Mais simples e confiável que usar schedulers, sem estado mutável adicional.

### DTOs com Java Records

Utilização de Records do Java para os DTOs, reduzindo boilerplate e tornando as classes imutáveis por padrão.

### Validação de CPF (Bônus 1)

Client fake (`CpfValidationClient`) que:

1. Valida o formato do CPF pelo algoritmo de dígitos verificadores
2. CPF com formato inválido → HTTP 404
3. CPF válido → retorna aleatoriamente `ABLE_TO_VOTE` ou `UNABLE_TO_VOTE`

### Performance (Bônus 2)

- Índice composto em `(sessao_id, voto)` para otimizar contagem de votos
- Constraint unique em `(sessao_id, associado_id)` garante unicidade e cria índice
- Consultas com `COUNT` direto no banco ao invés de carregar registros em memória
- Fetch lazy em relacionamentos para evitar queries desnecessárias

### Versionamento da API (Bônus 3)

Estratégia por **URL path** (`/api/v1/`). Escolhida por ser:

- Explícita e simples de entender
- Facilita coexistência de múltiplas versões
- Boa visibilidade para monitoramento e roteamento
- Padrão amplamente adotado no mercado

Alternativas consideradas:

- **Header versioning**: menos visível na URL, dificulta testes via browser
- **Query parameter**: não semântico para versionamento
- **Content negotiation**: complexidade desnecessária para este cenário

---

## Estrutura do Projeto

```
├── backend/
│   ├── src/main/java/com/dbserver/votacao/
│   │   ├── client/          # Client fake de validação de CPF
│   │   ├── config/          # Configurações (CORS, OpenAPI)
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/             # Records de request/response
│   │   ├── entity/          # Entidades JPA
│   │   ├── enums/           # Enumerações
│   │   ├── exception/       # Exceções e handler global
│   │   ├── repository/      # Repositórios Spring Data
│   │   └── service/         # Regras de negócio
│   ├── src/test/            # Testes unitários e de integração
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── src/
│   │   ├── components/      # Componentes reutilizáveis
│   │   ├── pages/           # Páginas da aplicação
│   │   ├── services/        # Serviços de API
│   │   └── types/           # Tipagens TypeScript
│   ├── package.json
│   └── Dockerfile
├── docker-compose.yml
└── README.md
```
