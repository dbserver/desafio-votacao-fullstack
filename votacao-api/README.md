# Votacao API (Desafio Tecnico - Senior Java)

API REST para gerenciamento de pautas, sessoes de votacao e votos de associados.

Este README foi estruturado para facilitar avaliacao tecnica (execucao, arquitetura, regras de negocio, testes e operacao).

## 1) Stack e arquitetura

- Java 21
- Spring Boot 4
- Spring MVC
- Spring Data JPA
- Jakarta Validation
- H2 (file mode no perfil local)
- Maven
- JUnit 5 + Mockito

Arquitetura em camadas:

- `controller`: contrato HTTP
- `service`: regras de negocio
- `repository`: acesso a dados
- `dto`: contratos de entrada e saida
- `exception`: excecoes de dominio + tratamento global
- `config`: versionamento da API e CORS

## 2) Estrutura do projeto

```text
src/main/java/com/desafio/votacao_api
├── config
├── controller
├── dto
├── exception
├── model
├── repository
└── service

src/main/resources
├── application.properties
└── application-local.properties

src/test/java/com/desafio/votacao_api
├── integration
└── service
```

## 3) Como executar localmente

### Pre-requisitos

- Java 21+
- Maven 3.8+

### Build e execucao

```bash
cd /Users/tiagojordao/Documents/desafio-db/desafio-votacao-fullstack/votacao-api
./mvnw clean package
./mvnw spring-boot:run
```

API disponivel em:

- `http://localhost:8080/api/v1`

Perfil ativo por padrao:

- `local` (`spring.profiles.active=local`)

Banco local (persistente):

- `jdbc:h2:file:./data/votacao_local`

## 4) Endpoints

Base path versionado:

- `/api/v1`

### Pautas

- `POST /api/v1/pautas`
- `GET /api/v1/pautas`
- `GET /api/v1/pautas/{id}`
- `DELETE /api/v1/pautas/{id}`

### Sessoes

- `POST /api/v1/sessoes`
- `GET /api/v1/sessoes/{id}`
- `GET /api/v1/sessoes/pauta/{pautaId}`
- `GET /api/v1/sessoes/{sessaoVotacaoId}/results`

### Votos

- `POST /api/v1/votos`
- `GET /api/v1/votos/{id}`

## 5) Contratos de entrada (resumo)

### `POST /pautas`

- `name`: obrigatorio, 5-100 caracteres
- `description`: opcional, max 500

### `POST /sessoes`

- `pautaId`: obrigatorio (UUID)
- `durationInSeconds`: opcional, se informado deve ser > 0

### `POST /votos`

- `sessaoVotacaoId`: obrigatorio (UUID)
- `cpf`: obrigatorio, regex `\d{11}`
- `vote`: obrigatorio (`SIM` ou `NAO`)

## 6) Regras de erro e status HTTP

Principais status usados:

- `200 OK`
- `201 Created`
- `204 No Content`
- `400 Bad Request` (validacao/entrada invalida)
- `404 Not Found` (recurso nao encontrado / CPF inelegivel fake)
- `409 Conflict` (conflito de regra de negocio)
- `500 Internal Server Error`

Modelo padrao de erro:

```json
{
  "timestamp": "2026-03-23T14:00:00Z",
  "status": 409,
  "error": "Business Rule Conflict",
  "message": "Agenda with id ... already has a voting session.",
  "path": "/api/v1/sessoes"
}
```

## 7) Bônus do desafio

### Bonus 1 - Integracao externa (fake)

Implementado via `CpfValidationFacade` + adapter fake:

- Retornos aleatorios para CPF nao encontrado e apto/inapto a votar
- API converte para respostas HTTP de erro adequadas

### Bonus 2 - Performance

Ajustes aplicados no modelo:

- Indice em `voto(sessao_id)`
- Contagem agregada por query no repositorio (`countBy...`)
- Restricoes de unicidade para garantir integridade sem logica complexa

### Bonus 3 - Versionamento

Versionamento por prefixo configuravel em `api.base-path`:

- atual: `/api/v1`

## 8) Testes automatizados

Executar todos os testes:

```bash
cd /Users/tiagojordao/Documents/desafio-db/desafio-votacao-fullstack/votacao-api
./mvnw test
```

Cobertura atual de cenarios:

- Services (`PautaService`, `SessaoVotacaoService`, `VotoService`)
- Integracao de tratamento global de excecoes

## 9) Decisoes tecnicas e trade-offs

- Simplicidade priorizada (camadas claras, sem overengineering)
- Regras criticas protegidas em service + banco
- Erros centralizados no `GlobalExceptionHandler`
- Persistencia local com H2 file para facilitar avaliacao rapida

Trade-off conhecido:

- H2 local e adequado para desafio, mas para producao cloud recomenda-se banco gerenciado (PostgreSQL) e migracoes versionadas (Flyway/Liquibase)

---

