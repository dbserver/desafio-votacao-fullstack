package br.com.sicredi.desafiovotacao.application.port.out;

import br.com.sicredi.desafiovotacao.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VotacaoRepository {
	Pauta salvarPauta(Pauta pauta);

	Optional<Pauta> buscarPauta(UUID pautaId);

	List<Pauta> listarPautas(int pagina, int tamanho);

	SessaoVotacao abrirSessao(UUID pautaId, int duracaoMinutos);

	void registrarVoto(Voto voto);

	Optional<Resultado> buscarResultado(UUID pautaId);
}