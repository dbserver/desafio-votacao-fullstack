package com.service.polls.adapters.out;

import com.service.polls.adapters.out.dto.DocumentDTO;
import com.service.polls.ports.out.IExternalDocumentProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class ExternalDocumentProvider implements IExternalDocumentProvider {

    private final WebClient webClient;

    public ExternalDocumentProvider(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://cnpjcheck.com.br")
                .build();
    }

    @Override
    public boolean validateCPF(String cpf) {
        try {
            DocumentDTO response = webClient
                    .get()
                    .uri("/api/tools/validate/cpf/{cpf}", cpf)
                    .retrieve()
                    .bodyToMono(DocumentDTO.class)
                    .block();

            return response != null && response.isValid();
        } catch (WebClientResponseException.NotFound ex) {
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
