package com.votacao.desafiovotacao.domain.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Builder
@Document
public class Vote {
    @Id
    private String id;
    private String vote;
    private String associatedId;
    private Session session;
}
