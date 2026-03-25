# Desafio Final POS XP

## Visao Geral

Este projeto implementa uma API REST para gerenciamento de clientes como solucao para o desafio final da pos em Arquitetura de Software.

A aplicacao foi desenvolvida com foco em:

- arquitetura MVC
- separacao de responsabilidades por camadas
- persistencia com Spring Data JPA
- documentacao via Swagger / OpenAPI
- observabilidade com logs
- validacao de entrada e tratamento padronizado de erros
- testes automatizados

## Stack Utilizada

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- Springdoc OpenAPI / Swagger UI
- JUnit 5
- MockMvc
- Mockito
- JaCoCo

## Dominio Escolhido

O dominio da API e `Cliente`.

Cada cliente possui os seguintes atributos:

- `id`
- `nome`
- `email`

## Funcionalidades Implementadas

- Criar cliente
- Listar todos os clientes
- Buscar cliente por ID
- Buscar clientes por nome
- Atualizar cliente
- Excluir cliente
- Contar total de clientes
- Validar dados de entrada
- Retornar `404` para recurso inexistente
- Retornar `409` para conflito de integridade

## Estrutura do Projeto

```text
src
+-- main
|   +-- java
|   |   +-- br/com/posxp/clientesapi
|   |       +-- config
|   |       +-- controller
|   |       +-- dto
|   |       +-- model
|   |       +-- repository
|   |       +-- service
|   |       +-- ClientesApiApplication.java
|   +-- resources
|       +-- application.properties
|       +-- data.sql
+-- test
    +-- java
    |   +-- br/com/posxp/clientesapi
    |       +-- config
    |       +-- controller
    |       +-- service
    +-- resources
        +-- application-test.properties
```

## Papel de Cada Camada

- `config`: configuracoes de infraestrutura, logging e OpenAPI.
- `controller`: endpoints REST e orquestracao das requisicoes HTTP.
- `dto`: objetos de entrada e saida da API.
- `model`: entidades de dominio persistidas.
- `repository`: acesso a dados com Spring Data JPA.
- `service`: regras de negocio da aplicacao.
- `resources`: configuracoes da aplicacao e carga inicial de dados.
- `test`: testes de integracao e unitarios.

## Como Executar

### Pre-requisitos

- Java 17 instalado
- Maven 3.9+ instalado

### Subir a aplicacao

```bash
mvn spring-boot:run
```

Depois disso, a API ficara disponivel em:

- `http://localhost:8080`

### Gerar build do projeto

```bash
mvn clean package
```

### Executar os testes

```bash
mvn test
```

## Executando com Docker

Se o usuario nao tiver Java instalado localmente, a aplicacao tambem pode ser executada via Docker.

Arquivos relacionados:

- `Dockerfile`
- `.dockerignore`

### Gerar a imagem

```bash
docker build -t clientes-api .
```

### Subir o container

```bash
docker run --rm -p 8080:8080 clientes-api
```

Depois disso, a aplicacao ficara disponivel em:

- `http://localhost:8080`
- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/h2-console`

### Parar o container

Se o container estiver rodando em primeiro plano, basta interromper com:

```bash
Ctrl + C
```

Se estiver rodando em background, use:

```bash
docker ps
docker stop <container_id>
```

## Documentacao da API

Com a aplicacao em execucao, a documentacao interativa pode ser acessada em:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Console do Banco H2

O console do banco em memoria pode ser acessado em:

- `http://localhost:8080/h2-console`

Parametros padrao:

- JDBC URL: `jdbc:h2:mem:clientesdb`
- User: `sa`
- Password: em branco

## Collection do Postman

O projeto possui uma collection pronta para importacao no Postman:

- `Cliente-API.postman_collection.json`

Essa collection contem as principais operacoes da API:

- listar clientes
- buscar cliente por ID
- buscar cliente por nome
- contar clientes
- criar cliente
- atualizar cliente
- excluir cliente
- validar retorno de erro `400`

### Como usar no Postman

1. Abra o Postman.
2. Clique em `Import`.
3. Selecione o arquivo `Cliente-API.postman_collection.json`.
4. Garanta que a aplicacao esteja rodando em `http://localhost:8080`.
5. Execute as requisicoes da pasta `Clientes`.

### Variavel utilizada na collection

A collection usa a variavel:

- `{{baseUrl}} = http://localhost:8080`

Se a aplicacao estiver rodando em outra porta ou host, basta alterar essa variavel no Postman.

## Endpoints Disponiveis

### Listar todos os clientes

```http
GET /clientes
```

### Buscar cliente por ID

```http
GET /clientes/{id}
```

### Buscar clientes por nome

```http
GET /clientes/nome/{nome}
```

### Contar clientes

```http
GET /clientes/contar
```

### Criar cliente

```http
POST /clientes
Content-Type: application/json
```

Exemplo de payload:

```json
{
  "nome": "Maria Oliveira",
  "email": "maria.oliveira@exemplo.com"
}
```

### Atualizar cliente

```http
PUT /clientes/{id}
Content-Type: application/json
```

Exemplo de payload:

```json
{
  "nome": "Maria Oliveira Atualizada",
  "email": "maria.atualizada@exemplo.com"
}
```

### Excluir cliente

```http
DELETE /clientes/{id}
```

## Respostas e Tratamento de Erros

A API retorna payload JSON consistente para erros.

Exemplos de cenarios tratados:

- `400 Bad Request` para dados invalidos
- `404 Not Found` para cliente inexistente
- `409 Conflict` para email duplicado
- `500 Internal Server Error` para erros inesperados

Exemplo de resposta de erro:

```json
{
  "timestamp": "2026-03-24T23:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente com id 999 nao encontrado.",
  "path": "/clientes/999",
  "validations": null
}
```

## Dados Iniciais

Ao iniciar a aplicacao, a base H2 recebe alguns registros para demonstracao:

- Ana Silva
- Bruno Costa
- Carla Souza

## Qualidade e Observabilidade

O projeto possui:

- logs de requisicao HTTP
- logs de operacoes da API
- logs de validacao e tratamento de excecao
- testes de integracao com MockMvc
- testes unitarios de service, mapper e configuracao
- relatorio de cobertura com JaCoCo em `target/site/jacoco/index.html`
