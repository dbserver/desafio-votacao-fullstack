package com.desafiofullstask.votacao.service;

import com.desafiofullstask.votacao.entity.Discuss;
import com.desafiofullstask.votacao.enums.SessionStatusEmum;
import com.desafiofullstask.votacao.repository.DiscussRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class DiscussService {

    @Autowired
    private DiscussRepository discussRepository;

    @Autowired
    private SessionService sessionService;

    private SessionStatusEmum status;

    public Discuss findDiscussById(Integer id) {
        return discussRepository.findById(id).orElse(null);
    }

    public Page<Discuss> findAllDiscuss(int page, int searchStatus) {

        int pageLimit = 10;
        int pageOffset = page;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageOffset, pageLimit, sort);
        if(searchStatus != 0){
            return discussRepository.findBySessionStatus(searchStatus, pageRequest);
        }else{
            return discussRepository.findAll(pageRequest);
        }

    }

    public Discuss save(Discuss discuss) {
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
        Timestamp timestamp = Timestamp.from(zonedDateTime.toInstant());
        discuss.getSession().setCreatedTime(timestamp);
        discuss.getSession().setStatus(SessionStatusEmum.OPEN.ordinal());
        sessionService.save(discuss.getSession());
        return discussRepository.save(discuss);
    }

    public void delete(Integer id) {
        discussRepository.deleteById(id);
    }

}
