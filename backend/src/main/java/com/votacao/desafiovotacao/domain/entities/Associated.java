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
public class Associated {
    @Id
    private String id;
    private String cpf;
    private String name;
}
