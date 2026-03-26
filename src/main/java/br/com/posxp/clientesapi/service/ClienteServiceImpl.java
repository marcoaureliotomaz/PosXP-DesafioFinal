package br.com.posxp.clientesapi.service;

import br.com.posxp.clientesapi.exception.OperacaoNaoPermitidaException;
import br.com.posxp.clientesapi.exception.RecursoNaoEncontradoException;
import br.com.posxp.clientesapi.model.Cliente;
import br.com.posxp.clientesapi.repository.ClienteRepository;
import br.com.posxp.clientesapi.repository.PedidoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Page<Cliente> listarTodos(Pageable pageable) {
        log.debug("Consultando clientes paginados. page={}, size={}, sort={}.",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return clienteRepository.findAll(pageable);
    }

    @Override
    public Cliente buscarPorId(Long id) {
        log.debug("Consultando cliente por id={} no repositorio.", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente com id " + id + " nao encontrado."));
    }

    @Override
    public Page<Cliente> buscarPorNome(String nome, Pageable pageable) {
        log.debug("Consultando clientes por nome contendo '{}'. page={}, size={}.",
                nome, pageable.getPageNumber(), pageable.getPageSize());
        return clienteRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        log.debug("Persistindo novo cliente com email={}.", cliente.getEmail());
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        log.debug("Atualizando cliente id={}.", id);
        Cliente clienteExistente = buscarPorId(id);
        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        return clienteRepository.save(clienteExistente);
    }

    @Override
    public void deletar(Long id) {
        log.debug("Removendo cliente id={}.", id);
        Cliente cliente = buscarPorId(id);
        if (pedidoRepository.existsByClienteId(id)) {
            throw new OperacaoNaoPermitidaException(
                    "Cliente nao pode ser removido porque possui pedidos associados."
            );
        }
        clienteRepository.delete(cliente);
    }

    @Override
    public long contarClientes() {
        log.debug("Contando clientes cadastrados.");
        return clienteRepository.count();
    }
}
