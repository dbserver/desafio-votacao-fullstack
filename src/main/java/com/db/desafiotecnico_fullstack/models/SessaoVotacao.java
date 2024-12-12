package com.db.desafiotecnico_fullstack.models;

import com.db.desafiotecnico_fullstack.models.enuns.Status;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Table(name = "sessaovotacao")
@Entity(name = "sessaovotacao")
@Getter
@Setter
public class SessaoVotacao {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ABERTA;


}
