package com.sarapio.votacao_api.controller;

import com.sarapio.votacao_api.domain.topic.Topic;
import com.sarapio.votacao_api.domain.topic.TopicRequestDTO;
import com.sarapio.votacao_api.service.TopicService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping(value = "/createTopic", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Topic> createTopic(@RequestBody TopicRequestDTO data) {
        Topic topic = topicService.createTopic(data);

        return ResponseEntity.ok(topic);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Topic> listTopics() {
        return topicService.listTopics();
    }


}
