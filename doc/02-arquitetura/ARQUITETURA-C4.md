# Arquitetura C4 Textual

## Objetivo

Este documento descreve a arquitetura da solucao em formato textual, servindo como base para os diagramas C4 exportados em `.png` e para os fontes em PlantUML.

## Nivel 1 - Contexto

### Pessoa

- Parceiro / Avaliador
  - consome a API REST para consultar, criar, atualizar e remover clientes, produtos e pedidos

### Sistema

- Clientes API
  - sistema backend responsavel por expor operacoes REST sobre os dominios `Cliente`, `Produto` e `Pedido`

### Relacionamentos

- Parceiro / Avaliador -> Clientes API
  - utiliza HTTP/JSON para consumir a API

## Nivel 2 - Containers

### Container 1

- Aplicacao Spring Boot
  - implementada em Java 17
  - concentra controllers, services, repositories, DTOs, mappers, builder, tratamento de excecao, documentacao OpenAPI e endpoints operacionais do Actuator

### Container 2

- Banco H2
  - banco relacional em memoria
  - armazena clientes, produtos, pedidos e itens de pedido

### Relacionamentos

- Parceiro / Avaliador -> Aplicacao Spring Boot
  - consome endpoints REST via HTTP

- Parceiro / Avaliador -> Aplicacao Spring Boot
  - consulta a documentacao interativa disponibilizada pelo Swagger UI

- Parceiro / Avaliador -> Aplicacao Spring Boot
  - consulta informacoes operacionais por meio dos endpoints do Actuator

- Aplicacao Spring Boot -> Banco H2
  - persiste e consulta dados via Spring Data JPA / Hibernate

## Nivel 3 - Componentes Da Aplicacao Spring Boot

### Controllers

- `ClienteController`
- `ProdutoController`
- `PedidoController`
  - recebem requisicoes HTTP
  - expoem endpoints REST
  - delegam regras de negocio para a camada de servico

### Services

- `ClienteService` / `ClienteServiceImpl`
- `ProdutoService` / `ProdutoServiceImpl`
- `PedidoService` / `PedidoServiceImpl`
  - centralizam a logica de negocio
  - aplicam validacoes de fluxo e regras de integridade

### Mappers

- `ClienteMapper`
- `ProdutoMapper`
- `PedidoMapper`
  - convertem entre entidades e DTOs

### Repositories

- `ClienteRepository`
- `ProdutoRepository`
- `PedidoRepository`
- `ItemPedidoRepository`
  - abstraem a persistencia com Spring Data JPA

### Builder

- `PedidoBuilder`
  - encapsula a montagem do agregado `Pedido`
  - associa cliente, itens, subtotais e total

### Tratamento De Erros

- `ApiExceptionHandler`
- `RecursoNaoEncontradoException`
- `OperacaoNaoPermitidaException`

## Relacionamentos Entre Componentes

- `ClienteController` -> `ClienteService`
- `ProdutoController` -> `ProdutoService`
- `PedidoController` -> `PedidoService`

- `ClienteController` -> `ClienteMapper`
- `ProdutoController` -> `ProdutoMapper`
- `PedidoController` -> `PedidoMapper`

- `ClienteServiceImpl` -> `ClienteRepository`
- `ProdutoServiceImpl` -> `ProdutoRepository`
- `ProdutoServiceImpl` -> `ItemPedidoRepository`
- `PedidoServiceImpl` -> `PedidoRepository`
- `PedidoServiceImpl` -> `ClienteRepository`
- `PedidoServiceImpl` -> `ProdutoRepository`

- `PedidoServiceImpl` -> `PedidoBuilder`

- `ApiExceptionHandler` trata excecoes originadas nos controllers
- repositories -> banco H2

## Decisoes Arquiteturais Evidenciadas No C4

- `MVC` para organizar a aplicacao por responsabilidade
- `Repository` para desacoplar regra de negocio da persistencia
- `Service Layer` para concentrar logica de negocio
- `DTO + Mapper` para desacoplar modelo interno do contrato HTTP
- `Builder` para construcao controlada de agregados complexos
- `Dependency Inversion` por meio de interfaces de servico
- `HATEOAS` para descoberta e navegabilidade entre recursos

## Como Desenhar No draw.io

### Contexto

- desenhar um ator chamado `Parceiro / Avaliador`
- desenhar um sistema chamado `Clientes API`
- ligar ator ao sistema com seta `HTTP/JSON`

### Containers

- dentro do sistema, desenhar:
  - `Aplicacao Spring Boot`
  - `Banco H2`
- conectar:
  - usuario -> aplicacao
  - usuario -> aplicacao para documentacao OpenAPI
  - usuario -> aplicacao para endpoints operacionais
  - aplicacao -> banco

### Componentes

- dentro da aplicacao, desenhar os blocos:
  - `ClienteController`
  - `ProdutoController`
  - `PedidoController`
  - `ClienteService / Impl`
  - `ProdutoService / Impl`
  - `PedidoService / Impl`
  - `ClienteMapper`
  - `ProdutoMapper`
  - `PedidoMapper`
  - `Repositories`
  - `PedidoBuilder`
  - `ApiExceptionHandler`
- conectar em fluxo:
  - request -> controller -> service -> repository -> banco
  - controller -> mapper
  - pedido service -> builder
  - excecoes -> handler
