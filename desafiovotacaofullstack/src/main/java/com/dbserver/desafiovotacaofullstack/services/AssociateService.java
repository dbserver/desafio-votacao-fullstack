package com.dbserver.desafiovotacaofullstack.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbserver.desafiovotacaofullstack.domains.Associate;
import com.dbserver.desafiovotacaofullstack.dtos.AssociateRequestDto;
import com.dbserver.desafiovotacaofullstack.dtos.AssociateResponseDto;
import com.dbserver.desafiovotacaofullstack.exceptions.ConflitDuplicateKeyException;
import com.dbserver.desafiovotacaofullstack.repositories.AssociateRepository;

@Service
public class AssociateService {
	
	@Autowired
	AssociateRepository associateRepository;
	
	public AssociateResponseDto createAssociate(AssociateRequestDto associateRequestDto) {
		if(associateRepository.existsByCpf(associateRequestDto.cpf()))
			throw new ConflitDuplicateKeyException("Já existe um associado com este cpf.");
		
		AssociateResponseDto associateResponseDto = associateRepository.save(new Associate(associateRequestDto.name(), associateRequestDto.cpf() )).entityToDto();
		return associateResponseDto;
	}
	
	public List<Associate> getAllAssociates() {
        return associateRepository.findAll();
    }
}
