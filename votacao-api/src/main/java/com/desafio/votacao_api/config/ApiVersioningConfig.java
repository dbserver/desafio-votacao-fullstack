package com.desafio.votacao_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Centralizes API base-path/versioning.
 *
 * <p>All controllers in the controller package automatically receive the
 * configured prefix, keeping version management in one place.</p>
 */
@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {

    private static final String CONTROLLER_BASE_PACKAGE = "com.desafio.votacao_api.controller";

    @Value("${api.base-path:/api/v1}")
    private String apiBasePath;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(
                apiBasePath,
                HandlerTypePredicate.forBasePackage(CONTROLLER_BASE_PACKAGE)
        );
    }
}

