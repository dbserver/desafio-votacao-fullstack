🗳️ Desafio Votação — Backend

API REST desenvolvida em Java Spring Boot, responsável por gerenciar um sistema de votação simples.
Permite criar pautas, abrir sessões, registrar votos e consultar resultados.

🚀 Funcionalidades

📝 Criar Pauta: Registrar uma nova pauta de votação.

🕒 Abrir Sessão: Iniciar uma sessão de votação para uma pauta existente.

✅ Registrar Voto: Registrar votos “Sim” ou “Não” em uma sessão.

📊 Consultar Resultado: Exibir o total de votos por sessão.

🧰 Tecnologias Utilizadas

Tecnologia	Descrição

☕ Java 17	Linguagem principal do projeto

🌱 Spring Boot 3	Framework para criação da API REST

🧱 Gradle	Gerenciador de dependências e build

🐘 PostgreSQL	Banco de dados relacional

📘 Swagger UI	Documentação e teste rápido dos endpoints

💡 Lombok	Redução de código repetitivo (getters, builders, etc.)

🏗️ Arquitetura do Projeto

O projeto segue uma estrutura inspirada na Clean Architecture, com separação clara de responsabilidades:

📦 votacao-backend

┣ 📂 application

    ┃ ┣ 📂 dto              → Objetos de transferência de dados (DTOs)
    
    ┃ ┗ 📂 usecase          → Casos de uso e lógica de aplicação

┣ 📂 domain

    ┃ ┣ 📂 model            → Entidades de domínio (Pauta, SessaoVotacao, Voto)
    
    ┃ ┗ 📂 service          → Serviços e regras de negócio

┣ 📂 infrastructure

    ┃ ┗ 📂 repository       → Repositórios JPA e persistência

┣ 📂 controller          → Endpoints REST e configuração Swagger

┗ 📜 VotacaoApplication  → Classe principal da aplicação


🎯 Benefícios

Separação clara de camadas

Facilita testes unitários e de integração

Código mais limpo, coeso e fácil de evoluir

⚙️ Como Rodar o Projeto

🔧 Pré-requisitos

Java 17+

Gradle

PostgreSQL instalado e em execução

🗃️ Configuração do Banco de Dados

No arquivo application.yml, configure suas credenciais:

spring:

datasource:

url: jdbc:postgresql://localhost:5432/votacao

username: SEU_USUARIO

password: SUA_SENHA

jpa:

hibernate:

ddl-auto: update

▶️ Executando a Aplicação

No terminal, rode:

./gradlew bootRun


A API estará disponível em:
👉 http://localhost:8080

🧭 Documentação Swagger

Acesse a interface interativa em:
👉 http://localhost:8080/swagger-ui.html

🧪 Testes

O projeto possui testes unitários e de integração com rollback automático.

Executar todos os testes:
./gradlew test

Tipos de testes:

🧩 Unitários: validam regras de negócio e serviços.

🔁 Integração: garantem persistência e comportamento entre camadas.

🔗 Endpoints Principais

Método	Endpoint	Descrição

POST	/pauta	Cria uma nova pauta

POST	/sessao	Abre uma sessão de votação

POST	/voto	Registra um voto

GET	/resultados/{idSessao}	Consulta o resultado da votação

🧩 Regras de Negócio

- Cada sessão possui início e fim definidos.
- Votos podem ser registrados livremente durante o período da sessão.
- O resultado é calculado automaticamente com base nos votos registrados.

🧠 Observações

Estrutura projetada para clareza e testabilidade.

Utiliza rollback automático em testes de integração.

Sistema pronto para ser consumido por um frontend React.

🏁 Exemplos de Execução
# Rodar o servidor
./gradlew bootRun

# Rodar todos os testes
./gradlew test
