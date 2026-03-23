# Desafio Técnico - Sistema de Votação (Java + React)

Implementação completa de uma solução de votação para cooperativas, com backend em Spring Boot, frontend em React e evidências de qualidade (testes, tratamento de erros, versionamento de API e teste de carga).

## Visão geral

O sistema atende os fluxos principais do desafio:

- Cadastro de pautas.
- Abertura de sessão de votação por pauta (duração configurável, com default no backend).
- Registro de votos por associado (`SIM`/`NAO`) com controle de voto único por pauta.
- Apuração e exibição do resultado consolidado.

Também contempla os bônus técnicos:

- Integração externa fake para validação de CPF/elegibilidade.
- Teste de performance com k6.
- Estratégia de versionamento de API.

## Arquitetura da solução

### Backend (Spring Boot)

- Linguagem e stack: Java 21, Spring Boot, Spring MVC, Spring Data JPA, Jakarta Validation, Maven.
- Persistência local em H2 modo arquivo para manter dados entre reinícios.
- Organização em camadas: `controller`, `service`, `repository`, `dto`, `exception`, `config`.
- Tratamento global de exceções com payload padronizado para facilitar observabilidade e consumo no frontend.

Referência detalhada: [votacao-api/README.md](votacao-api/README.md)

### Frontend (React)

- Stack: React 19, TypeScript, Vite, React Router.
- Organização por domínio (`features/pautas`, `features/sessoes`, `features/votacao`) e componentes compartilhados.
- Client HTTP centralizado e feedback de erro por toast com priorização de mensagens de negócio da API.
- Layout responsivo e fluxo funcional completo de cadastro, abertura de sessão, votação e consulta de resultado.

Referência detalhada: [votacaoSpa/README.md](votacaoSpa/README.md)

## Qualidade e robustez

- Testes automatizados no backend (camada de serviço e integração de tratamento de exceções).
- Testes no frontend para componentes e utilitários críticos.
- Regras de negócio protegidas em múltiplas camadas: validação de entrada, serviços e restrições de persistência.
- API versionada por prefixo configurável (`/api/v1`), reduzindo risco de quebra para consumidores.

## Bônus 1 - Integração com sistema externo fake

Foi implementada uma facade/client fake para simular validação externa de CPF e elegibilidade de voto.

Comportamento aplicado:

- CPF inválido/inelegível pode resultar em bloqueio com `404 Not Found` (conforme regra do desafio).
- CPF elegível segue para processamento de voto.

Esse desenho valida resiliência do fluxo e tratamento de respostas não determinísticas de dependências externas.

## Bônus 2 - Performance e estresse

O backend foi preparado para cenário de alta concorrência com foco em throughput, latência e integridade de regra de negócio.

Otimizações principais:

- Indexação estratégica na tabela de votos.
- Ajustes de pool de conexões (HikariCP) para absorção de picos de gravação.

Cenário executado com k6:

- 100 VUs por 30 segundos.
- Alvo: `POST /api/v1/votos`.

Evidências observadas (run documentado):

- Alta taxa de processamento sustentada.
- Latência baixa (média e p95 em milissegundos).
- Ausência de erro interno 500 no cenário descrito.
- Distribuição de respostas coerente com a regra fake de elegibilidade (aprox. 75% bloqueio / 25% sucesso).

Referência detalhada: [teste-de-carga/README.md](teste-de-carga/README.md)

## Bônus 3 - Versionamento da API

Estratégia adotada: versionamento por URI (`/api/v1`).

Motivos da escolha:

- Simplicidade operacional.
- Clareza para consumidores.
- Facilidade para coexistência de versões (`/v1`, `/v2`) durante evolução.

## Como executar o projeto completo

### Pré-requisitos

- Java 21+
- Node.js 22+
- npm 10+

### 1. Subir backend

```bash
cd votacao-api
./mvnw clean package
./mvnw spring-boot:run
```

API base: `http://localhost:8080/api/v1`

### 2. Subir frontend (Verificar versao Node.js 22+)

```bash
cd votacaoSpa
npm install
```

`.env` já definida com objetivo de facilitar para o avaliador:

Execute:

```bash
npm start
```

Frontend: `http://localhost:5173`

### 3. Executar testes

Backend:

```bash
cd votacao-api
./mvnw test
```

Frontend:

```bash
cd votacaoSpa
npm run test:run
```

### 4. Executar teste de carga (opcional)

```bash
cd teste-de-carga
k6 run load-test.js
```

Antes de executar, defina manualmente o `sessaoVotacaoId` em `load-test.js` com um ID válido no ambiente.

## Endpoints principais

- `POST /api/v1/pautas`
- `GET /api/v1/pautas`
- `POST /api/v1/sessoes`
- `GET /api/v1/sessoes/pauta/{pautaId}`
- `GET /api/v1/sessoes/{sessaoVotacaoId}/results`
- `POST /api/v1/votos`

## Decisões técnicas relevantes

- Prioridade em simplicidade e legibilidade, evitando overengineering.
- Separação clara de responsabilidades entre camadas e entre frontend/back.
- Persistência local resiliente para avaliação rápida sem dependências externas obrigatórias.
- Foco em comportamento determinístico de erros e mensagens para facilitar diagnóstico.
- Estratégia de escalabilidade já considerada no desenho de apuração e votação.

## Estrutura do repositório

- [votacao-api](votacao-api): API Java Spring Boot.
- [votacaoSpa](votacaoSpa): Aplicação React.
- [teste-de-carga](teste-de-carga): Script e documentação de carga com k6.

## Conclusão

Esta entrega foi construída para equilibrar simplicidade de design, qualidade de engenharia e robustez operacional, cobrindo integralmente o escopo funcional do desafio e os bônus com evidências práticas de execução.
