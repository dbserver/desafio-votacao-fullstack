# API de Votação

API REST para gerenciamento de sessões de votação em assembleias.

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.4.2
- PostgreSQL
- Redis
- Docker
- Maven

## 📋 Pré-requisitos

- Java 17+
- Docker e Docker Compose
- Maven 3.6+

## 🔧 Instalação

1. Clone o repositório:

```bash
git clone https://github.com/seu-usuario/votacao-api.git
cd votacao-api
```
2. Execute o Docker Compose para iniciar os serviços necessários:

```bash
docker-compose up -d
```

3. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

## 📚 Documentação da API

A documentação completa da API está disponível através do Swagger UI:

```bash
http://localhost:8080/swagger-ui.html
```
### Endpoints

#### Pautas

##### Criar nova pauta

```http
POST /api/v1/agendas
```

Request body:

```json
{
"title": "string",
"description": "string"
}
```

#### Sessões de Votação

##### Abrir sessão de votação

```http
POST /api/v1/agendas/{agendaId}/sessions
```

Query parameters:
- `durationMinutes` (opcional): Duração da sessão em minutos (default: 1)

#### Votos

##### Registrar voto

```http
POST /api/v1/sessions/{sessionId}/votes
```

```json
{
"associateId": "12345678900",
"vote": true
}
```

##### Obter resultado da votação

```http
GET /api/v1/sessions/{sessionId}/result
```

## 🏗️ Arquitetura

A aplicação segue uma arquitetura em camadas:

- **Controllers**: Responsáveis pelo recebimento das requisições HTTP
- **Services**: Implementação das regras de negócio
- **Repositories**: Camada de acesso aos dados
- **Models**: Entidades do domínio
- **DTOs**: Objetos de transferência de dados

### Cache

- Utiliza Redis para cache de resultados e controle de concorrência
- Implementação de batch processing para votos
- Cache distribuído para alta disponibilidade

### Banco de Dados

- PostgreSQL para persistência dos dados
- Índices otimizados para consultas de votação
- Connection pool com Hikari

## 🧪 Testes

Para executar os testes:

```http
./mvnw test
```
## 📄 Licença

Este projeto está sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🤝 Contribuindo

1. Faça o fork do projeto
2. Crie sua feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## ⚙️ Configuração

As principais configurações estão no arquivo `application.properties`:

- Banco de dados
- Redis
- Server
- Swagger/OpenAPI
- Connection Pool
- JPA/Hibernate

## 📦 Deploy

A aplicação pode ser containerizada usando o Dockerfile fornecido. Para construir e executar:


```bash
docker build -t votacao-api .
docker run -p 8080:8080 votacao-api
```

## 🔍 Monitoramento

A aplicação expõe endpoints do Spring Actuator para monitoramento:

```http
http://localhost:8080/actuator
```

## 📞 Suporte

Em caso de dúvidas ou problemas, abra uma issue no repositório ou contate o time de desenvolvimento em dev@empresa.com.
