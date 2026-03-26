package br.com.posxp.clientesapi.service;

import br.com.posxp.clientesapi.dto.PedidoRequest;
import br.com.posxp.clientesapi.model.Pedido;
import br.com.posxp.clientesapi.model.PedidoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoService {

    Page<Pedido> listarTodos(Pageable pageable);

    Pedido buscarPorId(Long id);

    Page<Pedido> buscarPorStatus(PedidoStatus status, Pageable pageable);

    Pedido salvar(PedidoRequest request);

    Pedido atualizar(Long id, PedidoRequest request);

    void deletar(Long id);

    long contarPedidos();
}
