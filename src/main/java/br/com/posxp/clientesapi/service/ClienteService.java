package br.com.posxp.clientesapi.service;

import br.com.posxp.clientesapi.model.Cliente;
import br.com.posxp.clientesapi.repository.ClienteRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        log.debug("Consultando todos os clientes ordenados por id.");
        return clienteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Cliente buscarPorId(Long id) {
        log.debug("Consultando cliente por id={} no repositorio.", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente com id " + id + " nao encontrado."));
    }

    public List<Cliente> buscarPorNome(String nome) {
        log.debug("Consultando clientes por nome contendo '{}'.", nome);
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Cliente salvar(Cliente cliente) {
        log.debug("Persistindo novo cliente com email={}.", cliente.getEmail());
        return clienteRepository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        log.debug("Atualizando cliente id={}.", id);
        Cliente clienteExistente = buscarPorId(id);
        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        return clienteRepository.save(clienteExistente);
    }

    public void deletar(Long id) {
        log.debug("Removendo cliente id={}.", id);
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }

    public long contarClientes() {
        log.debug("Contando clientes cadastrados.");
        return clienteRepository.count();
    }
}
