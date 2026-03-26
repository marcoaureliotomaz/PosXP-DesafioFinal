package br.com.posxp.clientesapi.mapper;

import br.com.posxp.clientesapi.dto.ProdutoRequest;
import br.com.posxp.clientesapi.dto.ProdutoResponse;
import br.com.posxp.clientesapi.controller.ProdutoController;
import br.com.posxp.clientesapi.model.Produto;
import org.springframework.data.domain.PageRequest;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public final class ProdutoMapper {

    private ProdutoMapper() {
    }

    public static Produto toEntity(ProdutoRequest request) {
        return new Produto(null, request.nome(), request.descricao(), request.preco());
    }

    public static ProdutoResponse toResponse(Produto produto) {
        ProdutoResponse response = new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco()
        );
        response.add(linkTo(methodOn(ProdutoController.class).buscarPorId(produto.getId())).withSelfRel());
        response.add(linkTo(methodOn(ProdutoController.class).listarTodos(PageRequest.of(0, 10))).withRel("produtos"));
        response.add(linkTo(methodOn(ProdutoController.class).contarProdutos()).withRel("contagem"));
        return response;
    }
}
