package br.com.posxp.clientesapi.controller;

import br.com.posxp.clientesapi.dto.ClienteRequest;
import br.com.posxp.clientesapi.dto.ClienteResponse;
import br.com.posxp.clientesapi.model.Cliente;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static Cliente toEntity(ClienteRequest request) {
        return new Cliente(null, request.nome(), request.email());
    }

    public static ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getEmail());
    }
}

