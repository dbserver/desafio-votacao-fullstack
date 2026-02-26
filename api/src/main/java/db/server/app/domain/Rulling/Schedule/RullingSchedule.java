package db.server.app.domain.Rulling.Schedule;

import db.server.app.domain.Rulling.Model.Rulling;
import db.server.app.domain.Rulling.Repository.RullingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RullingSchedule {
    private static final Logger log = LoggerFactory.getLogger(RullingSchedule.class);
    private final RullingRepository rullingRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public RullingSchedule(RullingRepository rullingRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.rullingRepository = rullingRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void checkRullingExpired() {
        LocalDateTime now = LocalDateTime.now();
        List<Rulling> items = this.rullingRepository.findRullingsExpireds(now);
        log.info("Rullings expireds: " + items.size());
        if (items.size() > 0) {
            items.forEach(itemRulling -> {
                itemRulling.setActive(false);
                this.rullingRepository.save(itemRulling);
                this.simpMessagingTemplate.convertAndSend("/topic/rullings/expired", itemRulling.getId());
            });
        }
    }
}
