package com.sarapio.votacao_api.service;

import com.github.javafaker.Faker;
import com.sarapio.votacao_api.domain.associate.Associate;
import com.sarapio.votacao_api.domain.session.Session;
import com.sarapio.votacao_api.domain.vote.Vote;
import com.sarapio.votacao_api.domain.vote.VoteRequestDTO;
import com.sarapio.votacao_api.repositories.AssociateRepository;
import com.sarapio.votacao_api.repositories.SessionRepository;
import com.sarapio.votacao_api.repositories.VoteRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final AssociateRepository associateRepository;
    private final SessionRepository sessionRepository;

    public VoteService(VoteRepository voteRepository, AssociateRepository associateRepository, SessionRepository sessionRepository) {
        this.voteRepository = voteRepository;
        this.associateRepository = associateRepository;
        this.sessionRepository = sessionRepository;
    }

    public String createVote(UUID associateId, UUID sessionId, VoteRequestDTO data) {
        Associate associate = associateRepository.findById(associateId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new IllegalArgumentException("Session not found"));

        Vote votoExistente = voteRepository.findByAssociateAndSession(associate, session);
        if (votoExistente != null) {
            return "Você já votou nesta sessão!";
        }

        Vote voto = new Vote();
        voto.setValue(data.value());
        voto.setAssociate(associate);
        voto.setSession(session);
        voteRepository.save(voto);

        session.setCountVoto(session.getCountVoto() + 1);
        sessionRepository.save(session);

        return "Voto registrado com sucesso!";
    }

    public List<Session> results() {
        return sessionRepository.findAll();
    }

    public String validCpf(){

        Faker faker = new Faker();
        List<String> cpfsValidos = new ArrayList<>();
        List<String> cpfsInvalidos = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String cpfValido = faker.idNumber().valid();
            cpfsValidos.add(cpfValido);
        }

        for (int i = 0; i < 5; i++) {
            String cpfValido = faker.idNumber().valid();
            String cpfInvalido = cpfValido.substring(0, 9) + "00";
            cpfsInvalidos.add(cpfInvalido);
        }

        List<Object[]> cpfsFinal = new ArrayList<>();
        for (String cpf : cpfsValidos) {
            cpfsFinal.add(new Object[]{cpf, true});
        }
        for (String cpf : cpfsInvalidos) {
            cpfsFinal.add(new Object[]{cpf, false});

        }

        Collections.shuffle(cpfsFinal);

        Object[] primeiroSorteado = cpfsFinal.get(0);
        String cpfSorteado = (String) primeiroSorteado[0];
        boolean valido = (boolean) primeiroSorteado[1];

        if (valido) {
            return "ABLE_TO_VOTE";
        } else {
            //HttpStatus.NOT_FOUND;
            return "UNABLE_TO_VOTE";
        }

    }


}
