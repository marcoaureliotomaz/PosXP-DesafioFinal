package br.com.posxp.clientesapi.controller;

import br.com.posxp.clientesapi.dto.ProdutoRequest;
import br.com.posxp.clientesapi.dto.ProdutoResponse;
import br.com.posxp.clientesapi.model.Produto;

public final class ProdutoMapper {

    private ProdutoMapper() {
    }

    public static Produto toEntity(ProdutoRequest request) {
        return new Produto(null, request.nome(), request.descricao(), request.preco());
    }

    public static ProdutoResponse toResponse(Produto produto) {
        return new ProdutoResponse(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco());
    }
}

