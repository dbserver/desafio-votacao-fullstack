package com.desafiofullstask.votacao;

import com.desafiofullstask.votacao.entity.Associated;
import com.desafiofullstask.votacao.entity.Discuss;
import com.desafiofullstask.votacao.entity.Session;
import com.desafiofullstask.votacao.entity.Vote;
import com.desafiofullstask.votacao.repository.AssociatedRepository;
import com.desafiofullstask.votacao.repository.DiscussRepository;
import com.desafiofullstask.votacao.repository.SessionRepository;
import com.desafiofullstask.votacao.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VotacaoApplicationTests {

	@Autowired
	private DiscussRepository discussRepository;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private AssociatedRepository associatedRepository;

	@Autowired
	private VoteRepository voteRepository;

	@Test
	public void testSaveAndRetrieveDiscuss() {
		Session session = new Session();
		session.setDuration(10);
		session = sessionRepository.save(session);

		Discuss discuss = new Discuss();
		discuss.setName("Discussão 1");
		discuss.setDescription("Descrição da discussão 1");
		discuss.setSession(session);
		discuss = discussRepository.save(discuss);

		assertThat(discuss.getId()).isNotNull();

		Discuss retrievedDiscuss = discussRepository.findById(discuss.getId()).orElse(null);
		assertThat(retrievedDiscuss).isNotNull();
		assertThat(retrievedDiscuss.getSession()).isNotNull();
		assertThat(retrievedDiscuss.getSession().getDuration()).isEqualTo(10L);
	}

	@Test
	public void createAssociated() {
		Associated associated = new Associated();
		associated.setCpf("12345678900");
		associated = associatedRepository.save(associated);
		assertThat(associated.getId()).isNotNull();
	}

	@Test
	public void testAssociateHadOneVote() {

		Discuss discuss = discussRepository.findById(1).orElse(null);
		Associated associated = associatedRepository.findById(1).orElse(null);

		Vote vote = new Vote();
        assert associated != null;
		assert discuss != null;
		vote.setDiscussId(discuss.getId());
		vote.setVote("Sim");

		vote = voteRepository.save(vote);
		assertThat(vote.getId()).isNotNull();

	}

	@Test
	public void testAssociatedCantMoreThanOneVote() {

		Discuss discuss = discussRepository.findById(1).orElse(null);
		Associated associated = associatedRepository.findById(1).orElse(null);
		Vote vote = voteRepository.findById(1).orElse(null);

		assert vote != null;
		assert associated != null;
		assert discuss != null;

		if(Objects.equals(vote.getAssociatedCpf(), associated.getCpf())) {
			System.out.println("Associado já votou");
		} else {
			vote.setAssociatedCpf(associated.getCpf());
			vote.setDiscussId(discuss.getId());
			vote.setVote("Sim");
			vote = voteRepository.save(vote);
			assertThat(vote.getId()).isNotNull();
		}
	}
}


