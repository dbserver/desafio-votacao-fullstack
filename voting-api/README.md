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

## Próximas melhorias (evolução futura)

* Autenticação com JWT
* Deploy em cloud (AWS ou Azure)
* Monitoramento com Prometheus + Grafana
* Testes de performance com JMeter
* Implementação de mensageria (Kafka ou RabbitMQ)
