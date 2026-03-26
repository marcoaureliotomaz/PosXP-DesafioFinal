package br.com.posxp.clientesapi.mapper;

import br.com.posxp.clientesapi.dto.ClienteRequest;
import br.com.posxp.clientesapi.dto.ClienteResponse;
import br.com.posxp.clientesapi.controller.ClienteController;
import br.com.posxp.clientesapi.model.Cliente;
import org.springframework.data.domain.PageRequest;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static Cliente toEntity(ClienteRequest request) {
        return new Cliente(null, request.nome(), request.email());
    }

    public static ClienteResponse toResponse(Cliente cliente) {
        ClienteResponse response = new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getEmail());
        response.add(linkTo(methodOn(ClienteController.class).buscarPorId(cliente.getId())).withSelfRel());
        response.add(linkTo(methodOn(ClienteController.class).listarTodos(PageRequest.of(0, 10))).withRel("clientes"));
        response.add(linkTo(methodOn(ClienteController.class).contarClientes()).withRel("contagem"));
        return response;
    }
}
