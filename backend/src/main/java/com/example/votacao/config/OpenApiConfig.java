package com.example.votacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

  @Bean
  OpenAPI votacaoOpenAPI() {
    return new OpenAPI().info(new Info()
        .title("API de Votação")
        .description("Gerenciamento de pautas, sessões de votação e votos")
        .version("v1"));
  }
}
