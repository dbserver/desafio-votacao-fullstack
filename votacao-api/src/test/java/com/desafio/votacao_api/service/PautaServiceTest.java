package com.desafio.votacao_api.service;

import com.desafio.votacao_api.dto.PautaRequestDTO;
import com.desafio.votacao_api.dto.PautaResponseDTO;
import com.desafio.votacao_api.exception.PautaNotFoundException;
import com.desafio.votacao_api.model.Pauta;
import com.desafio.votacao_api.repository.PautaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaService pautaService;

    @Test
    void createPautaShouldSaveAndReturnResponseDto() throws Exception {
        PautaRequestDTO request = new PautaRequestDTO("Reforma administrativa", "Discussao sobre impacto no orcamento");
        Pauta saved = new Pauta(request.name(), request.description());
        UUID pautaId = UUID.randomUUID();
        setPrivateField(saved, "id", pautaId);

        when(pautaRepository.save(org.mockito.ArgumentMatchers.any(Pauta.class))).thenReturn(saved);

        PautaResponseDTO response = pautaService.createPauta(request);

        assertEquals(pautaId, response.id());
        assertEquals(request.name(), response.name());
        assertEquals(request.description(), response.description());
        assertTrue(response.isActive());
        verify(pautaRepository, times(1)).save(org.mockito.ArgumentMatchers.any(Pauta.class));
    }

    @Test
    void getPautaByIdShouldReturnResponseWhenFound() throws Exception {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta("Aumento salarial", "Votacao sobre reajuste anual");
        setPrivateField(pauta, "id", pautaId);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        PautaResponseDTO response = pautaService.getPautaById(pautaId);

        assertEquals(pautaId, response.id());
        assertEquals("Aumento salarial", response.name());
        verify(pautaRepository, times(1)).findById(pautaId);
    }

    @Test
    void getPautaByIdShouldThrowWhenNotFound() {
        UUID pautaId = UUID.randomUUID();
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        assertThrows(PautaNotFoundException.class, () -> pautaService.getPautaById(pautaId));
    }

    @Test
    void getPautaByIdInternalShouldReturnEntityWhenFound() throws Exception {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta("Plano de investimentos", "Investimentos para o proximo trimestre");
        setPrivateField(pauta, "id", pautaId);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        Pauta result = pautaService.getPautaByIdInternal(pautaId);

        assertEquals(pautaId, result.getId());
    }

    @Test
    void getPautaByIdInternalShouldThrowWhenNotFound() {
        UUID pautaId = UUID.randomUUID();
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        assertThrows(PautaNotFoundException.class, () -> pautaService.getPautaByIdInternal(pautaId));
    }

    @Test
    void findAllActiveIsTrueShouldMapEntitiesToDtos() throws Exception {
        Pauta pauta1 = new Pauta("Tema 1", "Descricao 1");
        Pauta pauta2 = new Pauta("Tema 2", "Descricao 2");
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        setPrivateField(pauta1, "id", id1);
        setPrivateField(pauta2, "id", id2);

        when(pautaRepository.findAllByIsActiveTrue()).thenReturn(List.of(pauta1, pauta2));

        List<PautaResponseDTO> responses = pautaService.findAllActiveIsTrue();

        assertEquals(2, responses.size());
        assertEquals(id1, responses.get(0).id());
        assertEquals(id2, responses.get(1).id());
        verify(pautaRepository, times(1)).findAllByIsActiveTrue();
    }

    @Test
    void deletePautaByIdShouldDeactivateAndSaveWhenFound() throws Exception {
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta("Revisao fiscal", "Avaliacao de beneficios tributarios");
        setPrivateField(pauta, "id", pautaId);

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));

        pautaService.deletePautaById(pautaId);

        ArgumentCaptor<Pauta> pautaCaptor = ArgumentCaptor.forClass(Pauta.class);
        verify(pautaRepository, times(1)).save(pautaCaptor.capture());

        Pauta savedPauta = pautaCaptor.getValue();
        assertFalse(savedPauta.isActive());
        assertEquals(pautaId, savedPauta.getId());
    }

    @Test
    void deletePautaByIdShouldThrowWhenNotFound() {
        UUID pautaId = UUID.randomUUID();
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        assertThrows(PautaNotFoundException.class, () -> pautaService.deletePautaById(pautaId));
        verify(pautaRepository, never()).save(org.mockito.ArgumentMatchers.any(Pauta.class));
    }

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}

