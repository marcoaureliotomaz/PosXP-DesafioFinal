package br.com.posxp.clientesapi.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveListarTodosOsProdutos() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(100)))
                .andExpect(jsonPath("$[0].nome", is("Notebook")));
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {
        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.descricao", is("Notebook de alta performance")));
    }

    @Test
    void deveRetornar404QuandoProdutoIdNaoExistir() throws Exception {
        mockMvc.perform(get("/produtos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Produto com id 999 nao encontrado.")));
    }

    @Test
    void deveBuscarProdutosPorNome() throws Exception {
        mockMvc.perform(get("/produtos/nome/Mouse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("Mouse")));
    }

    @Test
    void deveContarProdutos() throws Exception {
        mockMvc.perform(get("/produtos/contar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(100)));
    }

    @Test
    void deveCriarProduto() throws Exception {
        String payload = """
                {
                  "nome": "Teclado",
                  "descricao": "Teclado mecanico",
                  "preco": 350.00
                }
                """;

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/produtos/101"))
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(jsonPath("$.nome", is("Teclado")));
    }

    @Test
    void deveAtualizarProduto() throws Exception {
        String payload = """
                {
                  "nome": "Notebook Gamer",
                  "descricao": "Notebook gamer atualizado",
                  "preco": 5999.90
                }
                """;

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Notebook Gamer")));
    }

    @Test
    void deveExcluirProduto() throws Exception {
        mockMvc.perform(delete("/produtos/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar409AoExcluirProdutoComItensDePedidoAssociados() throws Exception {
        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", is("Produto nao pode ser removido porque possui itens de pedido associados.")));
    }

    @Test
    void deveValidarPayloadProdutoInvalido() throws Exception {
        String payload = """
                {
                  "nome": "",
                  "descricao": "",
                  "preco": 0
                }
                """;

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Dados de entrada invalidos.")))
                .andExpect(jsonPath("$.validations.nome", is("O nome e obrigatorio.")))
                .andExpect(jsonPath("$.validations.descricao", is("A descricao e obrigatoria.")))
                .andExpect(jsonPath("$.validations.preco", is("O preco deve ser maior que zero.")));
    }
}
