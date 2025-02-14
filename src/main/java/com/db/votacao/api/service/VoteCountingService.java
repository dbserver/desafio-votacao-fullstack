package com.db.votacao.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class VoteCountingService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String VOTE_COUNT_KEY = "vote:session:%d:%s";

    public VoteCountingService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void incrementVote(Long sessionId, boolean voteValue) {
        String key = String.format(VOTE_COUNT_KEY, sessionId, voteValue ? "yes" : "no");
        redisTemplate.opsForValue().increment(key);
    }

    public long getVoteCount(Long sessionId, boolean voteValue) {
        String key = String.format(VOTE_COUNT_KEY, sessionId, voteValue ? "yes" : "no");
        String count = (String) redisTemplate.opsForValue().get(key);
        return count != null ? Long.parseLong(count) : 0;
    }
}
