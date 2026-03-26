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
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveListarTodosOsPedidos() throws Exception {
        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pedidos", hasSize(10)))
                .andExpect(jsonPath("$._embedded.pedidos[0].status", is("CRIADO")))
                .andExpect(jsonPath("$._embedded.pedidos[0].itens", hasSize(5)))
                .andExpect(jsonPath("$._embedded.pedidos[0]._links.self.href", is("http://localhost/pedidos/1")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/pedidos")))
                .andExpect(jsonPath("$.page.totalElements", is(10)))
                .andExpect(jsonPath("$.page.totalPages", is(1)))
                .andExpect(jsonPath("$.page.number", is(0)));
    }

    @Test
    void deveBuscarPedidoPorId() throws Exception {
        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.clienteId", is(1)))
                .andExpect(jsonPath("$.total", is(6927.75)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/pedidos/1")))
                .andExpect(jsonPath("$._links.cliente.href", is("http://localhost/clientes/1")));
    }

    @Test
    void deveRetornar404QuandoPedidoNaoExistir() throws Exception {
        mockMvc.perform(get("/pedidos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Pedido com id 999 nao encontrado.")));
    }

    @Test
    void deveBuscarPedidosPorStatus() throws Exception {
        mockMvc.perform(get("/pedidos/status/CRIADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pedidos", hasSize(3)))
                .andExpect(jsonPath("$._embedded.pedidos[0].status", is("CRIADO")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/pedidos/status/CRIADO")))
                .andExpect(jsonPath("$.page.totalElements", is(3)));
    }

    @Test
    void devePaginarPedidosComParametros() throws Exception {
        mockMvc.perform(get("/pedidos").param("page", "1").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.pedidos", hasSize(5)))
                .andExpect(jsonPath("$._embedded.pedidos[0].id", is(6)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/pedidos")))
                .andExpect(jsonPath("$._links.prev.href", is("http://localhost/pedidos?page=0&size=5")))
                .andExpect(jsonPath("$.page.size", is(5)))
                .andExpect(jsonPath("$.page.number", is(1)));
    }

    @Test
    void deveContarPedidos() throws Exception {
        mockMvc.perform(get("/pedidos/contar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(10)));
    }

    @Test
    void deveCriarPedido() throws Exception {
        String payload = """
                {
                  "clienteId": 2,
                  "itens": [
                    { "produtoId": 2, "quantidade": 2 },
                    { "produtoId": 3, "quantidade": 1 }
                  ]
                }
                """;

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/pedidos/11"))
                .andExpect(jsonPath("$.id", is(11)))
                .andExpect(jsonPath("$.clienteId", is(2)))
                .andExpect(jsonPath("$.status", is("CRIADO")))
                .andExpect(jsonPath("$.total", is(2139.90)))
                .andExpect(jsonPath("$.itens", hasSize(2)));
    }

    @Test
    void deveAtualizarPedido() throws Exception {
        String payload = """
                {
                  "clienteId": 1,
                  "status": "PAGO",
                  "itens": [
                    { "produtoId": 2, "quantidade": 3 }
                  ]
                }
                """;

        mockMvc.perform(put("/pedidos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("PAGO")))
                .andExpect(jsonPath("$.total", is(360.00)))
                .andExpect(jsonPath("$.itens", hasSize(1)));
    }

    @Test
    void deveExcluirPedido() throws Exception {
        mockMvc.perform(delete("/pedidos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveValidarPayloadPedidoInvalido() throws Exception {
        String payload = """
                {
                  "itens": []
                }
                """;

        mockMvc.perform(post("/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Dados de entrada invalidos.")))
                .andExpect(jsonPath("$.validations.clienteId", is("O clienteId e obrigatorio.")))
                .andExpect(jsonPath("$.validations.itens", is("O pedido deve possuir ao menos um item.")));
    }
}
