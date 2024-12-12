package com.db.desafiotecnico_fullstack.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CpfValidationService {

    public boolean validarCpf(String cpf) {
        // por enquantos empre retornar true para teste
        // em producao seria implementado a validação real do CPF
        return true;
    }

    // Implementação futura com API externa
    /*
    public boolean validarCpfComApi(String cpf) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://user-info.herokuapp.com/users/" + cpf;

        try {
            UserInfoResponse response = restTemplate.getForObject(url, UserInfoResponse.class);
            return "ABLE_TO_VOTE".equals(response.getStatus());
        } catch (Exception e) {
            return false;
        }
    }
    */
}