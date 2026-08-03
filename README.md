# Deploy

- Acessar raíz do projeto e rodar:
  ```
  docker compose up
  ```
- Acessar app na URL (frontend servido staticamente):
  - http://localhost:8080
- Consumo de endpoints na URL:
  - http://localhost:8080/api/agendas
  - http://localhost:8080/api/cpf
- Acessar a documentação da API (Swagger)
  - http://localhost:8080/swagger-ui/index.html
- Executar testes
  ```
  docker compose -f docker-compose.test.yml up --abort-on-container-exit
  ```

### Considerações
- O state de uma pauta é derivado da data de encerramento das votações, então achei melhor calcular o state pela data em vez de utilizar uma coluna no banco de dados já que isso poderia introduzir problemas:
  - Tornaria necessário uma rotina de agendamento para atualizar o state quando a data limite chegar
  - Poderia haver inconsistência entre data de encerramento <> state
- Para calcular o resultado consolidado das votações, criado uma @Query para não ter que carregar todas as votações em memória para fazer a consolidação
- Escrito erros em console com LoggingHandler e GlobalExceptionHandler, o ideal seria gravar em Syslog.
- O cálculo do state de uma pauta foi encapsulado em regra de negócio, o mesmo deveria ter sido feito para validar um associateId e se uma pauta pode receber votações.

### Ferramentas utilizadas
| Tecnologia | Descrição |
|---|---|
| Flyway | Migração do banco de dados |
| JUnit e Mockito | Testes automatizados |
| Swagger | Documentação da API |
| React | Frontend |
| Docker | Deploy |

### Tarefa Bônus 1 - Integração com sistemas externos
- Implementado validador de CPF no endpoint 
  - POST http://localhost:8080/api/cpf body: { "cpf": "...." }
- Exemplo:
```
curl -X POST http://localhost:8080/api/cpf \ -H "Content-Type: application/json" \ -d '{ "cpf": "123.456.789-09" }'
```

### Tarefa Bônus 2 - Performance
- O primeiro gargalo seria a quantidade elevada de operações de INSERT no banco de dados. Utilizando Kafka, a requisição poderia ser enviada para uma fila de processamento assíncrona, permitindo que as requisições HTTP não fiquem aguardando a conclusão do INSERT no banco.
- O segundo gargalo seria o grande volume de requisições HTTP simultâneas. Normalmente, cada requisição consome uma thread durante o processamento. Com Spring WebFlux e programação reativa, é possível processar um número maior de requisições concorrentes utilizando um modelo não bloqueante, processando a requisição e encaminhando o voto para o Kafka sem manter threads ocupadas enquanto aguardam operações de I/O.
- Exemplo da implementação
```
    public Mono<Void> vote(VotingDTO voteDTO) {
        return Mono.fromFuture(
                kafkaTemplate.send(
                        "votes-topic",
                        voteDTO.associateId(),
                        voteDTO
                ).completable()
        ).then();
    }
```

### Tarefa Bônus 3 - Versionamento da API
- Versionamento de URI
```
GET /api/v1/agendas
GET /api/v2/agendas
```

```
@RequestMapping("/api/v1/agendas")
public class AgendasControllerV1

@RequestMapping("/api/v2/agendas")
public class AgendasControllerV2
```

