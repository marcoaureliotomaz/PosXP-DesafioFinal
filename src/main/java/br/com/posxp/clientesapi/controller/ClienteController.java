package br.com.posxp.clientesapi.controller;

import br.com.posxp.clientesapi.dto.ClienteRequest;
import br.com.posxp.clientesapi.dto.ClienteResponse;
import br.com.posxp.clientesapi.dto.ContagemResponse;
import br.com.posxp.clientesapi.model.Cliente;
import br.com.posxp.clientesapi.service.ClienteService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        log.info("Recebida requisicao para listar todos os clientes.");
        List<ClienteResponse> clientes = clienteService.listarTodos().stream()
                .map(ClienteMapper::toResponse)
                .toList();
        log.info("Listagem concluida com {} cliente(s).", clientes.size());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        log.info("Recebida requisicao para buscar cliente por id: {}.", id);
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<ClienteResponse>> buscarPorNome(@PathVariable String nome) {
        log.info("Recebida requisicao para buscar clientes por nome: {}.", nome);
        List<ClienteResponse> clientes = clienteService.buscarPorNome(nome).stream()
                .map(ClienteMapper::toResponse)
                .toList();
        log.info("Busca por nome '{}' retornou {} cliente(s).", nome, clientes.size());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/contar")
    public ResponseEntity<ContagemResponse> contarClientes() {
        long total = clienteService.contarClientes();
        log.info("Recebida requisicao de contagem. Total atual de clientes: {}.", total);
        return ResponseEntity.ok(new ContagemResponse(total));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> salvar(@Valid @RequestBody ClienteRequest request) {
        log.info("Recebida requisicao para criar cliente com email: {}.", request.email());
        Cliente clienteSalvo = clienteService.salvar(ClienteMapper.toEntity(request));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clienteSalvo.getId())
                .toUri();
        log.info("Cliente criado com sucesso. id={}, email={}.", clienteSalvo.getId(), clienteSalvo.getEmail());
        return ResponseEntity.created(location).body(ClienteMapper.toResponse(clienteSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request
    ) {
        log.info("Recebida requisicao para atualizar cliente id={} com email={} .", id, request.email());
        Cliente clienteAtualizado = clienteService.atualizar(id, ClienteMapper.toEntity(request));
        log.info("Cliente atualizado com sucesso. id={}.", clienteAtualizado.getId());
        return ResponseEntity.ok(ClienteMapper.toResponse(clienteAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Recebida requisicao para excluir cliente id={}.", id);
        clienteService.deletar(id);
        log.info("Cliente removido com sucesso. id={}.", id);
        return ResponseEntity.noContent().build();
    }
}
