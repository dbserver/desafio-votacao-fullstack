package br.tec.db.voting_api.service;

import br.tec.db.voting_api.domain.Agenda;
import br.tec.db.voting_api.domain.Session;
import br.tec.db.voting_api.dto.input.SessionInputDTO;
import br.tec.db.voting_api.exception.BusinessException;
import br.tec.db.voting_api.repository.AgendaRepository;
import br.tec.db.voting_api.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final AgendaRepository agendaRepository;

    public void openSession(Long agendaId, SessionInputDTO sessionInputDTO) {

        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new BusinessException("Pauta não encontrada!"));

        Optional<Session> sessionOptional = sessionRepository.findByAgendaId(agendaId);
        if (sessionOptional.isPresent()) {
            if (LocalDateTime.now().isBefore(sessionOptional.get().getClosingDate()))
                throw new BusinessException("Já existe uma sessão aberta para essa pauta");
        }

        LocalDateTime now = LocalDateTime.now();

        int duration = sessionInputDTO.durationMinutes() != null ? sessionInputDTO.durationMinutes() : 1;

        Session session = Session.builder()
                .agenda(agenda)
                .openingDate(now)
                .closingDate(now.plusMinutes(duration))
                .build();

        sessionRepository.save(session);
    }
}
