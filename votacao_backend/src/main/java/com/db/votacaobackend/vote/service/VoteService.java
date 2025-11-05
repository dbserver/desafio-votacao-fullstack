package com.db.votacaobackend.vote.service;

import com.db.votacaobackend.section.model.Section;
import com.db.votacaobackend.section.service.SectionService;
import com.db.votacaobackend.vote.dto.VoteCreateDTO;
import com.db.votacaobackend.vote.dto.VoteDetailDTO;
import com.db.votacaobackend.vote.dto.VoteResultsDTO;
import com.db.votacaobackend.vote.exception.VoteBadRequestException;
import com.db.votacaobackend.vote.exception.VoteConflictException;
import com.db.votacaobackend.vote.mapper.VoteMapper;
import com.db.votacaobackend.vote.model.Vote;
import com.db.votacaobackend.vote.model.VoteOption;
import com.db.votacaobackend.vote.repository.VoteRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VoteService {

  private final VoteRepository repository;
  private final SectionService sectionService;
  private final VoteMapper mapper;

  public VoteDetailDTO createVote(VoteCreateDTO vote) {
    Section section = sectionService.getById(vote.sectionId());
    Vote cpfVoted = verifyVotesByCpf(vote);

    if(section.getEnd().isBefore(LocalDateTime.now())){
      throw new VoteBadRequestException("Sessão já está encerrada!");
    }

    if (cpfVoted != null) {
      throw new VoteConflictException("Este CPF já votou!");
    }

    Vote createdVote = createNewVote(vote, section);

    Vote newVote = repository.save(createdVote);
    return mapper.toDto(newVote);
  }

  private Vote verifyVotesByCpf(VoteCreateDTO vote) {
    return repository.findByCpfAndSectionId(vote.cpf(), vote.sectionId());
  }

  private Vote createNewVote(VoteCreateDTO vote, Section section) {

    return Vote.builder()
        .cpf(vote.cpf())
        .voteOption(vote.voteOption())
        .section(section)
        .build();
  }

  public List<VoteDetailDTO> listAllVotes() {
    return repository.findAll()
        .stream()
        .map(mapper::toDto)
        .toList();
  }

  public VoteResultsDTO listVotesFromSession(Long sectionId) {

    Section sectionById =  sectionService.getById(sectionId);

    List<Vote> sectionVotes = sectionById.getVotes();

    long simCount = sectionVotes.stream()
        .filter(v -> v.getVoteOption() == VoteOption.SIM)
        .count();
    long naoCount = sectionVotes.stream()
        .filter(v -> v.getVoteOption() == VoteOption.NAO)
        .count();

    return new VoteResultsDTO(simCount, naoCount);
  }
}
