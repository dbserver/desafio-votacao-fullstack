package com.service.polls.adapters.out.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {
    private String input;
    private boolean isValid;
    private String type;
    private String formatted;
    private String unformatted;
    private String message;

}
