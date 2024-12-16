package com.votacao.desafiovotacao.domain.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Setter
@Getter
@Builder
@Document
public class Agenda {
    @Id
    private String id;
    private String title;
    private String description;
    private String status;
}

