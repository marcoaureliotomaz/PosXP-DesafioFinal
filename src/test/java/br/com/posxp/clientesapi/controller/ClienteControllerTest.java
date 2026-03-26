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
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveListarTodosOsClientes() throws Exception {
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.clientes", hasSize(10)))
                .andExpect(jsonPath("$._embedded.clientes[0].nome", is("Ana Silva")))
                .andExpect(jsonPath("$._embedded.clientes[0]._links.self.href", is("http://localhost/clientes/1")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/clientes")))
                .andExpect(jsonPath("$._links.next.href", is("http://localhost/clientes?page=1&size=10")))
                .andExpect(jsonPath("$.page.size", is(10)))
                .andExpect(jsonPath("$.page.totalElements", is(100)))
                .andExpect(jsonPath("$.page.totalPages", is(10)))
                .andExpect(jsonPath("$.page.number", is(0)));
    }

    @Test
    void deveBuscarClientePorId() throws Exception {
        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("ana.silva@exemplo.com")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/clientes/1")));
    }

    @Test
    void deveRetornar404QuandoIdNaoExistir() throws Exception {
        mockMvc.perform(get("/clientes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Cliente com id 999 nao encontrado.")));
    }

    @Test
    void deveBuscarClientesPorNome() throws Exception {
        mockMvc.perform(get("/clientes/nome/Bruno"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.clientes", hasSize(1)))
                .andExpect(jsonPath("$._embedded.clientes[0].nome", is("Bruno Costa")))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/clientes/nome/Bruno")))
                .andExpect(jsonPath("$.page.totalElements", is(1)));
    }

    @Test
    void devePaginarClientesComParametros() throws Exception {
        mockMvc.perform(get("/clientes").param("page", "1").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.clientes", hasSize(5)))
                .andExpect(jsonPath("$._embedded.clientes[0].id", is(6)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost/clientes")))
                .andExpect(jsonPath("$._links.next.href", is("http://localhost/clientes?page=2&size=5")))
                .andExpect(jsonPath("$._links.prev.href", is("http://localhost/clientes?page=0&size=5")))
                .andExpect(jsonPath("$.page.size", is(5)))
                .andExpect(jsonPath("$.page.number", is(1)));
    }

    @Test
    void deveRetornar400QuandoSortForInvalido() throws Exception {
        mockMvc.perform(get("/clientes").param("sort", "string"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Parametro de ordenacao invalido. Utilize campos validos, por exemplo sort=id,asc.")));
    }

    @Test
    void deveContarClientes() throws Exception {
        mockMvc.perform(get("/clientes/contar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(100)));
    }

    @Test
    void deveCriarCliente() throws Exception {
        String payload = """
                {
                  "nome": "Diego Martins",
                  "email": "diego.martins@exemplo.com"
                }
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/clientes/101"))
                .andExpect(jsonPath("$.id", is(101)))
                .andExpect(jsonPath("$.nome", is("Diego Martins")));
    }

    @Test
    void deveAtualizarCliente() throws Exception {
        String payload = """
                {
                  "nome": "Ana Silva Atualizada",
                  "email": "ana.atualizada@exemplo.com"
                }
                """;

        mockMvc.perform(put("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nome", is("Ana Silva Atualizada")));
    }

    @Test
    void deveExcluirCliente() throws Exception {
        mockMvc.perform(delete("/clientes/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar409AoExcluirClienteComPedidosAssociados() throws Exception {
        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", is("Cliente nao pode ser removido porque possui pedidos associados.")));
    }

    @Test
    void deveValidarPayloadInvalido() throws Exception {
        String payload = """
                {
                  "nome": "",
                  "email": "email-invalido"
                }
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Dados de entrada invalidos.")))
                .andExpect(jsonPath("$.validations.nome", is("O nome e obrigatorio.")))
                .andExpect(jsonPath("$.validations.email", is("O email informado e invalido.")));
    }
}
