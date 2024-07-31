package com.dbserver.desafiovotacaofullstack.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.dbserver.desafiovotacaofullstack.domains.Associate;
import com.dbserver.desafiovotacaofullstack.dtos.AssociateRequestDto;

import jakarta.persistence.EntityManager;

@DataJpaTest
@ActiveProfiles("test")
public class AssociateRepositoryTest {
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	AssociateRepository associateRepository;
	
	@Test
	@DisplayName("Should if exists associate successfully from DB.")
	public void existsByCpfSuccess() {
		Associate associate = createAssociate(new AssociateRequestDto("Thales Bento Mário Rezende", "704.718.699-96"));
		boolean isExistsAssociate = associateRepository.existsByCpf(associate.getCpf());
		assertThat(isExistsAssociate).isTrue();
	}
	
	@Test
	@DisplayName("Should false if not exists associate from DB.")
	public void existsByCpfErrors() {
		boolean isExistsAssociate = associateRepository.existsByCpf("334.440.257-93");
		assertThat(isExistsAssociate).isFalse();
	}
	
	private Associate createAssociate(AssociateRequestDto associateRequestDto) {
		Associate associate = new Associate(associateRequestDto.name(), associateRequestDto.cpf());
		this.entityManager.persist(associate);
		return  associate;
	}
}
