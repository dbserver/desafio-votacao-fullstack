package com.sarapio.votacao_api.service;

import com.sarapio.votacao_api.domain.topic.Topic;
import com.sarapio.votacao_api.domain.topic.TopicRequestDTO;
import com.sarapio.votacao_api.repositories.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic createTopic(TopicRequestDTO data) {

        Topic topic = new Topic();
        topic.setTitle(data.title());
        topic.setDescription(data.description());

        return topicRepository.save(topic);
    }

    public List<Topic> listTopics() {
        return topicRepository.findAll();
    }

}
