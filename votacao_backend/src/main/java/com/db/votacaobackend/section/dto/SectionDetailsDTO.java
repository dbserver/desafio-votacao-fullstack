package com.db.votacaobackend.section.dto;

import com.db.votacaobackend.agenda.model.Agenda;
import com.db.votacaobackend.vote.dto.VoteDetailDTO;
import java.time.LocalDateTime;
import java.util.List;

public record SectionDetailsDTO(Long id, Agenda agendaId, Integer duration, LocalDateTime start,
                                LocalDateTime end, List<VoteDetailDTO> votes) {

}
