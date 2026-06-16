package com.votacao.backend.support;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@TestConfiguration
public class IntegrationTestConfig {

    @Bean
    public MutableCpfValidationClient cpfValidationClient() {
        return new MutableCpfValidationClient();
    }

    @Bean
    @Primary
    public SimpMessagingTemplate simpMessagingTemplate() {
        return new SimpMessagingTemplate(new MessageChannel() {
            @Override
            public boolean send(Message<?> message, long timeout) {
                return true;
            }

            @Override
            public boolean send(Message<?> message) {
                return true;
            }
        });
    }
}
