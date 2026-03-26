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

## Aplicacao Do Padrao MVC

O projeto aplica o padrao MVC de forma adaptada ao contexto de API REST:

- `Model`
  - representado principalmente pela entidade `Cliente`
  - define a estrutura do dominio persistido

- `Controller`
  - representado por `ClienteController`
  - recebe as requisicoes HTTP e devolve as respostas da API

- `Service`
  - funciona como camada de negocio entre controller e persistencia
  - concentra validacoes de fluxo, busca, atualizacao, exclusao e contagem

Em uma API REST com Spring Boot, a camada `View` tradicional de interfaces graficas nao e utilizada. O papel de apresentacao e exercido pelo retorno JSON dos endpoints.

## Decisoes Arquiteturais

As principais decisoes adotadas no projeto foram:

- uso de Spring Boot
  - acelera a construcao da API e reduz codigo de infraestrutura

- uso do padrao MVC
  - facilita organizacao, manutencao e separacao de responsabilidades

- uso de DTOs
  - evita expor diretamente a entidade JPA como contrato da API
  - melhora clareza e controle do payload HTTP

- uso de Spring Data JPA com H2
  - simplifica a persistencia
  - permite demonstracao local rapida sem dependencias externas

- uso de Swagger / OpenAPI
  - melhora a documentacao e facilita validacao dos endpoints

- uso de logs
  - aumenta a observabilidade das requisicoes e erros da aplicacao

- uso de testes automatizados
  - reduz regressao e melhora confianca sobre os fluxos principais

- uso de Docker
  - permite executar a aplicacao mesmo sem Java instalado na maquina do avaliador

## Artefatos De Arquitetura

Os artefatos de documentacao arquitetural do projeto estao na pasta `doc`:

- `doc/ENTREGAVEIS.md`
- `doc/ARQUITETURA-C4.md`
- `doc/Enunciado do Desafio Final - Arquiteto(a) de Software.pdf`

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

## Observabilidade

O projeto utiliza Spring Boot Actuator para disponibilizar endpoints tecnicos de observabilidade.

### Endpoints habilitados

- Health: `http://localhost:8080/actuator/health`
- Info: `http://localhost:8080/actuator/info`
- Metrics: `http://localhost:8080/actuator/metrics`

### Papel de cada endpoint

- `health`
  - informa se a aplicacao esta saudavel
  - util para verificar se a API subiu corretamente e se dependencias como o banco estao disponiveis

- `info`
  - expõe informacoes da aplicacao, como nome, descricao e versao
  - util para identificacao do sistema

- `metrics`
  - expõe metricas tecnicas da aplicacao
  - util para inspecionar comportamento de JVM, memoria e requisicoes HTTP

### Como acessar

Com a aplicacao rodando localmente ou via Docker, abra no navegador ou consulte via Postman:

```http
GET /actuator/health
GET /actuator/info
GET /actuator/metrics
```

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
