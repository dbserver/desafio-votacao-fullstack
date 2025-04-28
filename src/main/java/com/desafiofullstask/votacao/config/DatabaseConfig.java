package com.desafiofullstask.votacao.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class DatabaseConfig {


    @Bean
    public HikariDataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5495/desafio_votacao");
        dataSource.setUsername("votacao");
        dataSource.setPassword("z896ipGG012567");
        return dataSource;

    }
}
