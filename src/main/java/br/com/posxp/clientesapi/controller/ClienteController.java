package br.com.posxp.clientesapi.controller;

import br.com.posxp.clientesapi.dto.ClienteRequest;
import br.com.posxp.clientesapi.dto.ClienteResponse;
import br.com.posxp.clientesapi.dto.ContagemResponse;
import br.com.posxp.clientesapi.model.Cliente;
import br.com.posxp.clientesapi.service.ClienteService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        List<ClienteResponse> clientes = clienteService.listarTodos().stream()
                .map(ClienteMapper::toResponse)
                .toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<ClienteResponse>> buscarPorNome(@PathVariable String nome) {
        List<ClienteResponse> clientes = clienteService.buscarPorNome(nome).stream()
                .map(ClienteMapper::toResponse)
                .toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/contar")
    public ResponseEntity<ContagemResponse> contarClientes() {
        return ResponseEntity.ok(new ContagemResponse(clienteService.contarClientes()));
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> salvar(@Valid @RequestBody ClienteRequest request) {
        Cliente clienteSalvo = clienteService.salvar(ClienteMapper.toEntity(request));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(clienteSalvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(ClienteMapper.toResponse(clienteSalvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request
    ) {
        Cliente clienteAtualizado = clienteService.atualizar(id, ClienteMapper.toEntity(request));
        return ResponseEntity.ok(ClienteMapper.toResponse(clienteAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
