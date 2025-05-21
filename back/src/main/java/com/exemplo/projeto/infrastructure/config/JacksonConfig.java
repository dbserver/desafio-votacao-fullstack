package com.exemplo.projeto.infrastructure.config;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .timeZone(TimeZone.getTimeZone("America/Sao_Paulo"))
                .simpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
                .modulesToInstall(new JavaTimeModule());
    }

}
