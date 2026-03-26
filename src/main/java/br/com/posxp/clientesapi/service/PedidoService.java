package br.com.posxp.clientesapi.service;

import br.com.posxp.clientesapi.dto.PedidoItemRequest;
import br.com.posxp.clientesapi.dto.PedidoRequest;
import br.com.posxp.clientesapi.exception.RecursoNaoEncontradoException;
import br.com.posxp.clientesapi.model.Cliente;
import br.com.posxp.clientesapi.model.ItemPedido;
import br.com.posxp.clientesapi.model.Pedido;
import br.com.posxp.clientesapi.model.PedidoStatus;
import br.com.posxp.clientesapi.model.Produto;
import br.com.posxp.clientesapi.repository.ClienteRepository;
import br.com.posxp.clientesapi.repository.PedidoRepository;
import br.com.posxp.clientesapi.repository.ProdutoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        log.debug("Consultando todos os pedidos ordenados por id.");
        return pedidoRepository.findAllWithDetalhes(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        log.debug("Consultando pedido por id={} no repositorio.", id);
        return pedidoRepository.findByIdWithDetalhes(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido com id " + id + " nao encontrado."));
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorStatus(PedidoStatus status) {
        log.debug("Consultando pedidos por status={}.", status);
        return pedidoRepository.findByStatusWithDetalhes(status, Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional
    public Pedido salvar(PedidoRequest request) {
        log.debug("Persistindo novo pedido para clienteId={}.", request.clienteId());
        Cliente cliente = buscarCliente(request.clienteId());
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(request.status() != null ? request.status() : PedidoStatus.CRIADO);
        pedido.setDataCriacao(LocalDateTime.now());
        preencherItens(pedido, request.itens());
        Pedido salvo = pedidoRepository.save(pedido);
        return buscarPorId(salvo.getId());
    }

    @Transactional
    public Pedido atualizar(Long id, PedidoRequest request) {
        log.debug("Atualizando pedido id={}.", id);
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido com id " + id + " nao encontrado."));
        Cliente cliente = buscarCliente(request.clienteId());
        pedidoExistente.setCliente(cliente);
        if (request.status() != null) {
            pedidoExistente.setStatus(request.status());
        }
        preencherItens(pedidoExistente, request.itens());
        Pedido atualizado = pedidoRepository.save(pedidoExistente);
        return buscarPorId(atualizado.getId());
    }

    @Transactional
    public void deletar(Long id) {
        log.debug("Removendo pedido id={}.", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido com id " + id + " nao encontrado."));
        pedidoRepository.delete(pedido);
    }

    public long contarPedidos() {
        log.debug("Contando pedidos cadastrados.");
        return pedidoRepository.count();
    }

    private Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente com id " + clienteId + " nao encontrado."
                ));
    }

    private Produto buscarProduto(Long produtoId) {
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Produto com id " + produtoId + " nao encontrado."
                ));
    }

    private void preencherItens(Pedido pedido, List<PedidoItemRequest> itensRequest) {
        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (PedidoItemRequest itemRequest : itensRequest) {
            Produto produto = buscarProduto(itemRequest.produtoId());
            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemRequest.quantidade()));

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemRequest.quantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setSubtotal(subtotal);
            itens.add(item);
            total = total.add(subtotal);
        }

        pedido.getItens().clear();
        pedido.getItens().addAll(itens);
        pedido.setTotal(total);
    }
}
