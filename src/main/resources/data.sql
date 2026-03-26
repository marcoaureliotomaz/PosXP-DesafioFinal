INSERT INTO clientes (nome, email) VALUES ('Ana Silva', 'ana.silva@exemplo.com');
INSERT INTO clientes (nome, email) VALUES ('Bruno Costa', 'bruno.costa@exemplo.com');
INSERT INTO clientes (nome, email) VALUES ('Carla Souza', 'carla.souza@exemplo.com');

INSERT INTO produtos (nome, descricao, preco) VALUES ('Notebook', 'Notebook de alta performance', 4500.00);
INSERT INTO produtos (nome, descricao, preco) VALUES ('Mouse', 'Mouse sem fio', 120.00);
INSERT INTO produtos (nome, descricao, preco) VALUES ('Monitor', 'Monitor ultrawide', 1899.90);

INSERT INTO pedidos (cliente_id, status, total, data_criacao) VALUES (1, 'CRIADO', 6399.90, '2026-03-25 10:00:00');

INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES (1, 1, 1, 4500.00, 4500.00);

INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES (1, 3, 1, 1899.90, 1899.90);
