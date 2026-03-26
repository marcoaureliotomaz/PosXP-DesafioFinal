package br.com.posxp.clientesapi.dto;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@EqualsAndHashCode(callSuper = true)
@Relation(itemRelation = "produto", collectionRelation = "produtos")
public class ProdutoResponse extends RepresentationModel<ProdutoResponse> {

    private final Long id;
    private final String nome;
    private final String descricao;
    private final BigDecimal preco;

    public ProdutoResponse(Long id, String nome, String descricao, BigDecimal preco) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }
}
