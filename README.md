# Desafio Final POS XP

## Visão Geral

Este projeto implementa uma API REST para gerenciamento de clientes, produtos e pedidos como solução para o desafio final da pós em Arquitetura de Software.

A solução foi concebida para demonstrar:

- aplicação do padrão MVC em contexto de API REST
- separação de responsabilidades por camadas
- modelagem de domínio com relacionamentos
- persistência com Spring Data JPA
- HATEOAS com Spring HATEOAS
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
- Spring HATEOAS
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

### Uso De `class` Em Vez De `record` Nos DTOs De Saída

Os DTOs de entrada continuam usando `record`, pois representam apenas estruturas simples e imutáveis de transporte de dados.

Os DTOs de saída principais passaram a usar `class` nos arquivos:

- [ClienteResponse.java](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\src\main\java\br\com\posxp\clientesapi\dto\ClienteResponse.java)
- [ProdutoResponse.java](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\src\main\java\br\com\posxp\clientesapi\dto\ProdutoResponse.java)
- [PedidoResponse.java](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\src\main\java\br\com\posxp\clientesapi\dto\PedidoResponse.java)

Essa decisão foi necessária porque, com a adoção de HATEOAS, esses objetos passaram a estender `RepresentationModel`, o que não é possível com `record`.

Justificativa técnica:

- `record` não pode herdar de classes concretas ou abstratas além de `java.lang.Record`
- HATEOAS exige comportamento adicional para anexar links ao recurso
- `class` permite herança, inclusão de links e manutenção do contrato de saída enriquecido

Em síntese, `record` foi mantido onde o papel é apenas transportar dados, enquanto `class` foi adotada onde o objeto também representa hipermídia.

### HATEOAS

Foi incorporado HATEOAS nas respostas dos recursos principais com Spring HATEOAS.

As respostas agora expõem links navegaveis para:

- o próprio recurso (`self`)
- a coleção do recurso
- recursos relacionados, como o cliente associado a um pedido

Justificativa técnica e acadêmica:

- reforça aderência ao estilo REST
- reduz dependência do cliente em URIs fixas conhecidas previamente
- melhora descoberta de recursos
- torna o contrato HTTP mais expressivo e evolutivo

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

### Benefícios Arquiteturais Do HATEOAS

- melhora navegabilidade entre recursos da API
- reduz acoplamento entre consumidor e estrutura interna de rotas
- facilita evolução futura da API com menor impacto para clientes
- torna a API mais aderente a um modelo REST rico em hipermídia

## Análise De Aderência Ao Estilo RESTful

A aplicação pode ser considerada uma API REST em sentido prático e está próxima de uma API RESTful mais completa.

### Elementos Que Aproximam A API Do Estilo RESTful

- recursos identificados por URIs claras, como `/clientes`, `/produtos` e `/pedidos`
- uso consistente dos métodos HTTP `GET`, `POST`, `PUT` e `DELETE`
- respostas com códigos HTTP semânticos, como `200`, `201`, `204`, `400`, `404` e `409`
- uso de JSON como representação dos recursos
- ausência de estado de sessão entre requisições
- uso de HATEOAS para enriquecer respostas com links navegáveis

### Pontos Que Impedem Classificar A API Como Totalmente RESTful Em Sentido Purista

- alguns endpoints seguem abordagem pragmática e não totalmente orientada a filtragem por query string, como:
  - `/clientes/nome/{nome}`
  - `/produtos/nome/{nome}`
  - `/pedidos/status/{status}`
- endpoints como `/clientes/contar`, `/produtos/contar` e `/pedidos/contar` são úteis e claros, mas fogem de uma modelagem REST mais estrita, que tenderia a tratar contagem como metadado de coleção ou resultado de consulta
- a API não implementa negociação avançada de mídia além do suporte JSON/HAL básico
- não há versionamento explícito de API, o que é comum em APIs REST maduras

### Conclusão

A API adota os princípios centrais de uma solução RESTful e se encontra em um nível alto de aderência para uso acadêmico e profissional comum. Ainda assim, algumas decisões de design foram mantidas de forma pragmática para privilegiar clareza, simplicidade de consumo e objetividade da entrega.

## Artefatos De Arquitetura

Os artefatos de documentação arquitetural estão na pasta `doc`:

- [ENTREGAVEIS.md](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\03-entrega\ENTREGAVEIS.md)
- [ARQUITETURA-C4.md](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\ARQUITETURA-C4.md)
- [arquitetura-contexto.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\arquitetura-contexto.png)
- [arquitetura-containers.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\arquitetura-containers.png)
- [arquitetura-componentes.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\arquitetura-componentes.png)
- [sequencia](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\sequencia)
- [01-criar-cliente.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\sequencia\01-criar-cliente.png)
- [02-criar-produto.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\sequencia\02-criar-produto.png)
- [03-criar-pedido.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\sequencia\03-criar-pedido.png)
- [04-atualizar-pedido.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\sequencia\04-atualizar-pedido.png)
- [05-listar-clientes-paginado.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\sequencia\05-listar-clientes-paginado.png)
- [06-excluir-cliente-bloqueado.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\sequencia\06-excluir-cliente-bloqueado.png)
- [07-excluir-produto-bloqueado.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\sequencia\07-excluir-produto-bloqueado.png)
- [08-sort-invalido.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\sequencia\08-sort-invalido.png)
- [09-validacao-payload-invalido.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\sequencia\09-validacao-payload-invalido.png)
- [10-criar-cliente-email-duplicado.png](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\sequencia\10-criar-cliente-email-duplicado.png)
- [Enunciado do Desafio Final - Arquiteto(a) de Software.pdf](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\01-enunciado\Enunciado%20do%20Desafio%20Final%20-%20Arquiteto(a)%20de%20Software.pdf)

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

