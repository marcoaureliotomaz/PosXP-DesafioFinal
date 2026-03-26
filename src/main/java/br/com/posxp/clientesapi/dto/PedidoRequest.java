package br.com.posxp.clientesapi.dto;

import br.com.posxp.clientesapi.model.PedidoStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoRequest(
        @NotNull(message = "O clienteId e obrigatorio.")
        Long clienteId,

        PedidoStatus status,

        @NotEmpty(message = "O pedido deve possuir ao menos um item.")
        List<@Valid PedidoItemRequest> itens
) {
}
