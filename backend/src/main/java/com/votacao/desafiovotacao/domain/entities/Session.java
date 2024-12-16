package com.votacao.desafiovotacao.domain.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@Document
public class Session {
    @Id
    private String id;
    private String agendaId;
    private LocalDateTime startTime;
    private LocalDateTime closedTime;
    private StatusSession status;

    public enum StatusSession {
        ABERTA, ENCERRADA
    }
}