### Parar o container executado manualmente

```bash
docker ps
docker stop <container_id>
```

## Executando Com Docker Compose

### Subir a aplicação

```bash
docker compose up --build
```

### Executar em segundo plano

```bash
docker compose up --build -d
```

### Parar a aplicação

```bash
docker compose down
```

### Arquivo de orquestração

- [docker-compose.yml](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\docker-compose.yml)
  - sobe a aplicação utilizando o `Dockerfile` do projeto
  - publica a porta `8080`

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

Cobertura da collection:

- `{{baseUrl}} = http://localhost:8080`
- pasta `Clientes` com CRUD, busca por nome, contagem, paginação e cenários de erro
- pasta `Produtos` com CRUD, busca por nome, contagem, paginação e cenários de erro
- pasta `Pedidos` com CRUD, busca por status, contagem, paginação e cenários de erro

Variáveis utilizadas:

- `{{baseUrl}}`
- `{{clienteCriadoId}}`
- `{{produtoCriadoId}}`
- `{{pedidoCriadoId}}`

Comportamento da collection:

- os requests `Criar Cliente`, `Criar Produto` e `Criar Pedido` salvam automaticamente o ID criado nas variáveis da collection
- os requests `Atualizar ... Criado` e `Excluir ... Criado` dependem dessas variáveis e devem ser executados após o respectivo `POST`
- o `POST /clientes` usa email dinâmico com `{{$timestamp}}` para permitir reexecução sem conflito de unicidade

Ordem recomendada para demonstração:

1. executar as listagens, buscas e contagens
2. executar o `POST` de cada domínio
3. executar o `PUT` e o `DELETE` do recurso recém-criado
4. executar os cenários negativos de `400`, `404`, `409` e `sort` inválido

Validação realizada:

- a collection foi executada contra a aplicação local em `2026-03-26`
- todos os fluxos testados retornaram o status esperado, incluindo `200`, `201`, `204`, `400`, `404` e `409`

## Console H2

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:clientesdb`
- User: `sa`
- Password: em branco

## Endpoints Disponíveis

Os endpoints de listagem e busca paginada aceitam os parâmetros opcionais:

- `page`: número da página, iniciando em `0`
- `size`: quantidade de registros por página
- `sort`: campo e direção de ordenação, por exemplo `sort=id,asc`

Exemplos:

- `GET /clientes?page=0&size=10&sort=id,asc`
- `GET /produtos?page=1&size=5`
- `GET /pedidos/status/CRIADO?page=0&size=3`

No Swagger, o parâmetro `sort` não deve ficar como `string`. Use sempre um campo real da entidade, por exemplo:

- `sort=id,asc`
- `sort=nome,desc`
- `sort=dataCriacao,desc`

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

## Paginação

As operações de listagem de `clientes`, `produtos` e `pedidos`, bem como as buscas por `nome` e `status`, retornam payload HAL paginado com:

- `_embedded`: coleção de recursos da página atual
- `_links`: links de navegação, como `self`, `next` e `prev`
- `page`: metadados com número da página, tamanho, total de elementos e total de páginas

Essa decisão melhora escalabilidade da API, evita retorno de coleções excessivamente grandes e torna a navegação entre páginas explícita para o consumidor.

## Uso De HATEOAS

As respostas de recursos individuais incluem `_links` e as respostas de coleção incluem `_embedded`, `_links` e, quando aplicável, metadados de paginação.

### Exemplo De Recurso Individual

```json
{
  "id": 1,
  "nome": "Ana Silva",
  "email": "ana.silva@exemplo.com",
  "_links": {
    "self": {
      "href": "http://localhost:8080/clientes/1"
    },
    "clientes": {
      "href": "http://localhost:8080/clientes"
    },
    "contagem": {
      "href": "http://localhost:8080/clientes/contar"
    }
  }
}
```

### Exemplo De Coleção

```json
{
  "_embedded": {
    "produtos": [
      {
        "id": 1,
        "nome": "Notebook",
        "descricao": "Notebook de alta performance",
        "preco": 4500.00,
        "_links": {
          "self": {
            "href": "http://localhost:8080/produtos/1"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/produtos"
    },
    "next": {
      "href": "http://localhost:8080/produtos?page=1&size=10"
    }
  },
  "page": {
    "size": 10,
    "totalElements": 100,
    "totalPages": 10,
    "number": 0
  }
}
```

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
