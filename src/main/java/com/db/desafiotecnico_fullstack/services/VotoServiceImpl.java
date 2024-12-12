package com.db.desafiotecnico_fullstack.services;

import com.db.desafiotecnico_fullstack.exceptions.*;
import com.db.desafiotecnico_fullstack.models.*;
import com.db.desafiotecnico_fullstack.models.dto.VotoDTO;
import com.db.desafiotecnico_fullstack.models.enuns.EscolhaVoto;
import com.db.desafiotecnico_fullstack.repositories.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class VotoServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(VotoServiceImpl.class);

    private final VotoRepository votoRepository;
    private final PautaRepository pautaRepository;
    private final AssociadoRepository associadoRepository;
    private final CpfValidationService cpfValidationService;

    public VotoServiceImpl(VotoRepository votoRepository,
                           PautaRepository pautaRepository,
                           AssociadoRepository associadoRepository,
                           CpfValidationService cpfValidationService) {
        this.votoRepository = votoRepository;
        this.pautaRepository = pautaRepository;
        this.associadoRepository = associadoRepository;
        this.cpfValidationService = cpfValidationService;
    }

    public Voto votar(VotoDTO votoDTO) {
        Pauta pauta = pautaRepository.findById(votoDTO.getPautaId())
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada"));

        if (!pauta.isAberta()) {
            throw new SessaoFechadaException("Sessão de votação está fechada");
        }

        Associado associado = associadoRepository.findById(votoDTO.getAssociadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Associado não encontrado"));

        if (!cpfValidationService.validarCpf(associado.getCpf())) {
            throw new CpfValidationException("CPF não está habilitado para votar");
        }

        if (votoRepository.existsByPauta_IdAndAssociadoId(pauta.getId(), associado.getId())) {
            throw new VotoDuplicadoException("Associado já votou nesta pauta");
        }

        Voto voto = new Voto();
        voto.setPauta(pauta);
        voto.setAssociadoId(associado.getId());
        voto.setVoto(EscolhaVoto.valueOf(votoDTO.getVoto().toUpperCase()));

        logger.info("Registrando voto para associado {} na pauta {}",
                associado.getId(),
                pauta.getId());

        return votoRepository.save(voto);
    }
}