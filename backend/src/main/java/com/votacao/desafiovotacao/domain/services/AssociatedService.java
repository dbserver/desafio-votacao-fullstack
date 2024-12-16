package com.votacao.desafiovotacao.domain.services;

import com.votacao.desafiovotacao.application.dtos.AssociatedDTO;
import com.votacao.desafiovotacao.domain.entities.Agenda;
import com.votacao.desafiovotacao.domain.entities.Associated;
import com.votacao.desafiovotacao.domain.exceptions.CPFAlreadyExistsException;
import com.votacao.desafiovotacao.domain.exceptions.CPFInvalidException;
import com.votacao.desafiovotacao.domain.exceptions.NameNeededException;
import com.votacao.desafiovotacao.infra.AssociatedRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AssociatedService {

    @Autowired
    private AssociatedRepository associatedRepository;

    @Autowired
    private CPFValidationService cpfValidationService;

    public Associated createAssociated(AssociatedDTO associatedDTO)
            throws NameNeededException, CPFInvalidException, CPFAlreadyExistsException {

        if (associatedDTO.name().isEmpty()) {
            throw new NameNeededException();
        }
        if (!cpfValidationService.isValid(associatedDTO.cpf())) {
            throw new CPFInvalidException();
        }

        List<Associated> associatedList = associatedRepository.findAll();
        boolean cpfExists = associatedList.stream()
                .anyMatch(entity ->
                        entity.getCpf().equals(associatedDTO.cpf())
                );

        if (cpfExists) {
            throw new CPFAlreadyExistsException();
        }

        Associated associated = Associated.builder().build();
        BeanUtils.copyProperties(associatedDTO, associated);

        return associatedRepository.save(associated);
    }

    public Associated get(String id) {
        return associatedRepository.findById(id).orElse(null);
    }

    public List<Associated> findAll() {
        return associatedRepository.findAll();
    }

    public Associated update(String id, AssociatedDTO associatedDTO) {
        Associated associated = associatedRepository.findById(id).orElse(null);
        if (associated == null) return null;
        associated.setCpf(associatedDTO.cpf());
        associated.setName(associatedDTO.name());
        return associatedRepository.save(associated);
    }

    public void delete(String id) {
        associatedRepository.deleteById(id);
    }

    public void deleteAll() {
        associatedRepository.deleteAll();
    }
}


