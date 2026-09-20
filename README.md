# Votação

## Objetivo

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. Imagine que você deve criar uma solução we para gerenciar e participar dessas sessões de votação.
Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST / Front:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por
  um tempo determinado na chamada de abertura ou 1 minuto por default)
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado
  é identificado por um id único e pode votar apenas uma vez por pauta)
- Contabilizar os votos e dar o resultado da votação na pauta

Para fins de exercício, a segurança das interfaces pode ser abstraída e qualquer chamada para as interfaces pode ser considerada como autorizada. A solução deve ser construída em java com Spring-boot e Angular/React conforme orientação, mas os frameworks e bibliotecas são de livre escolha (desde que não infrinja direitos de uso).

É importante que as pautas e os votos sejam persistidos e que não sejam perdidos com o restart da aplicação.

## Como proceder

Por favor, realize o FORK desse repositório e implemente sua solução no FORK em seu repositório GItHub, ao final, notifique da conclusão para que possamos analisar o código implementado.

Lembre de deixar todas as orientações necessárias para executar o seu código.

### Tarefas bônus

- Tarefa Bônus 1 - Integração com sistemas externos
  - Criar uma Facade/Client Fake que retorna aleátoriamente se um CPF recebido é válido ou não.
  - Caso o CPF seja inválido, a API retornará o HTTP Status 404 (Not found). Você pode usar geradores de CPF para gerar CPFs válidos
  - Caso o CPF seja válido, a API retornará se o usuário pode (ABLE_TO_VOTE) ou não pode (UNABLE_TO_VOTE) executar a operação. Essa operação retorna resultados aleatórios, portanto um mesmo CPF pode funcionar em um teste e não funcionar no outro.

```
// CPF Ok para votar
{
    "status": "ABLE_TO_VOTE
}
// CPF Nao Ok para votar - retornar 404 no client tb
{
    "status": "UNABLE_TO_VOTE
}
```

Exemplos de retorno do serviço

### Tarefa Bônus 2 - Performance

- Imagine que sua aplicação possa ser usada em cenários que existam centenas de
  milhares de votos. Ela deve se comportar de maneira performática nesses
  cenários
- Testes de performance são uma boa maneira de garantir e observar como sua
  aplicação se comporta

### Tarefa Bônus 3 - Versionamento da API

○ Como você versionaria a API da sua aplicação? Que estratégia usar?

## O que será analisado

- Simplicidade no design da solução (evitar over engineering)
- Organização do código
- Arquitetura do projeto
- Boas práticas de programação (manutenibilidade, legibilidade etc)
- Possíveis bugs
- Tratamento de erros e exceções
- Explicação breve do porquê das escolhas tomadas durante o desenvolvimento da solução
- Uso de testes automatizados e ferramentas de qualidade
- Limpeza do código
- Documentação do código e da API
- Logs da aplicação
- Mensagens e organização dos commits
- Testes
- Layout responsivo

## Dicas

- Teste bem sua solução, evite bugs

  Observações importantes

- Não inicie o teste sem sanar todas as dúvidas
- Iremos executar a aplicação para testá-la, cuide com qualquer dependência externa e
  deixe claro caso haja instruções especiais para execução do mesmo
  Classificação da informação: Uso Interno

# desafio-votacao

## Como rodar

O jeito mais simples é subir tudo com Docker, na raiz do projeto:

```bash
docker compose up --build
```

Isso sobe o banco, o backend e o frontend:

| O quê    | Endereço                                           |
| -------- | -------------------------------------------------- |
| Frontend | http://localhost:3000                              |
| API      | http://localhost:8081/api/v1                       |
| Swagger  | http://localhost:8081/swagger-ui.html              |
| Postgres | localhost:5436 (usuário, senha e banco: `votacao`) |

## Versionamento da API

Optei por colocar a versão na URL: todas as rotas começam com `/api/v1`. É a forma mais simples de ver e testar, já que a versão aparece no Swagger, no navegador e nos logs, sem depender de header.

Uma nova versão (`/api/v2`) só seria criada para mudanças que quebram quem já consome a API, como remover ou renomear um campo ou mudar uma regra. Adicionar um campo opcional ou um endpoint novo continua na v1.

Se a v2 chegar, as duas versões rodam juntas por um tempo: os controllers novos reaproveitam os mesmos services, e a v1 fica marcada como descontinuada até os clientes migrarem.

## Validação de CPF

O associado é cadastrado com CPF, e o cadastro já recusa CPFs com dígitos verificadores errados. Na hora do voto, o CPF passa por um client fake (`FakeCpfClient`) que, como pede o enunciado, responde de forma aleatória. Se o CPF sair inválido ou o associado sair como `UNABLE_TO_VOTE`, o voto é recusado com 404. Por isso, é normal um mesmo associado conseguir votar numa tentativa e ser recusado na seguinte.

O `VotoService` depende da interface `CpfClient`, e não do fake. Para usar um serviço real, basta criar outra implementação.
