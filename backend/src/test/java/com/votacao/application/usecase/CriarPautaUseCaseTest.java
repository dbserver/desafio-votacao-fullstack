package com.votacao.application.usecase;

import com.votacao.application.dto.PautaRequestDTO;
import com.votacao.domain.service.PautaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CriarPautaUseCaseTest {

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private CriarPautaUseCase criarPautaUseCase;

    @Test
    void deveCriarPauta() {
        PautaRequestDTO dto = PautaRequestDTO.builder().titulo("Título teste").descricao("Descrição teste").build();

        criarPautaUseCase.execute(dto);

        verify(pautaService, times(1)).save(argThat(pauta -> pauta.getTitulo().equals("Título teste") && pauta.getDescricao().equals("Descrição teste")));
    }

}