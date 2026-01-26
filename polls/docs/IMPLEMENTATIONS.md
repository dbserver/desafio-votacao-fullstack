## Projeto Votação

Projeto seguirar com duas base ( redis, postgres )

**Redis:** Usando como base principal, para fluxo de votação,
pois sua leitura bem como sua persistência são performaticos pra alta demanda.

**Postgress:** Usado como banco secundário para historico.

Features: 

 - Migrations: para controle de criação de tabela e artefatos para banco;
 - Configuração Redis
 - Configuração Rabbitmq
 - Conexão com Banco
 - Endpoints:
   - Criação de pauta
   - Lista de pautas
   - Realizar Votação


## Deploy

API na Heroku, realizado deploy via CLI

```shell
  heroku stack:set container -a polls-java-api
  heroku container:login
  heroku container:push web -a polls-java-api
  heroku container:release web -a polls-java-api
```