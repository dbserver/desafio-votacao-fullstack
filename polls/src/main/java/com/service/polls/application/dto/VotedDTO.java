package com.service.polls.application.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VotedDTO {
    private String message;
}
