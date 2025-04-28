package com.desafiofullstask.votacao.service;


import com.desafiofullstask.votacao.entity.Vote;
import com.desafiofullstask.votacao.repository.AssociatedRepository;
import com.desafiofullstask.votacao.repository.VoteRepository;
import com.desafiofullstask.votacao.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Random;

@Service
public class VoteService {

    private final VoteRepository voteRepository;

    private final SaveTheVote saveTheVote;

    private final AssociatedRepository associatedRepository;

    private static final Random RANDOM = new Random();

    private static final Logger logger = LoggerFactory.getLogger(AssociatedService.class);


    public VoteService(VoteRepository voteRepository, AssociatedRepository associatedRepository) {
        this.voteRepository = voteRepository;
        this.saveTheVote = new SaveTheVote(voteRepository);
        this.associatedRepository = associatedRepository;
    }

    public String save(Vote vote) {
        if (ValidationUtil.validateCPF(vote.getAssociatedCpf())) {

            /**
             * Esse randomNumber é apenas para simular o sucesso ou falha do voto.
             */
            int randomNumber = RANDOM.nextInt(2) + 1;
            if (associatedRepository.findByCpf(vote.getAssociatedCpf()) != null) {
                if (randomNumber == 1) {
                    Optional<Vote> existVote = voteRepository.findByAssociatedCpfAndDiscussId(vote.getAssociatedCpf(), vote.getDiscussId());
                    if (existVote.isPresent()) {
                        logger.info("Vote already exists for CPF: {}", vote.getAssociatedCpf());
                        return "CPF_ALREADY_VOTED";
                    } else {
                        saveTheVote.saveVote(vote);
                        return "ABLE_TO_VOTE";
                    }
                } else {
                    logger.info("Infelizmente, não conseguimos processar seu voto.");
                    return "UNABLE_TO_VOTE";
                }
            }else {
                logger.info("Associado não cadastrado");
                return "ASSOCIATED_NOT_REGISTERED";
            }
            } else {
                logger.info("Invalid CPF: {}", vote.getAssociatedCpf());
                return "CPF_INVALID";
            }
        }

    public Vote findVoteById(Integer id) {
        return voteRepository.findById(id).orElse(null);
    }

    public void delete(Integer id) {
        voteRepository.deleteById(id);
    }
}
