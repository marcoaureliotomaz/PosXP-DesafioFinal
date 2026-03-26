package br.com.posxp.clientesapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.posxp.clientesapi.dto.PedidoItemRequest;
import br.com.posxp.clientesapi.dto.PedidoRequest;
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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Cliente cliente;
    private Produto produto1;
    private Produto produto2;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "Ana Silva", "ana.silva@exemplo.com");
        produto1 = new Produto(1L, "Notebook", "Notebook", new BigDecimal("4500.00"));
        produto2 = new Produto(2L, "Mouse", "Mouse", new BigDecimal("120.00"));

        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCliente(cliente);
        pedido.setStatus(PedidoStatus.CRIADO);
        pedido.setTotal(new BigDecimal("4620.00"));
        pedido.setDataCriacao(LocalDateTime.of(2026, 3, 25, 10, 0));
        pedido.setItens(new ArrayList<>(List.of(
                new ItemPedido(1L, pedido, produto1, 1, produto1.getPreco(), produto1.getPreco()),
                new ItemPedido(2L, pedido, produto2, 1, produto2.getPreco(), produto2.getPreco())
        )));
    }

    @Test
    void deveListarTodosOrdenadosPorId() {
        when(pedidoRepository.findAllWithDetalhes(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(List.of(pedido));

        List<Pedido> resultado = pedidoService.listarTodos();

        assertEquals(1, resultado.size());
        verify(pedidoRepository).findAllWithDetalhes(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Test
    void deveBuscarPedidoPorIdQuandoExistir() {
        when(pedidoRepository.findByIdWithDetalhes(1L)).thenReturn(Optional.of(pedido));

        Pedido resultado = pedidoService.buscarPorId(1L);

        assertSame(pedido, resultado);
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoExistirPorId() {
        when(pedidoRepository.findByIdWithDetalhes(99L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> pedidoService.buscarPorId(99L)
        );

        assertEquals("Pedido com id 99 nao encontrado.", ex.getMessage());
    }

    @Test
    void deveBuscarPedidosPorStatus() {
        when(pedidoRepository.findByStatusWithDetalhes(PedidoStatus.CRIADO, Sort.by(Sort.Direction.ASC, "id")))
                .thenReturn(List.of(pedido));

        List<Pedido> resultado = pedidoService.buscarPorStatus(PedidoStatus.CRIADO);

        assertEquals(1, resultado.size());
    }

    @Test
    void deveSalvarPedidoCalculandoTotalEStatusPadrao() {
        PedidoRequest request = new PedidoRequest(
                1L,
                null,
                List.of(new PedidoItemRequest(1L, 1), new PedidoItemRequest(2L, 2))
        );
        AtomicReference<Pedido> pedidoSalvo = new AtomicReference<>();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto1));
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(produto2));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido novoPedido = invocation.getArgument(0);
            novoPedido.setId(2L);
            pedidoSalvo.set(novoPedido);
            return novoPedido;
        });
        when(pedidoRepository.findByIdWithDetalhes(2L)).thenAnswer(invocation -> Optional.of(pedidoSalvo.get()));

        Pedido resultado = pedidoService.salvar(request);

        assertEquals(2L, resultado.getId());
        assertEquals(PedidoStatus.CRIADO, resultado.getStatus());
        assertEquals(new BigDecimal("4740.00"), resultado.getTotal());
        assertEquals(2, resultado.getItens().size());
    }

    @Test
    void deveAtualizarPedidoExistente() {
        PedidoRequest request = new PedidoRequest(
                1L,
                PedidoStatus.PAGO,
                List.of(new PedidoItemRequest(2L, 3))
        );
        AtomicReference<Pedido> pedidoAtualizado = new AtomicReference<>();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(produto2));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> {
            Pedido pedidoPersistido = invocation.getArgument(0);
            pedidoAtualizado.set(pedidoPersistido);
            return pedidoPersistido;
        });
        when(pedidoRepository.findByIdWithDetalhes(1L)).thenAnswer(invocation -> Optional.of(pedidoAtualizado.get()));

        Pedido resultado = pedidoService.atualizar(1L, request);

        assertEquals(PedidoStatus.PAGO, resultado.getStatus());
        assertEquals(new BigDecimal("360.00"), resultado.getTotal());
        assertEquals(1, resultado.getItens().size());
        assertEquals(3, resultado.getItens().get(0).getQuantidade());
    }

    @Test
    void deveDeletarPedidoExistente() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.deletar(1L);

        verify(pedidoRepository).delete(pedido);
    }

    @Test
    void deveContarPedidos() {
        when(pedidoRepository.count()).thenReturn(1L);

        long total = pedidoService.contarPedidos();

        assertEquals(1L, total);
    }
}
