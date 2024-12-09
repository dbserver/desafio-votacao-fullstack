package com.sarapio.votacao_api.service;

import com.github.javafaker.Faker;
import com.sarapio.votacao_api.domain.associate.Associate;
import com.sarapio.votacao_api.domain.associate.AssociateRequestDTO;
import com.sarapio.votacao_api.repositories.AssociateRepository;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AssociateService {

    private final AssociateRepository associateRepository;

    public AssociateService(AssociateRepository associateRepository) {
        this.associateRepository = associateRepository;
    }

    public Associate createAssociate(AssociateRequestDTO data) {
        String cpf = data.cpf();


        String name = data.name();

        if (cpf == null || cpf.isEmpty() || name == null || name.isEmpty()) {
            throw new IllegalArgumentException("CPF e Nome são obrigatórios!");
        }

        Optional<Associate> existente = associateRepository.findByCpf(cpf);
        if (existente.isPresent()) {
            throw new IllegalStateException("Associado com este CPF já está cadastrado!");
        }

        Associate associate = new Associate();
        associate.setCpf(cpf);
        associate.setName(name);

        return associateRepository.save(associate);
    }


}
