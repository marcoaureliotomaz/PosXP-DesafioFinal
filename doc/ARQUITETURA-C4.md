# Arquitetura C4 Textual

## Objetivo

Este documento descreve a arquitetura da solução em formato textual, servindo como base para a criação do diagrama em draw.io.

## Nível 1 - Contexto

### Pessoa

- Parceiro / Avaliador
  - consome a API REST para consultar, criar, atualizar e remover clientes, produtos e pedidos

### Sistema

- Clientes API
  - sistema backend responsável por expor operações REST sobre os domínios `Cliente`, `Produto` e `Pedido`

### Relacionamentos

- Parceiro / Avaliador -> Clientes API
  - utiliza HTTP/JSON para consumir a API

## Nível 2 - Containers

### Container 1

- Aplicação Spring Boot
  - implementada em Java 17
  - concentra controllers, services, repositories, DTOs, mappers, builders, configurações e tratamento de exceção

### Container 2

- Banco H2
  - banco relacional em memória
  - armazena clientes, produtos, pedidos e itens de pedido

### Container 3

- Swagger UI / OpenAPI
  - documentação interativa da API
  - permite visualizar e testar endpoints

### Container 4

- Spring Boot Actuator
  - endpoints técnicos de health, info e metrics

### Relacionamentos

- Parceiro / Avaliador -> Aplicação Spring Boot
  - consome endpoints REST via HTTP

- Aplicação Spring Boot -> Banco H2
  - persiste e consulta dados via Spring Data JPA / Hibernate

- Parceiro / Avaliador -> Swagger UI / OpenAPI
  - consulta a documentação da API via navegador

- Swagger UI / OpenAPI -> Aplicação Spring Boot
  - obtém o contrato exposto em `/v3/api-docs`

- Parceiro / Avaliador -> Spring Boot Actuator
  - consulta informações operacionais da aplicação

## Nível 3 - Componentes Da Aplicação Spring Boot

### Controllers

- `ClienteController`
- `ProdutoController`
- `PedidoController`
  - recebem requisições HTTP
  - expõem endpoints REST
  - delegam regras de negócio para a camada de serviço

### Services

- `ClienteService` / `ClienteServiceImpl`
- `ProdutoService` / `ProdutoServiceImpl`
- `PedidoService` / `PedidoServiceImpl`
  - centralizam a lógica de negócio
  - aplicam validações de fluxo e regras de integridade

### Repositories

- `ClienteRepository`
- `ProdutoRepository`
- `PedidoRepository`
- `ItemPedidoRepository`
  - abstraem a persistência com Spring Data JPA

### Model

- `Cliente`
- `Produto`
- `Pedido`
- `ItemPedido`
- `PedidoStatus`

### DTOs

- requests e responses de cliente, produto e pedido
- `ContagemResponse`
- `ErroResponse`

### Mappers

- `ClienteMapper`
- `ProdutoMapper`
- `PedidoMapper`
  - convertem entre entidades e DTOs

### Builder

- `PedidoBuilder`
  - encapsula a montagem do agregado `Pedido`
  - associa cliente, itens, subtotais e total

### Tratamento De Erros

- `ApiExceptionHandler`
- `RecursoNaoEncontradoException`
- `OperacaoNaoPermitidaException`

### Configuração

- `OpenApiConfig`
- `LoggingConfig`

## Relacionamentos Entre Componentes

- `ClienteController` -> `ClienteService`
- `ProdutoController` -> `ProdutoService`
- `PedidoController` -> `PedidoService`

- `ClienteServiceImpl` -> `ClienteRepository`
- `ProdutoServiceImpl` -> `ProdutoRepository`
- `ProdutoServiceImpl` -> `ItemPedidoRepository`
- `PedidoServiceImpl` -> `PedidoRepository`
- `PedidoServiceImpl` -> `ClienteRepository`
- `PedidoServiceImpl` -> `ProdutoRepository`

- `PedidoServiceImpl` -> `PedidoBuilder`

- controllers -> mappers -> DTOs
- exceções -> `ApiExceptionHandler` -> `ErroResponse`

## Decisões Arquiteturais Evidenciadas No C4

- `MVC` para organizar a aplicação por responsabilidade
- `Repository` para desacoplar regra de negócio da persistência
- `Service Layer` para concentrar lógica de negócio
- `DTO + Mapper` para desacoplar modelo interno do contrato HTTP
- `Builder` para construção controlada de agregados complexos
- `Dependency Inversion` por meio de interfaces de serviço

## Como Desenhar No draw.io

### Contexto

- desenhar um ator chamado `Parceiro / Avaliador`
- desenhar um sistema chamado `Clientes API`
- ligar ator ao sistema com seta `HTTP/JSON`

### Containers

- dentro do sistema, desenhar:
  - `Aplicação Spring Boot`
  - `Banco H2`
  - `Swagger UI / OpenAPI`
  - `Spring Boot Actuator`
- conectar:
  - usuário -> aplicação
  - usuário -> swagger
  - swagger -> aplicação
  - usuário -> actuator
  - aplicação -> banco

### Componentes

- dentro da aplicação, desenhar os blocos:
  - `ClienteController`
  - `ProdutoController`
  - `PedidoController`
  - `ClienteService / Impl`
  - `ProdutoService / Impl`
  - `PedidoService / Impl`
  - `Repositories`
  - `Entities`
  - `DTOs`
  - `Mappers`
  - `PedidoBuilder`
  - `ApiExceptionHandler`
  - `OpenApiConfig`
  - `LoggingConfig`
- conectar em fluxo:
  - request -> controller -> service -> repository -> banco
  - controller -> mapper -> DTO
  - pedido service -> builder
  - exceções -> handler
