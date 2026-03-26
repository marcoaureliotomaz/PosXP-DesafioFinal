package br.com.posxp.clientesapi.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.hateoas.RepresentationModel;

@Getter
@EqualsAndHashCode(callSuper = true)
@Relation(itemRelation = "cliente", collectionRelation = "clientes")
public class ClienteResponse extends RepresentationModel<ClienteResponse> {

    private final Long id;
    private final String nome;
    private final String email;

    public ClienteResponse(Long id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }
}
