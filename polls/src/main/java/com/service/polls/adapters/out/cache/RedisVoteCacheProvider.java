package com.service.polls.adapters.out.cache;

import com.service.polls.application.enums.AnswerEnum;
import com.service.polls.domain.exceptions.UserHasAlreadyVotedException;
import com.service.polls.domain.model.Vote;
import com.service.polls.ports.out.IVoteCacheProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisVoteCacheProvider implements IVoteCacheProvider {

    private final StringRedisTemplate redis;

    public RedisVoteCacheProvider(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @Override
    public void validateVote(Vote vote) {
        Boolean success = redis.opsForValue()
                .setIfAbsent(String.format("vote:%s:%s", vote.getPollId(), vote.getDocument()), "1");

        if(Boolean.FALSE.equals(success)) {
            throw new UserHasAlreadyVotedException();
        }
    }

    @Override
    public void increment(Vote vote) {
        redis.opsForValue().increment(String.format("poll:%s:option:%s", vote.getPollId(), vote.getAnswer()));
    }

    private String key(Long pollId, AnswerEnum answer) {
        return "poll:" + pollId + ":option:" + answer.name();
    }

    @Override
    public long countVotes(Long pollId, AnswerEnum answer) {
        String value = redis.opsForValue().get(key(pollId, answer));
        return value != null ? Long.parseLong(value) : 0L;
    }
}
