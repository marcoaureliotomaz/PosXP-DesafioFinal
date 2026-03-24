package br.com.posxp.clientesapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank(message = "O nome e obrigatorio.")
        @Size(max = 120, message = "O nome deve ter no maximo 120 caracteres.")
        String nome,

        @NotBlank(message = "O email e obrigatorio.")
        @Email(message = "O email informado e invalido.")
        @Size(max = 120, message = "O email deve ter no maximo 120 caracteres.")
        String email
) {
}

