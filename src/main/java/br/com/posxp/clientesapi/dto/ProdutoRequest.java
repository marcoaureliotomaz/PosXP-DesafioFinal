package br.com.posxp.clientesapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank(message = "O nome e obrigatorio.")
        @Size(max = 120, message = "O nome deve ter no maximo 120 caracteres.")
        String nome,

        @NotBlank(message = "A descricao e obrigatoria.")
        @Size(max = 255, message = "A descricao deve ter no maximo 255 caracteres.")
        String descricao,

        @NotNull(message = "O preco e obrigatorio.")
        @DecimalMin(value = "0.01", message = "O preco deve ser maior que zero.")
        BigDecimal preco
) {
}

