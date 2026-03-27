# Desafio Final POS XP

## Visao Geral

Este projeto implementa uma API REST para gerenciamento de clientes, produtos e pedidos como solucao para o desafio final da pos em Arquitetura de Software.

A solucao demonstra:

- aplicacao do padrao MVC em contexto de API REST
- separacao de responsabilidades por camadas
- modelagem de dominio com relacionamentos
- persistencia com Spring Data JPA
- HATEOAS com Spring HATEOAS
- observabilidade com logs e Actuator
- documentacao com Swagger / OpenAPI
- testes automatizados
- empacotamento com Docker

## Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- Springdoc OpenAPI / Swagger UI
- Spring Boot Actuator
- Spring HATEOAS
- JUnit 5
- MockMvc
- Mockito
- JaCoCo
- Docker

## Dominio Da Solucao

O projeto modela um dominio de vendas com quatro elementos centrais:

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

### Regras De Negocio Implementadas

- `Pedido` possui `status`
- `Pedido` armazena o `total`
- `ItemPedido` armazena o `precoUnitario` no momento da compra
- o total do pedido e recalculado a partir dos itens
- nao e permitido excluir `Cliente` com pedidos associados
- nao e permitido excluir `Produto` com itens de pedido associados

## Funcionalidades

- CRUD completo de clientes
- CRUD completo de produtos
- CRUD completo de pedidos
- busca por nome para clientes e produtos
- busca por status para pedidos
- contagem total de clientes, produtos e pedidos
- validacao de entrada com Bean Validation
- tratamento padronizado de erros
- documentacao interativa da API
- observabilidade tecnica com Actuator
- dados iniciais para demonstracao

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

## Pacotes

- `builder`: montagem controlada de agregados complexos, como `Pedido`
- `config`: configuracoes de logging, OpenAPI e infraestrutura
- `controller`: endpoints REST e orquestracao HTTP
- `dto`: contratos de entrada e saida da API
- `exception`: excecoes de negocio e tratamento global de erros
- `mapper`: conversao entre entidades e DTOs
- `model`: entidades e enums do dominio
- `repository`: acesso a dados com Spring Data JPA
- `service`: contratos e implementacoes da regra de negocio

## Arquitetura

### Contexto C4

O sistema foi modelado no nivel de contexto como uma `Clientes API` consumida por um `Usuario / Avaliador`, responsavel por operar os fluxos de clientes, produtos e pedidos por meio de HTTP/JSON.

### Containers C4

No nivel de containers, o sistema foi representado por:

- `Aplicacao Spring Boot`
- `Banco H2`

A aplicacao Spring Boot concentra:

- endpoints REST
- regras de negocio
- mapeamentos entre entidades e DTOs
- HATEOAS
- documentacao OpenAPI
- endpoints operacionais do Actuator

O H2 foi mantido como container de persistencia, adequado ao escopo academico e ao objetivo de facilitar execucao e avaliacao local.

### Componentes C4

No nivel de componentes, a aplicacao foi decomposta em:

- controllers
- services
- mappers
- repositories
- `PedidoBuilder`
- `ApiExceptionHandler`

Essa decomposicao representa melhor os componentes realmente relevantes do sistema do que tratar configuracoes tecnicas como componentes centrais da arquitetura.

## Decisoes Arquiteturais

### MVC

O projeto aplica o padrao `Model-View-Controller` adaptado ao contexto de API REST:

- `Model`: representado pelas entidades de dominio persistidas
- `Controller`: responsavel por receber requisicoes HTTP e devolver respostas JSON
- `Service`: camada intermediaria com a logica de negocio

Em APIs REST, a `View` tradicional nao aparece como interface grafica. O papel de apresentacao e exercido pelos payloads JSON e pela documentacao OpenAPI.

### Repository

Foi adotado o padrao `Repository` por meio do Spring Data JPA. Essa decisao reduz acoplamento entre regra de negocio e persistencia, centralizando operacoes de acesso a dados em contratos especificos.

### DTO E Mapper

Foram utilizados `DTOs` e `Mappers` para desacoplar o contrato HTTP do modelo JPA.

Essa decisao:

- evita expor entidades diretamente na API
- reduz acoplamento entre persistencia e interface
- melhora controle sobre serializacao e validacao

### HATEOAS

Foi incorporado HATEOAS nas respostas dos recursos principais com Spring HATEOAS.

As respostas expoem links navegaveis para:

- o proprio recurso (`self`)
- a colecao do recurso
- recursos relacionados, como o cliente associado a um pedido

### Service Layer

A camada de servico concentra regras de negocio, como:

