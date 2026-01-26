package com.service.polls.application.dto;

import com.service.polls.application.annotations.EnumValidator;
import com.service.polls.application.enums.AnswerEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO {

    @NotNull
    private Long pollId;

    @NotEmpty
    private String document;

    @NotNull
    @EnumValidator(enumClass = AnswerEnum.class)
    private AnswerEnum answer;
}
