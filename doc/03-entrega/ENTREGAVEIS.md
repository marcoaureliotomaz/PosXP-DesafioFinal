# Entregáveis Finais

## Itens Do Desafio Atendidos

- API REST no padrão MVC
- CRUD completo dos domínios `Cliente`, `Produto` e `Pedido`
- endpoint de listagem total dos registros
- endpoint de busca por ID
- endpoint de busca por nome para `Cliente` e `Produto`
- endpoint de busca por status para `Pedido`
- endpoint de contagem total
- persistência com Spring Data JPA e H2
- validação de dados de entrada
- tratamento padronizado de erros
- documentação da API com Swagger / OpenAPI
- observabilidade com logs e Actuator
- testes automatizados
- execução via Docker
- collection do Postman

## Artefatos Do Projeto

- [README.md](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\README.md)
  - visão geral da solução
  - instruções de execução
  - endpoints
  - decisões arquiteturais
  - justificativas técnicas

- [ARQUITETURA-C4.md](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\02-arquitetura\ARQUITETURA-C4.md)
  - descrição textual do modelo arquitetural
  - base para diagrama visual em draw.io

- [Enunciado do Desafio Final - Arquiteto(a) de Software.pdf](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\doc\01-enunciado\Enunciado%20do%20Desafio%20Final%20-%20Arquiteto(a)%20de%20Software.pdf)
  - documento original do desafio

- [Cliente-API.postman_collection.json](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\Cliente-API.postman_collection.json)
  - collection pronta para testes funcionais

- [Dockerfile](c:\Gryphem\Projetos\PosXP\PosXP-DesafioFinal\Dockerfile)
  - empacotamento da aplicação

## Decisões Arquiteturais Relevantes

- uso do padrão `MVC`
- uso do padrão `Repository`
- uso de `DTOs` e `Mappers`
- uso de `Service Layer`
- interfaces de serviço com implementações concretas
- aplicação de `Dependency Inversion` na camada de serviços
- uso de `Builder` para montagem do agregado `Pedido`
- tratamento centralizado de exceções
- observabilidade via logs e Actuator
- uso de `HATEOAS` nas respostas da API
- uso de `class` nos DTOs de saída enriquecidos por hipermídia

## Justificativa Acadêmica

Essas decisões demonstram preocupação com:

- baixo acoplamento
- alta coesão
- separação de responsabilidades
- manutenibilidade
- testabilidade
- rastreabilidade da execução
- clareza na documentação da arquitetura
- coerência entre contrato HTTP e modelo de representação

## Sugestão De Pacote Para Entrega

1. Repositório Git com histórico de commits
2. `README.md` atualizado
3. diagrama visual em draw.io, png ou pdf com base em `doc/02-arquitetura/ARQUITETURA-C4.md`
4. collection Postman para demonstração
5. evidências de execução local, Swagger e testes

## Pendências Opcionais Para Fortalecer Ainda Mais

- exportar o diagrama arquitetural para `.png` ou `.pdf`
- incluir imagens do Swagger e da execução dos endpoints
- adicionar diagramas complementares de entidade-relacionamento ou sequência
