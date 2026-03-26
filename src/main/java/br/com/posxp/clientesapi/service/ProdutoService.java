package br.com.posxp.clientesapi.service;

import br.com.posxp.clientesapi.model.Produto;
import java.util.List;

public interface ProdutoService {

    List<Produto> listarTodos();

    Produto buscarPorId(Long id);

    List<Produto> buscarPorNome(String nome);

    Produto salvar(Produto produto);

    Produto atualizar(Long id, Produto produtoAtualizado);

    void deletar(Long id);

    long contarProdutos();
}
