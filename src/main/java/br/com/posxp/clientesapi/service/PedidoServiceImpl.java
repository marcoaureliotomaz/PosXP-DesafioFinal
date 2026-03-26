package br.com.posxp.clientesapi.service;

import br.com.posxp.clientesapi.builder.PedidoBuilder;
import br.com.posxp.clientesapi.dto.PedidoItemRequest;
import br.com.posxp.clientesapi.dto.PedidoRequest;
import br.com.posxp.clientesapi.exception.RecursoNaoEncontradoException;
import br.com.posxp.clientesapi.model.Cliente;
import br.com.posxp.clientesapi.model.Pedido;
import br.com.posxp.clientesapi.model.PedidoStatus;
import br.com.posxp.clientesapi.model.Produto;
import br.com.posxp.clientesapi.repository.ClienteRepository;
import br.com.posxp.clientesapi.repository.PedidoRepository;
import br.com.posxp.clientesapi.repository.ProdutoRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoServiceImpl(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            ProdutoRepository produtoRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        log.debug("Consultando todos os pedidos ordenados por id.");
        return pedidoRepository.findAllWithDetalhes(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        log.debug("Consultando pedido por id={} no repositorio.", id);
        return pedidoRepository.findByIdWithDetalhes(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido com id " + id + " nao encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorStatus(PedidoStatus status) {
        log.debug("Consultando pedidos por status={}.", status);
        return pedidoRepository.findByStatusWithDetalhes(status, Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    @Transactional
    public Pedido salvar(PedidoRequest request) {
        log.debug("Persistindo novo pedido para clienteId={}.", request.clienteId());
        Cliente cliente = buscarCliente(request.clienteId());
        Pedido pedido = preencherItens(
                PedidoBuilder.novo(
                        cliente,
                        request.status() != null ? request.status() : PedidoStatus.CRIADO,
                        LocalDateTime.now()
                ),
                request.itens()
        ).build();
        Pedido salvo = pedidoRepository.save(pedido);
        return buscarPorId(salvo.getId());
    }

    @Override
    @Transactional
    public Pedido atualizar(Long id, PedidoRequest request) {
        log.debug("Atualizando pedido id={}.", id);
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido com id " + id + " nao encontrado."));
        Cliente cliente = buscarCliente(request.clienteId());
        Pedido atualizadoParaSalvar = preencherItens(
                PedidoBuilder.paraAtualizacao(
                        pedidoExistente,
                        cliente,
                        request.status() != null ? request.status() : pedidoExistente.getStatus()
                ),
                request.itens()
        ).build();
        Pedido atualizado = pedidoRepository.save(atualizadoParaSalvar);
        return buscarPorId(atualizado.getId());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        log.debug("Removendo pedido id={}.", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido com id " + id + " nao encontrado."));
        pedidoRepository.delete(pedido);
    }

    @Override
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

    private PedidoBuilder preencherItens(PedidoBuilder builder, List<PedidoItemRequest> itensRequest) {
        for (PedidoItemRequest itemRequest : itensRequest) {
            Produto produto = buscarProduto(itemRequest.produtoId());
            builder.adicionarItem(produto, itemRequest.quantidade());
        }
        return builder;
    }
}
