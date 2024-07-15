package com.dbserver.desafiovotacaofullstack.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbserver.desafiovotacaofullstack.domains.Associate;
import com.dbserver.desafiovotacaofullstack.domains.Session;
import com.dbserver.desafiovotacaofullstack.domains.Vote;
import com.dbserver.desafiovotacaofullstack.dtos.TotalVotesDto;
import com.dbserver.desafiovotacaofullstack.dtos.VoteRequestDto;
import com.dbserver.desafiovotacaofullstack.dtos.VoteResponseDto;
import com.dbserver.desafiovotacaofullstack.exceptions.ResourceNotFoundException;
import com.dbserver.desafiovotacaofullstack.repositories.AssociateRepository;
import com.dbserver.desafiovotacaofullstack.repositories.SessionRepository;
import com.dbserver.desafiovotacaofullstack.repositories.VoteRepository;

@Service
public class VoteService {
	
	@Autowired
	VoteRepository voteRepository;
	
	@Autowired
	SessionRepository sessionRepository;
	
	@Autowired
	AssociateRepository associateRepository;
	
	@Autowired
	SessionService sessionService;
	
	public static String VOTE_POSITIVE = "SIM";
	public static String VOTE_NEGATIVE = "NÃO";
	
	public VoteResponseDto createVote(VoteRequestDto voteRequestDto) {
		return saveVotePosValidation(voteRequestDto);
	}
	
	public TotalVotesDto getAllVotesBySession(Integer idSession) {
        List<VoteResponseDto> listVotes = voteRepository.findVoteBySession(idSession).stream().map(vote -> vote.entityToDto()).collect(Collectors.toList());
        return createTotalVotes(idSession, listVotes);
    }
	
	public TotalVotesDto createTotalVotes(Integer idSession, List<VoteResponseDto> votesResponseDto) {
		long qtdVotesPositives = voteRepository.countByResponseAndIdSessionId(VOTE_POSITIVE, idSession);
		long qtdVotesNegatives = voteRepository.countByResponseAndIdSessionId(VOTE_NEGATIVE, idSession);
		long totalVotes = qtdVotesPositives + qtdVotesNegatives;
		return new TotalVotesDto(totalVotes, qtdVotesPositives, qtdVotesNegatives, votesResponseDto);
	}
	
	public boolean voteValid(String response){
		String responseValid = response.trim().toUpperCase();
		return responseValid.equals(VOTE_POSITIVE) || responseValid.equals(VOTE_NEGATIVE);
	}
	
	public void preValidationCreateVote(Optional<Session> session, Optional<Associate> associate, VoteRequestDto voteRequestDto) {
		
		if(session.isEmpty())
			throw new ResourceNotFoundException("Sessão não encontrada.");
		
		if(associate.isEmpty())
			throw new ResourceNotFoundException("Associado não encontrado.");
		
		if(!sessionService.isSessionOpen(session.get().getId()))
			throw new RuntimeException("Está sessão está fechada.");
		
		if(!voteValid(voteRequestDto.response()))
			throw new RuntimeException("Informe os valores válidos: 'SIM' ou 'NÃO'");
		
		if(voteRepository.existsByIdSessionIdAndIdAssociateId(session.get().getId(), associate.get().getId()))
			throw new RuntimeException("Já existe um voto desse associado para está pauta.");
		
	}
	
	public VoteResponseDto saveVotePosValidation(VoteRequestDto voteRequestDto) {
		Optional<Session> session = sessionRepository.findById(voteRequestDto.idSession());
		Optional<Associate> associate = associateRepository.findById(voteRequestDto.idAssociate());
		preValidationCreateVote(session, associate, voteRequestDto);
		Vote vote = voteRepository.save(new Vote(session.get(), associate.get(), voteRequestDto.response().trim().toUpperCase()));
		return vote.entityToDto();
	}
	
}
