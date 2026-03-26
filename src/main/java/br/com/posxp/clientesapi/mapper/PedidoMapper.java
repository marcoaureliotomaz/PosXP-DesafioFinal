package br.com.posxp.clientesapi.mapper;

import br.com.posxp.clientesapi.controller.ClienteController;
import br.com.posxp.clientesapi.controller.PedidoController;
import br.com.posxp.clientesapi.dto.PedidoItemResponse;
import br.com.posxp.clientesapi.dto.PedidoResponse;
import br.com.posxp.clientesapi.model.ItemPedido;
import br.com.posxp.clientesapi.model.Pedido;
import org.springframework.data.domain.PageRequest;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public final class PedidoMapper {

    private PedidoMapper() {
    }

    public static PedidoResponse toResponse(Pedido pedido) {
        PedidoResponse response = new PedidoResponse(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getStatus(),
                pedido.getTotal(),
                pedido.getDataCriacao(),
                pedido.getItens().stream().map(PedidoMapper::toItemResponse).toList()
        );
        response.add(linkTo(methodOn(PedidoController.class).buscarPorId(pedido.getId())).withSelfRel());
        response.add(linkTo(methodOn(PedidoController.class).listarTodos(PageRequest.of(0, 10))).withRel("pedidos"));
        response.add(linkTo(methodOn(ClienteController.class).buscarPorId(pedido.getCliente().getId())).withRel("cliente"));
        return response;
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
