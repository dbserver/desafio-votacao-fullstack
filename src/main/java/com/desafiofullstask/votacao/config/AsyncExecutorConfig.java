package com.desafiofullstask.votacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuração do executor assíncrono.
 * Define um executor de tarefas com um pool de threads para execução assíncrona.
 * Usado para ter maior controle sobre as threads que estão sendo criadas.
 */

@Configuration
@EnableAsync
public class AsyncExecutorConfig {

    @Bean(name = "voteExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("RegistroAsync-");
        executor.initialize();
        return executor;
    }
}
