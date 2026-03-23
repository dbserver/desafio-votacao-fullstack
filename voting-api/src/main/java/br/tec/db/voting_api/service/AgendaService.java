package br.tec.db.voting_api.service;

import br.tec.db.voting_api.domain.Agenda;
import br.tec.db.voting_api.domain.Session;
import br.tec.db.voting_api.dto.input.AgendaInputDTO;
import br.tec.db.voting_api.dto.output.AgendaDetailOutputDTO;
import br.tec.db.voting_api.dto.output.AgendaOutputDTO;
import br.tec.db.voting_api.repository.AgendaRepository;
import br.tec.db.voting_api.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final SessionRepository sessionRepository;

    public AgendaOutputDTO createAgenda(AgendaInputDTO agendaInputDTO) {

        var agenda = Agenda.builder()
                .title(agendaInputDTO.title())
                .description(agendaInputDTO.description())
                .build();

        var agendaSaved = agendaRepository.save(agenda);

        return new AgendaOutputDTO(agendaSaved.getId(), agendaSaved.getTitle(), agendaSaved.getDescription());
    }
    
    public List<AgendaOutputDTO> getAllAgendas() {
        return agendaRepository.findAll()
                .stream()
                .map(agenda -> new AgendaOutputDTO(
                        agenda.getId(),
                        agenda.getTitle(),
                        agenda.getDescription()
                ))
                .collect(Collectors.toList());
    }

    public AgendaDetailOutputDTO getAgendaById(Long id) {
        Agenda agenda = agendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pauta não encontrada"));

        Optional<Session> session = sessionRepository.findByAgendaId(agenda.getId());
        boolean sessionOpen = false;
        boolean createdSession = false;
        String localTime = null;
        if (session.isPresent()) {
            createdSession = true;
            Session sessionEntity = session.get();
            sessionOpen = LocalDateTime.now().isBefore(sessionEntity.getClosingDate());
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(now, sessionEntity.getClosingDate());
            localTime = String.format("%02d:%02d:%02d", duration.toHours(), duration.toMinutes() % 60, duration.toSeconds() % 60);
        }

        return new AgendaDetailOutputDTO(agenda.getId(), agenda.getTitle(), agenda.getDescription(), createdSession, sessionOpen, localTime, agenda.getCreatedAt());
    }
}
