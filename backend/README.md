# Sistema de Votação

Este projeto foi desenvolvido para o desafio de desenvolvimento de uma API de
votação para uma cooperativa.

## Technologias Utilizadas

- Java 21
- Spring Boot
- Maven
- MongoDB
- Docker
- Docker Compose

## Estrutura do Projeto

Esse projeto foi criado utilizando a abordagem de design
de DDD (Domain Driven Development) e "Clean Architecture". 


- `src/main/java/com/votacao/desafiovotacao`: Contém o código principal do aplicativo.
    - `application`: Contém controladores e DTOs.
    - `domain`:Contém entidades, serviços e exceções.
    - `infra`: Contém as interfaces dos repositórios.


### Prerequisitos

- Java 21
- Maven
- MongoDB
- Docker
- Docker Compose

## Alguns CPFs para teste

- 086.492.750-95
- 984.928.230-46
- 582.106.150-41
- 263.220.420-84
- 333.535.550-48

Caso seja necessário mais CPFs para teste, pode-se utilizar o site
https://www.4devs.com.br/gerador_de_cpf

Pois adicionei um serviço para validar o CPF informado.

## Como rodar o backend do projeto

Para rodar o projeto é necessário ter o Java e o Maven e o Docker funcionando.

- Subir o banco de dados
```
docker compose up -d
```

- Execute a aplicação Spring Boot.
```
mvn clean
```
```
mvn spring-boot:run
```

- Para acessar a aplicação:
```
http://localhost:8080/
```


### Versionamento da API

"Como você versionaria a API da sua aplicação? Que estratégia usar?"

A versão da API pode ser controlada por meio da URL. Aconselhável quando a API sofre
mudanças significativas.
Por exemplo, a versão 1 da API seria acessada através de `/api/v1/`.

Outra estratégia seria usar o content-type para controlar a versão da API,
por exemplo: content-type: `application/vnd.api.v1+json`. Aconselhável quando a API sofre
mudanças incrementais ou mesmo mundanças de no conteúdo trocado com o cliente.

Portanto, eu versionaria a API conforme a mundança que seria feita no código.


### Explicação breve do porquê das escolhas tomadas durante o desenvolvimento da solução

Escolhi de usar o design DDD e o clean architecture para facilitar a manutenção 
e o entendimento do projeto. Tendo sempre em vista uma boa organização e 
bom entendimento do fluxo de dados e ações, o que permitindo o código ser
facilmente compreendido e alterado.

Com isso em mente o projeto foi divido em 3 camadas:
- a camada de Aplicação: onde ficam os controladores, os DTOS que são transmitidos para 
a camada de domínio e o handler global de exceções.
- a camada do Domínio: onde se encontram as entidades, serviços e exceções.
- a camada da Infraestutura (infra): no qual estão as interfaces dos repositórios que estão na camada de
- infraestrutura. E as entidades são o núcleo do projeto, onde ficam as regras de negócio.

Escolhi usar o MongoDB por ser um banco de dados NoSQL que traz a proposta de ser 
mais performático que um banco relacional, isso ocorre devido ao MongoDB ser um 
banco de dados CP (do Teorema CAP) o que oferece consistência e tolerância à 
partição em detrimento da disponibilidade.

O uso do MongoDB, portanto, é ideal para o cenário de votação permite que existam
centenas de milhares de votos, conforme exigido pela "Tarefa Bônus 2 - Performance".

Foi criado o serviço para validar o CPF informado conforme solicitado na
"Tarefa Bônus 1 - Validação do Associado". Entretanto optei por não integrar essa
funcionalidade de retornar aleatoriamente "ABLE_TO_VOTE" ou "UNABLE_TO_VOTE"
no projeto, sendo feito apenas um endpoint para sua utilização. Já a validação
do CPF foi adicionado ao código para garantir que não há CPFs
inválidos no banco de dados.

Só se altera o status de uma sessão para encerrada quando se procura por uma sessão e 
se verifica que o tempo de duração da sessão foi alcançado.

O Status da pauta não fecha, pois não há um limite para quantas sessões
podem ser criadas nela.

Criei o CRUD completo dos endpoints para caso sejam úteis ao testar a aplicação.

Escolhi deixar que o ID dos diversos itens nesse projeto sejam inseridos diretamente pelo
o usuário para facilitar a utilização e teste da aplicação.

Apenas o voto não está em inglês e esse readme, no backend, para facilitar o transporte de
de dados do voto entre o frontend e o backend e por que o readme do desafio esta em 
português.