package br.com.posxp.clientesapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.posxp.clientesapi.model.Produto;
import br.com.posxp.clientesapi.repository.ItemPedidoRepository;
import br.com.posxp.clientesapi.repository.ProdutoRepository;
import java.math.BigDecimal;
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
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto(1L, "Notebook", "Notebook de alta performance", new BigDecimal("4500.00"));
    }

    @Test
    void deveListarTodosOrdenadosPorId() {
        List<Produto> produtos = List.of(produto);
        when(produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))).thenReturn(produtos);

        List<Produto> resultado = produtoService.listarTodos();

        assertEquals(produtos, resultado);
        verify(produtoRepository).findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Test
    void deveBuscarPorIdQuandoProdutoExistir() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        Produto resultado = produtoService.buscarPorId(1L);

        assertSame(produto, resultado);
        verify(produtoRepository).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistirPorId() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException ex = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> produtoService.buscarPorId(99L)
        );

        assertEquals("Produto com id 99 nao encontrado.", ex.getMessage());
    }

    @Test
    void deveBuscarProdutosPorNome() {
        List<Produto> produtos = List.of(produto);
        when(produtoRepository.findByNomeContainingIgnoreCase("Note")).thenReturn(produtos);

        List<Produto> resultado = produtoService.buscarPorNome("Note");

        assertEquals(produtos, resultado);
        verify(produtoRepository).findByNomeContainingIgnoreCase("Note");
    }

    @Test
    void deveSalvarProduto() {
        when(produtoRepository.save(produto)).thenReturn(produto);

        Produto resultado = produtoService.salvar(produto);

        assertSame(produto, resultado);
        verify(produtoRepository).save(produto);
    }

    @Test
    void deveAtualizarProdutoExistente() {
        Produto atualizado = new Produto(null, "Notebook Atualizado", "Descricao atualizada", new BigDecimal("4999.99"));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Produto resultado = produtoService.atualizar(1L, atualizado);

        assertEquals(1L, resultado.getId());
        assertEquals("Notebook Atualizado", resultado.getNome());
        assertEquals("Descricao atualizada", resultado.getDescricao());
        assertEquals(new BigDecimal("4999.99"), resultado.getPreco());
        verify(produtoRepository).save(produto);
    }

    @Test
    void deveDeletarProdutoExistente() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(itemPedidoRepository.existsByProdutoId(1L)).thenReturn(false);

        produtoService.deletar(1L);

        verify(produtoRepository).delete(produto);
    }

    @Test
    void naoDeveDeletarQuandoProdutoNaoExistir() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> produtoService.deletar(1L));

        verify(produtoRepository, never()).delete(any());
    }

    @Test
    void naoDeveDeletarProdutoQuandoPossuirItensDePedidoAssociados() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(itemPedidoRepository.existsByProdutoId(1L)).thenReturn(true);

        OperacaoNaoPermitidaException ex = assertThrows(
                OperacaoNaoPermitidaException.class,
                () -> produtoService.deletar(1L)
        );

        assertEquals("Produto nao pode ser removido porque possui itens de pedido associados.", ex.getMessage());
        verify(produtoRepository, never()).delete(any());
    }

    @Test
    void deveContarProdutos() {
        when(produtoRepository.count()).thenReturn(3L);

        long total = produtoService.contarProdutos();

        assertEquals(3L, total);
        verify(produtoRepository).count();
    }
}
