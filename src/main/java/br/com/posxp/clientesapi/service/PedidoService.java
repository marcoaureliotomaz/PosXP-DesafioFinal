package br.com.posxp.clientesapi.service;

import br.com.posxp.clientesapi.dto.PedidoRequest;
import br.com.posxp.clientesapi.model.Pedido;
import br.com.posxp.clientesapi.model.PedidoStatus;
import java.util.List;

public interface PedidoService {

    List<Pedido> listarTodos();

    Pedido buscarPorId(Long id);

    List<Pedido> buscarPorStatus(PedidoStatus status);

    Pedido salvar(PedidoRequest request);

    Pedido atualizar(Long id, PedidoRequest request);

    void deletar(Long id);

    long contarPedidos();
}
