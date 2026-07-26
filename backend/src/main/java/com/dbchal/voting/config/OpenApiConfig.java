package com.dbchal.voting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI votingOpenApi() {
		return new OpenAPI().info(new Info().title("Voting API")
				.description("API REST para gerenciamento de pautas, " + "sessões de votação, votos e resultados.")
				.version("v1"));
	}
}