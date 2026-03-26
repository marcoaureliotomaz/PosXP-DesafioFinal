package br.com.posxp.clientesapi.service;

import br.com.posxp.clientesapi.exception.OperacaoNaoPermitidaException;
import br.com.posxp.clientesapi.exception.RecursoNaoEncontradoException;
import br.com.posxp.clientesapi.model.Produto;
import br.com.posxp.clientesapi.repository.ItemPedidoRepository;
import br.com.posxp.clientesapi.repository.ProdutoRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public ProdutoService(ProdutoRepository produtoRepository, ItemPedidoRepository itemPedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public List<Produto> listarTodos() {
        log.debug("Consultando todos os produtos ordenados por id.");
        return produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Produto buscarPorId(Long id) {
        log.debug("Consultando produto por id={} no repositorio.", id);
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto com id " + id + " nao encontrado."));
    }

    public List<Produto> buscarPorNome(String nome) {
        log.debug("Consultando produtos por nome contendo '{}'.", nome);
        return produtoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Produto salvar(Produto produto) {
        log.debug("Persistindo novo produto com nome={}.", produto.getNome());
        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        log.debug("Atualizando produto id={}.", id);
        Produto produtoExistente = buscarPorId(id);
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        return produtoRepository.save(produtoExistente);
    }

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

    public long contarProdutos() {
        log.debug("Contando produtos cadastrados.");
        return produtoRepository.count();
    }
}