- calculo do total do pedido
- atribuicao de status inicial
- validacao de existencia de cliente e produto
- bloqueio de exclusao por integridade de negocio

### Dependency Inversion

Os controllers dependem de interfaces de servico, nao diretamente de implementacoes concretas. Isso melhora testabilidade e reduz acoplamento.

### Builder

Foi adotado o padrao `Builder` na construcao de `Pedido` por meio de `PedidoBuilder`, centralizando a montagem do agregado e o calculo do total.

### Tratamento Centralizado De Excecoes

`ApiExceptionHandler` padroniza respostas para:

- `400 Bad Request`
- `404 Not Found`
- `409 Conflict`
- `500 Internal Server Error`

### Persistencia Com H2

Foi escolhida persistencia em `H2` por se tratar de um trabalho academico com foco em simplicidade de execucao e independencia de infraestrutura externa.

### Observabilidade

Foi incorporada uma camada enxuta de observabilidade com:

- logs estruturados de controllers e services
- logging de requisicoes HTTP
- Spring Boot Actuator

## Aderencia Ao Estilo RESTful

A aplicacao pode ser considerada uma API REST em sentido pratico e esta proxima de uma API RESTful mais completa.

### Elementos Que Aproximam A API Do Estilo RESTful

- recursos identificados por URIs claras, como `/clientes`, `/produtos` e `/pedidos`
- uso consistente dos metodos HTTP `GET`, `POST`, `PUT` e `DELETE`
- respostas com codigos HTTP semanticos, como `200`, `201`, `204`, `400`, `404` e `409`
- uso de JSON como representacao dos recursos
- ausencia de estado de sessao entre requisicoes
- uso de HATEOAS para enriquecer respostas com links navegaveis

### Pontos Pragmaticos Da API

- os endpoints de busca por nome e status usam path variables
- os endpoints de contagem sao dedicados e nao modelados como metadado de colecao
- nao ha versionamento explicito da API

Essas escolhas foram mantidas para favorecer clareza, simplicidade de consumo e objetividade da entrega.

## Entregaveis

Os principais artefatos da entrega podem ser acessados diretamente a partir deste README:

- [Enunciado do Desafio Final - Arquiteto(a) de Software.pdf](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/01-enunciado/Enunciado%20do%20Desafio%20Final%20-%20Arquiteto(a)%20de%20Software.pdf)
- [ENTREGAVEIS.md](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/03-entrega/ENTREGAVEIS.md)
- [ARQUITETURA-C4.md](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/ARQUITETURA-C4.md)
- [arquitetura-contexto.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/arquitetura-contexto.png)
- [arquitetura-containers.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/arquitetura-containers.png)
- [arquitetura-componentes.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/arquitetura-componentes.png)
- [arquitetura-contexto.puml](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/arquitetura-contexto.puml)
- [arquitetura-containers.puml](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/arquitetura-containers.puml)
- [arquitetura-componentes.puml](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/arquitetura-componentes.puml)
- [arquitetura-contexto.mmd](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/arquitetura-contexto.mmd)
- [arquitetura-containers.mmd](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/arquitetura-containers.mmd)
- [arquitetura-componentes.mmd](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/arquitetura-componentes.mmd)
- [Pasta de Diagramas de Sequencia](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia)
- [README dos Diagramas de Sequencia](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia/README.md)
- [01-criar-cliente.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia/01-criar-cliente.png)
- [02-criar-produto.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia/02-criar-produto.png)
- [03-criar-pedido.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia/03-criar-pedido.png)
- [04-atualizar-pedido.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia/04-atualizar-pedido.png)
- [05-listar-clientes-paginado.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia/05-listar-clientes-paginado.png)
- [06-excluir-cliente-bloqueado.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia/06-excluir-cliente-bloqueado.png)
- [07-excluir-produto-bloqueado.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia/07-excluir-produto-bloqueado.png)
- [08-sort-invalido.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia/08-sort-invalido.png)
- [09-validacao-payload-invalido.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia/09-validacao-payload-invalido.png)
- [10-criar-cliente-email-duplicado.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia/10-criar-cliente-email-duplicado.png)
- [Cliente-API.postman_collection.json](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/Cliente-API.postman_collection.json)
- [docker-compose.yml](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/docker-compose.yml)
- [Dockerfile](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/Dockerfile)
- [pom.xml](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/pom.xml)

## Execucao

### Pre-requisitos

- Java 17 instalado
- Maven 3.9+ instalado

### Subir A Aplicacao

```bash
mvn spring-boot:run
```

### Build

```bash
mvn clean package
```

### Executar Testes

```bash
mvn test
```

### Docker

```bash
docker compose up --build
```

## Endpoints Principais

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
