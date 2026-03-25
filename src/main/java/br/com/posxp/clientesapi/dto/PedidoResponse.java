package br.com.posxp.clientesapi.dto;

import br.com.posxp.clientesapi.model.PedidoStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        Long clienteId,
        String clienteNome,
        PedidoStatus status,
        BigDecimal total,
        LocalDateTime dataCriacao,
        List<PedidoItemResponse> itens
) {
}
