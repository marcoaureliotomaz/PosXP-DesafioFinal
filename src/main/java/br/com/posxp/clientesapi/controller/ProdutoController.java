package br.com.posxp.clientesapi.controller;

import br.com.posxp.clientesapi.dto.ContagemResponse;
import br.com.posxp.clientesapi.dto.ProdutoRequest;
import br.com.posxp.clientesapi.dto.ProdutoResponse;
import br.com.posxp.clientesapi.model.Produto;
import br.com.posxp.clientesapi.service.ProdutoService;
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
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Operacoes de gerenciamento de produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Retorna todos os produtos cadastrados na base.")
    public ResponseEntity<List<ProdutoResponse>> listarTodos() {
        log.info("Recebida requisicao para listar todos os produtos.");
        List<ProdutoResponse> produtos = produtoService.listarTodos().stream()
                .map(ProdutoMapper::toResponse)
                .toList();
        log.info("Listagem concluida com {} produto(s).", produtos.size());
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto especifico a partir do identificador informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto nao encontrado",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class)))
    })
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        log.info("Recebida requisicao para buscar produto por id: {}.", id);
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(ProdutoMapper.toResponse(produto));
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar produtos por nome", description = "Retorna os produtos cujo nome contenha o valor informado.")
    public ResponseEntity<List<ProdutoResponse>> buscarPorNome(@PathVariable String nome) {
        log.info("Recebida requisicao para buscar produtos por nome: {}.", nome);
        List<ProdutoResponse> produtos = produtoService.buscarPorNome(nome).stream()
                .map(ProdutoMapper::toResponse)
                .toList();
        log.info("Busca por nome '{}' retornou {} produto(s).", nome, produtos.size());
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/contar")
    @Operation(summary = "Contar produtos", description = "Retorna o total de produtos cadastrados.")
    public ResponseEntity<ContagemResponse> contarProdutos() {
        long total = produtoService.contarProdutos();
        log.info("Recebida requisicao de contagem. Total atual de produtos: {}.", total);
        return ResponseEntity.ok(new ContagemResponse(total));
    }

    @PostMapping
    @Operation(summary = "Criar produto", description = "Cria um novo produto com nome, descricao e preco.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload invalido",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class)))
    })
    public ResponseEntity<ProdutoResponse> salvar(@Valid @RequestBody ProdutoRequest request) {
        log.info("Recebida requisicao para criar produto com nome: {}.", request.nome());
        Produto produtoSalvo = produtoService.salvar(ProdutoMapper.toEntity(request));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produtoSalvo.getId())
                .toUri();
        log.info("Produto criado com sucesso. id={}, nome={}.", produtoSalvo.getId(), produtoSalvo.getNome());
        return ResponseEntity.created(location).body(ProdutoMapper.toResponse(produtoSalvo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Payload invalido",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto nao encontrado",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class)))
    })
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request
    ) {
        log.info("Recebida requisicao para atualizar produto id={} com nome={}.", id, request.nome());
        Produto produtoAtualizado = produtoService.atualizar(id, ProdutoMapper.toEntity(request));
        log.info("Produto atualizado com sucesso. id={}.", produtoAtualizado.getId());
        return ResponseEntity.ok(ProdutoMapper.toResponse(produtoAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir produto", description = "Remove um produto existente a partir do ID informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto nao encontrado",
                    content = @Content(schema = @Schema(implementation = br.com.posxp.clientesapi.dto.ErroResponse.class)))
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("Recebida requisicao para excluir produto id={}.", id);
        produtoService.deletar(id);
        log.info("Produto removido com sucesso. id={}.", id);
        return ResponseEntity.noContent().build();
    }
}

