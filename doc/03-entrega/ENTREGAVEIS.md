# Entregaveis Finais

## Itens Do Desafio Atendidos

- API REST no padrao MVC
- CRUD completo dos dominios `Cliente`, `Produto` e `Pedido`
- endpoint de listagem paginada dos registros
- endpoint de busca por ID
- endpoint de busca por nome para `Cliente` e `Produto`
- endpoint de busca por status para `Pedido`
- endpoint de contagem total
- persistencia com Spring Data JPA e H2
- validacao de dados de entrada
- tratamento padronizado de erros
- documentacao da API com Swagger / OpenAPI
- observabilidade com logs e Actuator
- testes automatizados
- execucao via Docker
- collection do Postman
- diagramas C4 de contexto, containers e componentes
- diagramas de sequencia dos principais fluxos

## Artefatos Do Projeto

- [README.md](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/README.md)
  - visao geral da solucao
  - instrucoes de execucao
  - endpoints
  - decisoes arquiteturais
  - justificativas tecnicas

- [ARQUITETURA-C4.md](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/ARQUITETURA-C4.md)
  - descricao textual do modelo arquitetural
  - base descritiva dos diagramas finais

- [Enunciado do Desafio Final - Arquiteto(a) de Software.pdf](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/01-enunciado/Enunciado%20do%20Desafio%20Final%20-%20Arquiteto(a)%20de%20Software.pdf)
  - documento original do desafio

- [arquitetura-contexto.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/arquitetura-contexto.png)
  - diagrama C4 de contexto exportado

- [arquitetura-containers.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/arquitetura-containers.png)
  - diagrama C4 de containers exportado

- [arquitetura-componentes.png](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/arquitetura-componentes.png)
  - diagrama C4 de componentes exportado

- [sequencia](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/doc/02-arquitetura/sequencia)
  - diagramas de sequencia dos principais fluxos da aplicacao

- [Cliente-API.postman_collection.json](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/Cliente-API.postman_collection.json)
  - collection pronta para testes funcionais

- [docker-compose.yml](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/docker-compose.yml)
  - orquestracao simplificada da aplicacao

- [Dockerfile](C:/Gryphem/Projetos/PosXP/PosXP-DesafioFinal/Dockerfile)
  - empacotamento da aplicacao

## Decisoes Arquiteturais Relevantes

- uso do padrao `MVC`
- uso do padrao `Repository`
- uso de `DTOs` e `Mappers`
- uso de `Service Layer`
- interfaces de servico com implementacoes concretas
- aplicacao de `Dependency Inversion` na camada de servicos
- uso de `Builder` para montagem do agregado `Pedido`
- tratamento centralizado de excecoes
- observabilidade via logs e Actuator
- uso de `HATEOAS` nas respostas da API

## Ajustes Nos Diagramas C4

Os diagramas C4 foram refinados para refletir melhor a implementacao real do sistema:

- no nivel de containers, `Swagger UI` e `Actuator` foram tratados como capacidades da aplicacao Spring Boot, e nao como containers independentes
- no nivel de componentes, o foco foi mantido em elementos arquiteturalmente relevantes: controllers, services, mappers, repositories, `PedidoBuilder` e `ApiExceptionHandler`
- componentes tecnicos secundarios, como classes de configuracao, deixaram de ser exibidos como componentes centrais

## Justificativa Academica

Essas decisoes demonstram preocupacao com:

- baixo acoplamento
- alta coesao
- separacao de responsabilidades
- manutenibilidade
- testabilidade
- rastreabilidade da execucao
- clareza na documentacao da arquitetura
- coerencia entre contrato HTTP e modelo de representacao

## Evidencias Tecnicas

- testes automatizados executados com sucesso via `mvn test`
- cobertura gerada via JaCoCo
- documentacao interativa disponivel com OpenAPI
- monitoramento operacional disponivel com Actuator
- empacotamento e execucao em container

## Sugestao De Pacote Para Entrega

1. Repositorio Git com historico de commits
2. `README.md` atualizado
3. diagramas visuais em `.png` presentes em `doc/02-arquitetura`
4. collection Postman para demonstracao
5. evidencias de execucao local, Swagger e testes
