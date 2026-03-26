package br.com.posxp.clientesapi.service;

import br.com.posxp.clientesapi.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

    Page<Cliente> listarTodos(Pageable pageable);

    Cliente buscarPorId(Long id);

    Page<Cliente> buscarPorNome(String nome, Pageable pageable);

    Cliente salvar(Cliente cliente);

    Cliente atualizar(Long id, Cliente clienteAtualizado);

    void deletar(Long id);

    long contarClientes();
}
