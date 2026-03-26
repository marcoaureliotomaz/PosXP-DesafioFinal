package br.com.posxp.clientesapi.service;

import br.com.posxp.clientesapi.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoService {

    Page<Produto> listarTodos(Pageable pageable);

    Produto buscarPorId(Long id);

    Page<Produto> buscarPorNome(String nome, Pageable pageable);

    Produto salvar(Produto produto);

    Produto atualizar(Long id, Produto produtoAtualizado);

    void deletar(Long id);

    long contarProdutos();
}
