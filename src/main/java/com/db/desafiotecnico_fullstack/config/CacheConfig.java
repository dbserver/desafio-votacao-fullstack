package com.db.desafiotecnico_fullstack.config;

import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class CacheConfig {
    public static final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

    public static void limparCache(UUID pautaId) {
        cache.remove("resultado_" + pautaId);
    }

}
