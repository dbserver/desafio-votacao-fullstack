package com.votacao.backend.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Votação Cooperativa API")
                        .description("API REST para pautas, sessões, votos e resultado em tempo real.")
                        .version("v1"));
    }
}
