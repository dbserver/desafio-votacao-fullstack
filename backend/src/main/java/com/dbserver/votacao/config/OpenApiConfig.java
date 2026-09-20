package com.dbserver.votacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Votação")
                        .version("v1")
                        .description("Documentação dos endpoints de votação"));
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            if (openApi.getComponents() != null && openApi.getComponents().getSchemas() != null) {
                openApi.getComponents().getSchemas().values().forEach(schema -> {
                    // Tipando explicitamente o Map para evitar o warning de Raw Type
                    @SuppressWarnings("unchecked")
                    Map<String, Schema<?>> properties = schema.getProperties();

                    if (properties != null) {
                        properties.forEach((propertyName, propertySchema) -> {
                            if ("LocalDateTime".equals(propertySchema.getType())) {
                                propertySchema.setType("string");
                                propertySchema.setFormat("date-time");
                                propertySchema.setExample("2026-09-19T20:30:00");
                            }
                        });
                    }
                });
            }
        };
    }
}