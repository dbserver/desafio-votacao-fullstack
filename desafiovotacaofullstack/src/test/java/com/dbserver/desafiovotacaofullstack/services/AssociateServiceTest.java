package com.dbserver.desafiovotacaofullstack.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.dbserver.desafiovotacaofullstack.domains.Associate;
import com.dbserver.desafiovotacaofullstack.dtos.AssociateRequestDto;
import com.dbserver.desafiovotacaofullstack.dtos.AssociateResponseDto;
import com.dbserver.desafiovotacaofullstack.repositories.AssociateRepository;

@ActiveProfiles("test")
public class AssociateServiceTest {
	
	@Mock
	AssociateRepository associateRepository;
	
	@Autowired
	@InjectMocks
	AssociateService associateService;
		
	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	@DisplayName("Should create associate successfully when everything is OK")
	public void createAssociate() {
		when(associateRepository.existsByCpf(any())).thenReturn(false);
		Associate associate = new Associate(1, "Thales Bento Mário Rezende", "704.718.699-96");
		when(associateRepository.save(any(Associate.class))).thenReturn(associate);
		AssociateRequestDto associateRequestDto = new AssociateRequestDto(associate.getName(), associate.getCpf());

		AssociateResponseDto associateResponseDto = associateService.createAssociate(associateRequestDto);
		assertNotNull(associateResponseDto);
		assertEquals(associateResponseDto.id(), 1);
		assertEquals(associateResponseDto.name(), "Thales Bento Mário Rezende");
		assertEquals(associateResponseDto.cpf(), "704.718.699-96");
		verify(associateRepository, times(1)).save(any());
	}
	
	@Test
	@DisplayName("Should not create associate when everything is not OK because exits cpf")
	public void createAssociateErrorByExistsCpf() {
		when(associateRepository.existsByCpf(any())).thenReturn(true);
		assertThat(associateRepository.existsByCpf(any())).isTrue();
	}
	
	@Test
	@DisplayName("Bring all associates when contains data")
	public void getAllAssociatesWhenContainsData() {
		
		Associate associate1 = new Associate(1, "Thales Bento Mário Rezende", "704.718.699-96");
		Associate associate2 = new Associate(2,"Vicente Ricardo Duarte", "730.129.760-27" );
		Associate associate3 = new Associate(3, "Regina Laura Teresinha Jesus", "017.790.408-90");
		
		List<Associate> associates = Arrays.asList(associate1, associate2, associate3);
        when(associateRepository.findAll()).thenReturn(associates);
        List<Associate> resultAssociates = associateService.getAllAssociates();
        
        assertNotNull(resultAssociates);
        assertEquals(3, resultAssociates.size());
        assertEquals("Thales Bento Mário Rezende", resultAssociates.get(0).getName());
        assertEquals("Vicente Ricardo Duarte", resultAssociates.get(1).getName());
        assertEquals("Regina Laura Teresinha Jesus", resultAssociates.get(2).getName());
        verify(associateRepository, times(1)).findAll();
    }
	
	@Test
	@DisplayName("Bring all associates when not contains data")
	public void getAllAssociateWhenListIsEmpty() {

        when(associateRepository.findAll()).thenReturn(Collections.emptyList());
        List<Associate> resultAssociates = associateService.getAllAssociates();
        
        assertNotNull(resultAssociates);
        assertEquals(0, resultAssociates.size());
        assertTrue(resultAssociates.isEmpty(), "A lista deve estar vazia");
        verify(associateRepository, times(1)).findAll();
    }
}
