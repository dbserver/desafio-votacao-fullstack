# Votação - Entrega do desafio

## Tecnologias utilizadas:
- java 23.0.2
- maven 3.9.11
- mysql 8.0.43
- node 20.19.0
- angular 20.1.0

## Como rodar:
- tenha todas as dependências instaladas e configuradas
- crie o banco de dados 
- faça o clone do projeto
- abra o `./pautas-api`
- abra o arquivo: `./pautas-api/src/main/resources/application.properties`
- dentro do arquivo `application.properties`, coloque o usuário e senha do banco:
-- `spring.datasource.username=seuLoginDB`
-- `spring.datasource.password=suaSenhaDB`
- depois abra o terminal dentro da pasta `./pautas-api` 
- rode o comando no terminal: `mvn clean install`
- navegue até `./pautas-api/target`
- dentro de `/target`, rode o comando no terminal: `java -jar pautas-api-0.0.1-SNAPSHOT.jar`
- se tiver dado tudo certo, no fim do console aparece a mensagem:
-- `2025-07-29T19:36:43.147-03:00  INFO 4012 --- [pautas-api] [           main] c.n.pautas_api.PautasApiApplication      : Started PautasApiApplication in 7.002 seconds (process running for 7.61)`
- com a API rodando, volte a raíz do repositório
- navegue até `./pautas-ui` e abra o terminal nessa pasta
- rode o comando: `npm i`
- se tiver dado tudo certo, aparecerá a mensagem no final: 
-- `found 0 vulnerabilities`
- rode o front: `ng serve --open`

## Requisitos implementados:
- cadastrar pauta;
- listar pautas;
- abre um timer para a sessão da votação criada;
- recebe os votos dos associados com base no CPF;
- contabiliza os votos e exibe na tela após o término e na listagem;
- valida pautas e votos únicos;
- persiste no banco.
