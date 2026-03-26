# Arquitetura C4 Textual

## Objetivo

Este documento descreve a arquitetura da solucao em formato textual, servindo como base para a criacao do diagrama em draw.io.

## Nivel 1 - Contexto

### Pessoa

- Parceiro / Avaliador
  - consome a API REST para consultar, criar, atualizar e remover clientes

### Sistema

- Clientes API
  - sistema backend responsavel por expor operacoes REST sobre o dominio Cliente

### Relacionamentos

- Parceiro / Avaliador -> Clientes API
  - utiliza HTTP/JSON para consumir a API

## Nivel 2 - Containers

### Container 1

- Aplicacao Spring Boot
  - implementada em Java 17
  - concentra controller, service, repository, DTOs, configuracoes e tratamento de excecao

### Container 2

- Banco H2
  - banco relacional em memoria
  - armazena os registros de clientes durante a execucao

### Container 3

- Swagger UI / OpenAPI
  - documentacao interativa da API
  - permite visualizar e testar os endpoints

### Relacionamentos

- Parceiro / Avaliador -> Aplicacao Spring Boot
  - consome endpoints REST via HTTP

- Aplicacao Spring Boot -> Banco H2
  - persiste e consulta dados via Spring Data JPA / Hibernate

- Parceiro / Avaliador -> Swagger UI / OpenAPI
  - consulta a documentacao da API pelo navegador

- Swagger UI / OpenAPI -> Aplicacao Spring Boot
  - obtem o contrato OpenAPI exposto em `/v3/api-docs`

## Nivel 3 - Componentes Da Aplicacao Spring Boot

### Controller

- `ClienteController`
  - recebe requisicoes HTTP
  - expoe os endpoints REST
  - converte entrada e saida usando DTOs e mapper

### Service

- `ClienteService`
  - centraliza a logica de negocio
  - coordena consultas, atualizacoes, exclusoes e contagem

### Repository

- `ClienteRepository`
  - interface de acesso a dados com Spring Data JPA
  - realiza operacoes de persistencia sobre `Cliente`

### Model

- `Cliente`
  - entidade de dominio persistida no banco

### DTOs

- `ClienteRequest`
- `ClienteResponse`
- `ContagemResponse`
- `ErroResponse`
  - desacoplam o contrato HTTP do modelo interno

### Tratamento De Erros

- `ApiExceptionHandler`
  - padroniza erros de validacao, recurso inexistente, conflito e falhas internas

### Configuracao

- `OpenApiConfig`
  - define metadados da documentacao OpenAPI

- `LoggingConfig`
  - configura logging de requisicoes HTTP

### Relacionamentos

- `ClienteController` -> `ClienteService`
- `ClienteService` -> `ClienteRepository`
- `ClienteRepository` -> `Cliente`
- `ClienteController` -> `ClienteMapper`
- `ClienteController` -> DTOs
- `ApiExceptionHandler` -> DTO `ErroResponse`

## Como Desenhar No draw.io

### Contexto

- desenhar um ator chamado `Parceiro / Avaliador`
- desenhar um sistema chamado `Clientes API`
- ligar ator ao sistema com seta `HTTP/JSON`

### Containers

- dentro do sistema, desenhar:
  - `Aplicacao Spring Boot`
  - `Banco H2`
  - `Swagger UI / OpenAPI`
- conectar:
  - usuario -> aplicacao
  - usuario -> swagger
  - swagger -> aplicacao
  - aplicacao -> banco

### Componentes

- dentro da aplicacao, desenhar os blocos:
  - `ClienteController`
  - `ClienteService`
  - `ClienteRepository`
  - `Cliente`
  - `DTOs`
  - `ApiExceptionHandler`
  - `OpenApiConfig`
  - `LoggingConfig`
- conectar em fluxo:
  - request -> controller -> service -> repository -> banco
  - controller <-> DTOs
  - excecoes -> handler
