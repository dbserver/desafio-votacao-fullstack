package com.desafioVotacaoBackend.desafioVotacaoBackend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Pauta;
import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Voto;
import com.desafioVotacaoBackend.desafioVotacaoBackend.repository.AssociadoRepository;
import com.desafioVotacaoBackend.desafioVotacaoBackend.repository.VotoRepository;

@Service
public class VotoService {
        @Autowired
        private VotoRepository repository;

        @Autowired
        private PautaService pautaService;

        @Autowired
        private AssociadoRepository associadoRepository;

        public Voto registrarVoto(Long pautaId, String cpf, Voto.VotoOpcao opcao) {
                if (!pautaService.verificarSessaoAtiva(pautaId)) {
                        throw new RuntimeException("Sessão de votação não está ativa para esta pauta");
                }

                Pauta pauta = pautaService.buscarPautaPorId(pautaId)
                                .orElseThrow(() -> new RuntimeException("Pauta não encontrada"));

                boolean associadoExiste = associadoRepository.existsByCpf(cpf);
                if (!associadoExiste) {
                        throw new RuntimeException("CPF não cadastrado como associado");
                }

                boolean votoExistente = repository.existsByPautaIdAndCpf(pautaId, cpf);
                if (votoExistente) {
                        throw new RuntimeException("CPF já votou nesta pauta");
                }

                Voto voto = new Voto();
                voto.setCpf(cpf);
                voto.setDataVoto(LocalDateTime.now());
                voto.setOpcao(opcao);
                voto.setPauta(pauta);

                return repository.save(voto);
        }

        public Map<String, Long> contabilizarResultado(Long pautaId) {
                List<Voto> votos = repository.findAll().stream()
                                .filter(v -> v.getPauta().getId().equals(pautaId))
                                .collect(Collectors.toList());

                return votos.stream()
                                .collect(Collectors.groupingBy(
                                                v -> v.getOpcao().name(),
                                                Collectors.counting()));
        }

}
