package br.com.posxp.clientesapi.service;

import br.com.posxp.clientesapi.exception.OperacaoNaoPermitidaException;
import br.com.posxp.clientesapi.exception.RecursoNaoEncontradoException;
import br.com.posxp.clientesapi.model.Produto;
import br.com.posxp.clientesapi.repository.ItemPedidoRepository;
import br.com.posxp.clientesapi.repository.ProdutoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository, ItemPedidoRepository itemPedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Override
    public Page<Produto> listarTodos(Pageable pageable) {
        log.debug("Consultando produtos paginados. page={}, size={}, sort={}.",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return produtoRepository.findAll(pageable);
    }

    @Override
    public Produto buscarPorId(Long id) {
        log.debug("Consultando produto por id={} no repositorio.", id);
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto com id " + id + " nao encontrado."));
    }

    @Override
    public Page<Produto> buscarPorNome(String nome, Pageable pageable) {
        log.debug("Consultando produtos por nome contendo '{}'. page={}, size={}.",
                nome, pageable.getPageNumber(), pageable.getPageSize());
        return produtoRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    @Override
    public Produto salvar(Produto produto) {
        log.debug("Persistindo novo produto com nome={}.", produto.getNome());
        return produtoRepository.save(produto);
    }

    @Override
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        log.debug("Atualizando produto id={}.", id);
        Produto produtoExistente = buscarPorId(id);
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        return produtoRepository.save(produtoExistente);
    }

    @Override
    public void deletar(Long id) {
        log.debug("Removendo produto id={}.", id);
        Produto produto = buscarPorId(id);
        if (itemPedidoRepository.existsByProdutoId(id)) {
            throw new OperacaoNaoPermitidaException(
                    "Produto nao pode ser removido porque possui itens de pedido associados."
            );
        }
        produtoRepository.delete(produto);
    }

    @Override
    public long contarProdutos() {
        log.debug("Contando produtos cadastrados.");
        return produtoRepository.count();
    }
}
