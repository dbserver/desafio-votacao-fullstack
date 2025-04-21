package com.db.votacaobackend.section.mapper;

import com.db.votacaobackend.section.dto.SectionDetailsDTO;
import com.db.votacaobackend.section.model.Section;
import com.db.votacaobackend.vote.dto.VoteDetailDTO;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SectionMapper {


  public SectionDetailsDTO toDto(Section section) {
    List<VoteDetailDTO> voteDTOs = section.getVotes() != null
        ? section.getVotes().stream()
        .map(v -> new VoteDetailDTO(v.getId(), v.getVoteOption(), v.getCpf()))
        .toList()
        : List.of();

    return new SectionDetailsDTO(
        section.getId(),
        section.getAgendaId(),
        section.getDuration(),
        section.getStart(),
        section.getEnd(),
        voteDTOs
    );
  }
}
