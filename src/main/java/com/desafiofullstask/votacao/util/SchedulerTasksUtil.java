package com.desafiofullstask.votacao.util;

import com.desafiofullstask.votacao.repository.DiscussRepository;
import com.desafiofullstask.votacao.repository.SessionRepository;
import com.desafiofullstask.votacao.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


@Component
public class SchedulerTasksUtil {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private DiscussRepository discussRepository;

    /**
     * Scheduled responsável por realizar o fechamento da sessão da pauta e fazer a contagem dos votos da mesma
     */

    @Scheduled(fixedRate = 60000)
    public void scheduleChangeStatusOfSessions() {

        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
        Timestamp timestamp = Timestamp.from(zonedDateTime.toInstant());

        sessionRepository.findSessionsToChangeStatus(timestamp).forEach(session -> {
            Timestamp newTime = new Timestamp(session.getCreatedTime().getTime() + ((long) session.getDuration() * 60 * 1000));
            if (newTime.before(timestamp)) {
                discussRepository.findBySessionId(session.getId()).ifPresent(discuss -> {
                    discuss.setTotalVotesYes(voteRepository.countVotesYesByDiscussId(discuss.getId()));
                    discuss.setTotalVotesNo(voteRepository.countVotesNoByDiscussId(discuss.getId()));
                    discussRepository.save(discuss);
                });
                session.setStatus(2);
                sessionRepository.save(session);
            }
        });
    }
}
