package com.service.polls.domain.model;

import com.service.polls.application.dto.VoteDTO;
import com.service.polls.application.enums.AnswerEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vote {
    private Long pollId;
    private String document;
    private AnswerEnum answer;

    public static Vote of(VoteDTO voteDTO) {
        return Vote.builder()
                .answer(voteDTO.getAnswer())
                .pollId(voteDTO.getPollId())
                .document(voteDTO.getDocument())
                .build();
    }
}
