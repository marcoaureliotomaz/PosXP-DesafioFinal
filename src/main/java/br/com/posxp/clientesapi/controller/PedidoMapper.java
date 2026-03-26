package br.com.posxp.clientesapi.controller;

import br.com.posxp.clientesapi.dto.PedidoItemResponse;
import br.com.posxp.clientesapi.dto.PedidoResponse;
import br.com.posxp.clientesapi.model.ItemPedido;
import br.com.posxp.clientesapi.model.Pedido;

public final class PedidoMapper {

    private PedidoMapper() {
    }

    public static PedidoResponse toResponse(Pedido pedido) {
        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getStatus(),
                pedido.getTotal(),
                pedido.getDataCriacao(),
                pedido.getItens().stream().map(PedidoMapper::toItemResponse).toList()
        );
    }

    private static PedidoItemResponse toItemResponse(ItemPedido item) {
        return new PedidoItemResponse(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getSubtotal()
        );
    }
}
