# Desafio Final POS XP

## Visão Geral

Este projeto implementa uma API REST para gerenciamento de clientes, produtos e pedidos como solução para o desafio final da pós em Arquitetura de Software.

A solução foi concebida para demonstrar:

- aplicação do padrão MVC em contexto de API REST
- separação de responsabilidades por camadas
- modelagem de domínio com relacionamentos
- persistência com Spring Data JPA
- observabilidade com logs e Actuator
- documentação com Swagger / OpenAPI
- testes automatizados
- empacotamento com Docker

## Stack Utilizada

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- Springdoc OpenAPI / Swagger UI
- Spring Boot Actuator
- JUnit 5
- MockMvc
- Mockito
- JaCoCo
- Docker

## Domínio Da Solução

O projeto evoluiu de um CRUD simples para um domínio de vendas com quatro elementos centrais:

- `Cliente`
- `Produto`
- `Pedido`
- `ItemPedido`

### Entidades

`Cliente`

- `id`
- `nome`
- `email`

`Produto`

- `id`
- `nome`
- `descricao`
- `preco`

`Pedido`

- `id`
- `cliente`
- `status`
- `total`
- `dataCriacao`
- `itens`

`ItemPedido`

- `id`
- `pedido`
- `produto`
- `quantidade`
- `precoUnitario`
- `subtotal`

`PedidoStatus`

- `CRIADO`
- `PAGO`
- `CANCELADO`
- `ENVIADO`
- `FINALIZADO`

### Regras De Negócio Implementadas

- `Pedido` possui `status`
- `Pedido` armazena o `total`
- `ItemPedido` armazena o `precoUnitario` no momento da compra
- o total do pedido é recalculado a partir dos itens
- não é permitido excluir `Cliente` com pedidos associados
- não é permitido excluir `Produto` com itens de pedido associados

## Funcionalidades Implementadas

- CRUD completo de clientes
- CRUD completo de produtos
- CRUD completo de pedidos
- busca por nome para clientes e produtos
- busca por status para pedidos
- contagem total de clientes, produtos e pedidos
- validação de entrada com Bean Validation
- tratamento padronizado de erros
- documentação interativa da API
- observabilidade técnica com Actuator
- dados iniciais para demonstração

## Estrutura Do Projeto

```text
src
+-- main
|   +-- java
|   |   +-- br/com/posxp/clientesapi
|   |       +-- builder
|   |       +-- config
|   |       +-- controller
|   |       +-- dto
|   |       +-- exception
|   |       +-- mapper
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
    |       +-- mapper
    |       +-- service
    +-- resources
        +-- application-test.properties
```

## Papel De Cada Pacote

- `builder`: montagem controlada de agregados complexos, como `Pedido`
- `config`: configurações de logging, OpenAPI e infraestrutura
- `controller`: endpoints REST e orquestração HTTP
- `dto`: contratos de entrada e saída da API
- `exception`: exceções de negócio e tratamento global de erros
- `mapper`: conversão entre entidades e DTOs
- `model`: entidades e enums do domínio
- `repository`: acesso a dados com Spring Data JPA
- `service`: contratos e implementações da regra de negócio

## Padrões E Decisões Arquiteturais

### MVC

O projeto aplica o padrão `Model-View-Controller` adaptado ao contexto de API REST:

- `Model`: representado pelas entidades de domínio persistidas
- `Controller`: responsável por receber requisições HTTP e devolver respostas JSON
- `Service`: camada intermediária com a lógica de negócio

Em APIs REST, a `View` tradicional não aparece como interface gráfica. O papel de apresentação é exercido pelos payloads JSON e pela documentação OpenAPI.

### Repository

Foi adotado o padrão `Repository` por meio do Spring Data JPA. Essa decisão reduz acoplamento entre regra de negócio e persistência, centralizando operações de acesso a dados em contratos específicos.

Justificativa técnica:

- melhora a coesão da camada de acesso a dados
- evita SQL espalhado pela aplicação
- facilita evolução da persistência

### DTO e Mapper

Foram utilizados `DTOs` e `Mappers` para desacoplar o contrato HTTP do modelo JPA.

Justificativa acadêmica e técnica:

- evita expor entidades diretamente na API
- reduz risco de acoplamento indevido entre persistência e interface
- melhora controle sobre validação e serialização

### Service Layer

A camada de serviço concentra regras de negócio, como:

- cálculo do total do pedido
- atribuição de status inicial
- validação de existência de cliente e produto
- bloqueio de exclusão por integridade de negócio

Essa decisão reforça separação de responsabilidades e facilita manutenção, teste e reuso de regras.

### Interfaces De Serviço E Inversão De Dependência

Os controllers dependem de abstrações, como:

- [ClienteService.java](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\src\main\java\br\com\posxp\clientesapi\service\ClienteService.java)
- [ProdutoService.java](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\src\main\java\br\com\posxp\clientesapi\service\ProdutoService.java)
- [PedidoService.java](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\src\main\java\br\com\posxp\clientesapi\service\PedidoService.java)

com implementações concretas em:

- [ClienteServiceImpl.java](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\src\main\java\br\com\posxp\clientesapi\service\ClienteServiceImpl.java)
- [ProdutoServiceImpl.java](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\src\main\java\br\com\posxp\clientesapi\service\ProdutoServiceImpl.java)
- [PedidoServiceImpl.java](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\src\main\java\br\com\posxp\clientesapi\service\PedidoServiceImpl.java)

