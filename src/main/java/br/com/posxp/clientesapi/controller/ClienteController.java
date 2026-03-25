package br.com.posxp.clientesapi.controller;

import br.com.posxp.clientesapi.dto.ClienteRequest;
import br.com.posxp.clientesapi.dto.ClienteResponse;
import br.com.posxp.clientesapi.dto.ContagemResponse;
import br.com.posxp.clientesapi.model.Cliente;
import br.com.posxp.clientesapi.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Clientes", description = "Operacoes de gerenciamento de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna todos os clientes cadastrados na base.")
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        log.info("Recebida requisicao para listar todos os clientes.");
        List<ClienteResponse> clientes = clienteService.listarTodos().stream()
                .map(ClienteMapper::toResponse)
                .toList();
        log.info("Listagem concluida com {} cliente(s).", clientes.size());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente especifico a partir do identificador informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente nao encontrado",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class)))
    })
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        log.info("Recebida requisicao para buscar cliente por id: {}.", id);
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toResponse(cliente));
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar clientes por nome", description = "Retorna os clientes cujo nome contenha o valor informado.")
    public ResponseEntity<List<ClienteResponse>> buscarPorNome(@PathVariable String nome) {
        log.info("Recebida requisicao para buscar clientes por nome: {}.", nome);
        List<ClienteResponse> clientes = clienteService.buscarPorNome(nome).stream()
                .map(ClienteMapper::toResponse)
                .toList();
        log.info("Busca por nome '{}' retornou {} cliente(s).", nome, clientes.size());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/contar")
    @Operation(summary = "Contar clientes", description = "Retorna o total de clientes cadastrados.")
    public ResponseEntity<ContagemResponse> contarClientes() {
        long total = clienteService.contarClientes();
        log.info("Recebida requisicao de contagem. Total atual de clientes: {}.", total);
        return ResponseEntity.ok(new ContagemResponse(total));
    }

    @PostMapping
    @Operation(summary = "Criar cliente", description = "Cria um novo cliente com nome e email.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload invalido",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email ja cadastrado",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class)))
    })
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
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload invalido",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente nao encontrado",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email ja cadastrado",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class)))
    })
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
    @Operation(summary = "Excluir cliente", description = "Remove um cliente existente a partir do ID informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente nao encontrado",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class)))
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Recebida requisicao para excluir cliente id={}.", id);
        clienteService.deletar(id);
        log.info("Cliente removido com sucesso. id={}.", id);
        return ResponseEntity.noContent().build();
    }
}
