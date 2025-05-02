package com.desafioVotacaoBackend.desafioVotacaoBackend.record;

import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Voto.VotoOpcao;

public record CriarVotoRecord(Long pautaId, String cpf, VotoOpcao opcao) {
}
