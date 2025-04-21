package com.db.votacaobackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfigSwagger {

  @Bean
  public OpenAPI configOpenApi() {
    return new OpenAPI().info(
        new Info().description("Definição de API para votação de sessões criadas a partir de pautas")
            .version("1.0.0")
            .title("Votação API")
    );
  }
}
