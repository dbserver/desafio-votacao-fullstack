package com.exemplo.projeto.application.port.input;

import com.exemplo.projeto.domain.model.Sessao;

public interface SessaoUseCase {
    Sessao abrirSessao(Long idPauta, Long duracaoEmMinutos);
}
