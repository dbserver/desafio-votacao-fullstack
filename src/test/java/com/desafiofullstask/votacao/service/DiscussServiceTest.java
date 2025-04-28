package com.desafiofullstask.votacao.service;

import com.desafiofullstask.votacao.entity.Discuss;
import com.desafiofullstask.votacao.repository.DiscussRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DiscussServiceTest {

    @Autowired
    private DiscussService discussService;

    @Autowired
    private DiscussRepository discussRepository;

    @Test
    void findDiscussById() {
        Discuss discuss = discussService.findDiscussById(1);
        assertThat(discuss).isNotNull();
        assertThat(discuss.getName()).isEqualTo("Discuss 1");
        assertThat(discuss.getDescription()).isEqualTo("Description 1");
    }

    @Test
    void save() {
        Discuss discuss = new Discuss();
        discuss.setName("Discuss 1");
        discuss.setDescription("Description 1");
        discuss = discussService.save(discuss);

        assertThat(discuss.getId()).isNotNull();
    }

    @Test
    void delete() {
        Discuss discuss = new Discuss();
        discuss.setName("Discuss 3");
        discuss.setDescription("Description 3");
        discuss = discussService.save(discuss);

        discussService.delete(discuss.getId());

        Discuss retrievedDiscuss = discussRepository.findById(discuss.getId()).orElse(null);
        assertThat(retrievedDiscuss).isNull();
    }
}