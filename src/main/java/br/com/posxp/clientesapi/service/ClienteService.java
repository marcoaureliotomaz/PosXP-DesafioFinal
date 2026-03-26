package br.com.posxp.clientesapi.service;

import br.com.posxp.clientesapi.model.Cliente;
import java.util.List;

public interface ClienteService {

    List<Cliente> listarTodos();

    Cliente buscarPorId(Long id);

    List<Cliente> buscarPorNome(String nome);

    Cliente salvar(Cliente cliente);

    Cliente atualizar(Long id, Cliente clienteAtualizado);

    void deletar(Long id);

    long contarClientes();
}
