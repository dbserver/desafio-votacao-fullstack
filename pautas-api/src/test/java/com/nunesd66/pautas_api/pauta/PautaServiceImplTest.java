package com.nunesd66.pautas_api.pauta;

import com.nunesd66.pautas_api.pauta.dto.PautaCreateRequest;
import com.nunesd66.pautas_api.pauta.dto.PautaResponse;
import com.nunesd66.pautas_api.pauta.exception.PautaException;
import com.nunesd66.pautas_api.pauta.mapper.PautaMapper;
import com.nunesd66.pautas_api.pauta.model.Pauta;
import com.nunesd66.pautas_api.pauta.repository.PautaRepository;
import com.nunesd66.pautas_api.pauta.service.PautaServiceImpl;
import com.nunesd66.pautas_api.pauta.validator.PautaValidator;
import com.nunesd66.pautas_api.sessaoVotacao.model.SessaoVotacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PautaServiceImplTest {

    @Mock
    private PautaRepository repository;

    @Mock
    private PautaValidator validator;

    @Mock
    private PautaMapper mapper;

    @InjectMocks
    private PautaServiceImpl service;

    private PautaCreateRequest request;
    private Pauta entity;
    private Pauta savedEntity;
    private PautaResponse response;

    @BeforeEach
    void setup() {
        request = new PautaCreateRequest("Titulo", "Descricao", null);
        entity = new Pauta();
        entity.setSessaoVotacao(new SessaoVotacao());
        entity.setTitulo("Titulo");
        entity.setDescricao("Descricao");

        savedEntity = new Pauta();
        savedEntity.setId(1L);
        savedEntity.setTitulo("Titulo");
        savedEntity.setDescricao("Descricao");

        response = new PautaResponse(
                1L,
                "Titulo",
                "Descricao",
                null
        );
    }

    @Test
    void save_success() throws PautaException {
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toResponse(savedEntity)).thenReturn(response);

        PautaResponse result = service.save(request);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Titulo", result.titulo());

        verify(mapper).toEntity(request);
        verify(validator).validCreatePauta(entity);
        verify(repository).save(entity);
        verify(mapper).toResponse(savedEntity);
    }

    @Test
    void save_validationFails_throwsException() throws PautaException {
        when(mapper.toEntity(request)).thenReturn(entity);
        doThrow(new PautaException("erro")).when(validator).validCreatePauta(entity);

        PautaException ex = assertThrows(PautaException.class,
                () -> service.save(request)
        );
        assertEquals("erro", ex.getMessage());

        verify(repository, never()).save(any());
        verify(mapper, never()).toResponse(any(Pauta.class));
    }

    @Test
    void getAll_returnsMappedList() {
        when(repository.findAllClosed()).thenReturn(List.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        var result = service.getAll();

        assertEquals(1, result.size());
        assertSame(response, result.getFirst());

        verify(repository).findAllClosed();
        verify(mapper).toResponse(entity);
    }

    @Test
    void getLastRecord_present() {
        when(repository.getLastRecord()).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        PautaResponse result = service.getLastRecord();

        assertEquals(response, result);
        verify(repository).getLastRecord();
        verify(mapper).toResponse(entity);
    }

    @Test
    void getLastRecord_empty_returnsNull() {
        when(repository.getLastRecord()).thenReturn(Optional.empty());

        PautaResponse result = service.getLastRecord();

        assertNull(result);
        verify(repository).getLastRecord();
        verify(mapper, never()).toResponse(any(Pauta.class));
    }

    @Test
    void fecharVotos_pautaNotFound_throwsException() {
        when(repository.findById(42L)).thenReturn(Optional.empty());

        PautaException ex = assertThrows(PautaException.class,
                () -> service.fecharVotos(42L)
        );
        assertEquals(
                "Não foi possível fechar os votos, código da pauta não encontrada.",
                ex.getMessage()
        );
        verify(repository).findById(42L);
        verify(repository, never()).save(any());
        verify(mapper, never()).toResponse(any(Pauta.class));
    }

    @Test
    void fecharVotos_success() throws PautaException {
        Pauta dbEntity = new Pauta();
        dbEntity.setId(42L);
        dbEntity.setSessaoVotacao(new SessaoVotacao());

        Pauta saved = new Pauta();
        saved.setId(42L);
        saved.setSessaoVotacao(new SessaoVotacao());

        when(repository.findById(42L)).thenReturn(Optional.of(dbEntity));
        when(repository.save(dbEntity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(response);

        PautaResponse result = service.fecharVotos(42L);

        assertEquals(response, result);
        verify(repository).findById(42L);
        verify(repository).save(dbEntity);
        verify(mapper).toResponse(saved);
    }

}