Isso caracteriza aplicação de `Dependency Inversion` na camada de serviços, pois módulos de mais alto nível dependem de contratos em vez de implementações específicas.

### Builder

Foi adotado o padrão `Builder` na construção de `Pedido`, por meio de [PedidoBuilder.java](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\src\main\java\br\com\posxp\clientesapi\builder\PedidoBuilder.java).

Justificativa:

- a entidade `Pedido` passou a ser um agregado com montagem mais complexa
- a construção exige associar cliente, itens, subtotais, total e status
- o builder centraliza essa montagem e reduz repetição na camada de serviço

### Tratamento Centralizado De Exceções

O tratamento global em [ApiExceptionHandler.java](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\src\main\java\br\com\posxp\clientesapi\exception\ApiExceptionHandler.java) padroniza respostas para:

- `400 Bad Request`
- `404 Not Found`
- `409 Conflict`
- `500 Internal Server Error`

Essa abordagem melhora consistência da API e previsibilidade para consumidores.

### Persistência Com H2 E Spring Data JPA

Foi escolhida persistência em `H2` por se tratar de um trabalho acadêmico com foco em demonstração, simplicidade de execução e independência de infraestrutura externa.

Justificativa:

- permite executar o projeto sem instalação de banco adicional
- acelera validação local e avaliação do projeto
- preserva conceitos relacionais e integridade referencial

### Observabilidade

Foi incorporada uma camada enxuta de observabilidade com:

- logs estruturados de controllers e services
- logging de requisições HTTP
- Spring Boot Actuator

Essa decisão reforça boas práticas operacionais, permitindo inspeção de saúde, informações da aplicação e métricas.

## Artefatos De Arquitetura

Os artefatos de documentação arquitetural estão na pasta `doc`:

- [ENTREGAVEIS.md](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\ENTREGAVEIS.md)
- [ARQUITETURA-C4.md](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\ARQUITETURA-C4.md)
- [Enunciado do Desafio Final - Arquiteto(a) de Software.pdf](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\Enunciado%20do%20Desafio%20Final%20-%20Arquiteto(a)%20de%20Software.pdf)

## Como Executar

### Pré-requisitos

- Java 17 instalado
- Maven 3.9+ instalado

### Subir a aplicação

```bash
mvn spring-boot:run
```

### Build

```bash
mvn clean package
```

### Testes

```bash
mvn clean test
```

## Executando Com Docker

### Gerar a imagem

```bash
docker build -t clientes-api .
```

### Subir o container

```bash
docker run --rm -p 8080:8080 clientes-api
```

### Endereços úteis

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/v3/api-docs`
- `http://localhost:8080/h2-console`
- `http://localhost:8080/actuator/health`

## Swagger E OpenAPI

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Observabilidade

Endpoints técnicos habilitados:

- `GET /actuator/health`
- `GET /actuator/info`
- `GET /actuator/metrics`

## Collection Do Postman

O projeto possui uma collection pronta para importação:

- [Cliente-API.postman_collection.json](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\Cliente-API.postman_collection.json)

Variável utilizada:

- `{{baseUrl}} = http://localhost:8080`

## Console H2

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:clientesdb`
- User: `sa`
- Password: em branco

## Endpoints Disponíveis

### Clientes

- `GET /clientes`
- `GET /clientes/{id}`
- `GET /clientes/nome/{nome}`
- `GET /clientes/contar`
- `POST /clientes`
- `PUT /clientes/{id}`
- `DELETE /clientes/{id}`

### Produtos

- `GET /produtos`
- `GET /produtos/{id}`
- `GET /produtos/nome/{nome}`
- `GET /produtos/contar`
- `POST /produtos`
- `PUT /produtos/{id}`
- `DELETE /produtos/{id}`

### Pedidos

- `GET /pedidos`
- `GET /pedidos/{id}`
- `GET /pedidos/status/{status}`
- `GET /pedidos/contar`
- `POST /pedidos`
- `PUT /pedidos/{id}`
- `DELETE /pedidos/{id}`

## Exemplos De Payload

### Criar Cliente

```json
{
  "nome": "Maria Oliveira",
  "email": "maria.oliveira@exemplo.com"
}
```

### Criar Produto

```json
{
  "nome": "Teclado",
  "descricao": "Teclado mecanico",
  "preco": 350.00
}
```

### Criar Pedido

```json
{
  "clienteId": 2,
  "itens": [
    { "produtoId": 2, "quantidade": 2 },
    { "produtoId": 3, "quantidade": 1 }
  ]
}
```

## Tratamento De Erros

A API retorna payload JSON consistente para cenários de falha.

Exemplo:

```json
{
  "timestamp": "2026-03-25T21:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Produto nao pode ser removido porque possui itens de pedido associados.",
  "path": "/produtos/1",
  "validations": null
}
```

## Dados Iniciais

Ao iniciar a aplicação, a base H2 recebe:

- `100` clientes
- `100` produtos
- `10` pedidos
- `50` itens de pedido

Essa massa permite demonstrar:

- listagens mais realistas
- relações entre entidades
- bloqueios de exclusão por integridade de negócio

## Qualidade Do Projeto

O projeto possui:

- logs de requisição HTTP
- logs de operações de negócio
- tratamento padronizado de erros
- testes de integração com MockMvc
- testes unitários de service, mapper e configuração
- cobertura com JaCoCo em `target/site/jacoco/index.html`
