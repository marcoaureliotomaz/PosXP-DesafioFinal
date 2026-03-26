package br.com.posxp.clientesapi.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import br.com.posxp.clientesapi.dto.ClienteRequest;
import br.com.posxp.clientesapi.dto.ClienteResponse;
import br.com.posxp.clientesapi.model.Cliente;
import org.junit.jupiter.api.Test;

class ClienteMapperTest {

    @Test
    void deveConverterRequestParaEntity() {
        ClienteRequest request = new ClienteRequest("Ana Silva", "ana.silva@exemplo.com");

        Cliente cliente = ClienteMapper.toEntity(request);

        assertNull(cliente.getId());
        assertEquals("Ana Silva", cliente.getNome());
        assertEquals("ana.silva@exemplo.com", cliente.getEmail());
    }

    @Test
    void deveConverterEntityParaResponse() {
        Cliente cliente = new Cliente(1L, "Ana Silva", "ana.silva@exemplo.com");

        ClienteResponse response = ClienteMapper.toResponse(cliente);

        assertEquals(1L, response.getId());
        assertEquals("Ana Silva", response.getNome());
        assertEquals("ana.silva@exemplo.com", response.getEmail());
        assertEquals("/clientes/1", response.getRequiredLink("self").getHref());
    }
}
