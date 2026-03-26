package br.com.posxp.clientesapi.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import br.com.posxp.clientesapi.dto.ProdutoRequest;
import br.com.posxp.clientesapi.dto.ProdutoResponse;
import br.com.posxp.clientesapi.model.Produto;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProdutoMapperTest {

    @Test
    void deveConverterRequestParaEntity() {
        ProdutoRequest request = new ProdutoRequest("Mouse", "Mouse sem fio", new BigDecimal("120.00"));

        Produto produto = ProdutoMapper.toEntity(request);

        assertNull(produto.getId());
        assertEquals("Mouse", produto.getNome());
        assertEquals("Mouse sem fio", produto.getDescricao());
        assertEquals(new BigDecimal("120.00"), produto.getPreco());
    }

    @Test
    void deveConverterEntityParaResponse() {
        Produto produto = new Produto(1L, "Mouse", "Mouse sem fio", new BigDecimal("120.00"));

        ProdutoResponse response = ProdutoMapper.toResponse(produto);

        assertEquals(1L, response.id());
        assertEquals("Mouse", response.nome());
        assertEquals("Mouse sem fio", response.descricao());
        assertEquals(new BigDecimal("120.00"), response.preco());
    }
}
