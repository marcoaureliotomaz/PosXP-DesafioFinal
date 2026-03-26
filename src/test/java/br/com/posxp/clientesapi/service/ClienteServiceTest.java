package br.com.posxp.clientesapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.posxp.clientesapi.model.Cliente;
import br.com.posxp.clientesapi.repository.ClienteRepository;
import br.com.posxp.clientesapi.repository.PedidoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "Ana Silva", "ana.silva@exemplo.com");
    }

    @Test
    void deveListarTodosOrdenadosPorId() {
        List<Cliente> clientes = List.of(cliente);
        when(clienteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(clientes);

        List<Cliente> resultado = clienteService.listarTodos();

        assertEquals(clientes, resultado);
        verify(clienteRepository).findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Test
    void deveBuscarPorIdQuandoClienteExistir() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarPorId(1L);

        assertSame(cliente, resultado);
        verify(clienteRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistirPorId() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> clienteService.buscarPorId(99L)
        );

        assertEquals("Cliente com id 99 nao encontrado.", ex.getMessage());
    }

    @Test
    void deveBuscarClientesPorNome() {
        List<Cliente> clientes = List.of(cliente);
        when(clienteRepository.findByNomeContainingIgnoreCase("Ana")).thenReturn(clientes);

        List<Cliente> resultado = clienteService.buscarPorNome("Ana");

        assertEquals(clientes, resultado);
        verify(clienteRepository).findByNomeContainingIgnoreCase("Ana");
    }

    @Test
    void deveSalvarCliente() {
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.salvar(cliente);

        assertSame(cliente, resultado);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveAtualizarClienteExistente() {
        Cliente atualizado = new Cliente(null, "Ana Atualizada", "ana.atualizada@exemplo.com");
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cliente resultado = clienteService.atualizar(1L, atualizado);

        assertEquals(1L, resultado.getId());
        assertEquals("Ana Atualizada", resultado.getNome());
        assertEquals("ana.atualizada@exemplo.com", resultado.getEmail());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveDeletarClienteExistente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(pedidoRepository.existsByClienteId(1L)).thenReturn(false);

        clienteService.deletar(1L);

        verify(clienteRepository).delete(cliente);
    }

    @Test
    void naoDeveDeletarQuandoClienteNaoExistir() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> clienteService.deletar(1L));

        verify(clienteRepository, never()).delete(any());
    }

    @Test
    void naoDeveDeletarClienteQuandoPossuirPedidosAssociados() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(pedidoRepository.existsByClienteId(1L)).thenReturn(true);

        OperacaoNaoPermitidaException ex = assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> clienteService.deletar(1L)
        );

        assertEquals("Cliente nao pode ser removido porque possui pedidos associados.", ex.getMessage());
        verify(clienteRepository, never()).delete(any());
    }

    @Test
    void deveContarClientes() {
        when(clienteRepository.count()).thenReturn(3L);

        long total = clienteService.contarClientes();

        assertEquals(3L, total);
        verify(clienteRepository).count();
    }
}
