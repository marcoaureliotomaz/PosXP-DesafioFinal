package br.com.posxp.clientesapi.controller;

import br.com.posxp.clientesapi.dto.ContagemResponse;
import br.com.posxp.clientesapi.dto.PedidoRequest;
import br.com.posxp.clientesapi.dto.PedidoResponse;
import br.com.posxp.clientesapi.model.Pedido;
import br.com.posxp.clientesapi.model.PedidoStatus;
import br.com.posxp.clientesapi.service.PedidoService;
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
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Operacoes de gerenciamento de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna todos os pedidos cadastrados na base.")
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        log.info("Recebida requisicao para listar todos os pedidos.");
        List<PedidoResponse> pedidos = pedidoService.listarTodos().stream()
                .map(PedidoMapper::toResponse)
                .toList();
        log.info("Listagem concluida com {} pedido(s).", pedidos.size());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido especifico a partir do identificador informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido nao encontrado",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class)))
    })
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        log.info("Recebida requisicao para buscar pedido por id: {}.", id);
        Pedido pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(PedidoMapper.toResponse(pedido));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar pedidos por status", description = "Retorna os pedidos com o status informado.")
    public ResponseEntity<List<PedidoResponse>> buscarPorStatus(@PathVariable PedidoStatus status) {
        log.info("Recebida requisicao para buscar pedidos por status: {}.", status);
        List<PedidoResponse> pedidos = pedidoService.buscarPorStatus(status).stream()
                .map(PedidoMapper::toResponse)
                .toList();
        log.info("Busca por status '{}' retornou {} pedido(s).", status, pedidos.size());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/contar")
    @Operation(summary = "Contar pedidos", description = "Retorna o total de pedidos cadastrados.")
    public ResponseEntity<ContagemResponse> contarPedidos() {
        long total = pedidoService.contarPedidos();
        log.info("Recebida requisicao de contagem. Total atual de pedidos: {}.", total);
        return ResponseEntity.ok(new ContagemResponse(total));
    }

    @PostMapping
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido com cliente, status e itens.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload invalido",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente ou produto nao encontrado",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class)))
    })
    public ResponseEntity<PedidoResponse> salvar(@Valid @RequestBody PedidoRequest request) {
        log.info("Recebida requisicao para criar pedido para cliente id={}.", request.clienteId());
        Pedido pedidoSalvo = pedidoService.salvar(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedidoSalvo.getId())
                .toUri();
        log.info("Pedido criado com sucesso. id={}, clienteId={}.", pedidoSalvo.getId(), pedidoSalvo.getCliente().getId());
        return ResponseEntity.created(location).body(PedidoMapper.toResponse(pedidoSalvo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido", description = "Atualiza os dados de um pedido existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload invalido",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pedido, cliente ou produto nao encontrado",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class)))
    })
    public ResponseEntity<PedidoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoRequest request
    ) {
        log.info("Recebida requisicao para atualizar pedido id={} do cliente id={}.", id, request.clienteId());
        Pedido pedidoAtualizado = pedidoService.atualizar(id, request);
        log.info("Pedido atualizado com sucesso. id={}.", pedidoAtualizado.getId());
        return ResponseEntity.ok(PedidoMapper.toResponse(pedidoAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pedido", description = "Remove um pedido existente a partir do ID informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pedido removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido nao encontrado",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class)))
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Recebida requisicao para excluir pedido id={}.", id);
        pedidoService.deletar(id);
        log.info("Pedido removido com sucesso. id={}.", id);
        return ResponseEntity.noContent().build();
    }
}
