# Diagramas De Sequência

Esta pasta reúne diagramas de sequência para os principais fluxos da aplicação.

## Arquivos

- `01-criar-cliente.puml`
- `01-criar-cliente.mmd`
- `01-criar-cliente.png`
- `02-criar-produto.puml`
- `02-criar-produto.mmd`
- `02-criar-produto.png`
- `03-criar-pedido.puml`
- `03-criar-pedido.mmd`
- `03-criar-pedido.png`
- `04-atualizar-pedido.puml`
- `04-atualizar-pedido.mmd`
- `04-atualizar-pedido.png`
- `05-listar-clientes-paginado.puml`
- `05-listar-clientes-paginado.mmd`
- `05-listar-clientes-paginado.png`
- `06-excluir-cliente-bloqueado.puml`
- `06-excluir-cliente-bloqueado.mmd`
- `06-excluir-cliente-bloqueado.png`
- `07-excluir-produto-bloqueado.puml`
- `07-excluir-produto-bloqueado.mmd`
- `07-excluir-produto-bloqueado.png`
- `08-sort-invalido.puml`
- `08-sort-invalido.mmd`
- `08-sort-invalido.png`
- `09-validacao-payload-invalido.puml`
- `09-validacao-payload-invalido.mmd`
- `09-validacao-payload-invalido.png`
- `10-criar-cliente-email-duplicado.puml`
- `10-criar-cliente-email-duplicado.mmd`
- `10-criar-cliente-email-duplicado.png`

## Objetivo

Os diagramas complementam o C4 ao mostrar o comportamento temporal da aplicação, evidenciando:

- interação entre usuário e API
- passagem pelas camadas `controller`, `service` e `repository`
- uso do `PedidoBuilder`
- tratamento padronizado de exceções
- fluxos de sucesso e de erro
- validação de integridade, como email duplicado em `Cliente`
