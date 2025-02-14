package com.db.votacao.api.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String VOTE_COUNT_KEY = "vote:session:%d:%s";
    private static final String VOTE_LOCK_KEY = "vote:lock:session:%d:user:%s";

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void incrementVoteCount(Long sessionId, boolean voteValue) {
        String key = String.format(VOTE_COUNT_KEY, sessionId, voteValue ? "yes" : "no");
        redisTemplate.opsForValue().increment(key);
    }

    public Long getVoteCount(Long sessionId, boolean voteValue) {
        String key = String.format(VOTE_COUNT_KEY, sessionId, voteValue ? "yes" : "no");
        String count = (String) redisTemplate.opsForValue().get(key);
        return count != null ? Long.parseLong(count) : 0L;
    }

    public boolean tryLockUserVote(Long sessionId, String userId, long timeoutSeconds) {
        String key = String.format(VOTE_LOCK_KEY, sessionId, userId);
        return Boolean.TRUE.equals(
                redisTemplate.opsForValue().setIfAbsent(key, "locked", timeoutSeconds, TimeUnit.SECONDS)
        );
    }
}
