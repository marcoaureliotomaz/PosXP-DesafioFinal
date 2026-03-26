package br.com.posxp.clientesapi.builder;

import br.com.posxp.clientesapi.model.Cliente;
import br.com.posxp.clientesapi.model.ItemPedido;
import br.com.posxp.clientesapi.model.Pedido;
import br.com.posxp.clientesapi.model.PedidoStatus;
import br.com.posxp.clientesapi.model.Produto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class PedidoBuilder {

    private final Pedido pedido;
    private final List<ItemPedido> itens = new ArrayList<>();
    private BigDecimal total = BigDecimal.ZERO;

    private PedidoBuilder(Pedido pedido) {
        this.pedido = pedido;
    }

    public static PedidoBuilder novo(Cliente cliente, PedidoStatus status, LocalDateTime dataCriacao) {
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(status);
        pedido.setDataCriacao(dataCriacao);
        return new PedidoBuilder(pedido);
    }

    public static PedidoBuilder paraAtualizacao(Pedido pedido, Cliente cliente, PedidoStatus status) {
        pedido.setCliente(cliente);
        pedido.setStatus(status);
        return new PedidoBuilder(pedido);
    }

    public PedidoBuilder adicionarItem(Produto produto, Integer quantidade) {
        BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());
        item.setSubtotal(subtotal);

        itens.add(item);
        total = total.add(subtotal);
        return this;
    }

    public Pedido build() {
        pedido.getItens().clear();
        pedido.getItens().addAll(itens);
        pedido.setTotal(total);
        return pedido;
    }
}
