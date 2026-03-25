package br.com.posxp.clientesapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI clientesApiOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clientes API")
                        .description("API REST MVC para gerenciamento de clientes, desenvolvida como solucao do desafio final da pos em Arquitetura de Software.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Projeto POS XP")
                                .email("contato@posxp.local"))
                        .license(new License()
                                .name("Uso academico")
                                .url("https://opensource.org/licenses/MIT")));
    }
}

