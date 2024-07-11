package com.dbserver.desafiovotacaofullstack.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbserver.desafiovotacaofullstack.domains.Associate;
import com.dbserver.desafiovotacaofullstack.repositories.AssociateRepository;

@Service
public class AssociateService {
	
	@Autowired
	AssociateRepository associateRepository;
	
	public Associate createAssociate(Associate associate) {
		associate = associateRepository.save(associate);
		return associate;
	}
	
	public List<Associate> getAllAssociates() {
        return associateRepository.findAll();
    }
}
