package br.com.posxp.clientesapi.dto;

public record ClienteResponse(
        Long id,
        String nome,
        String email
) {
}

