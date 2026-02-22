package dev.rodrigo.desafiovotacao.entity;

import dev.rodrigo.desafiovotacao.enums.TipoVoto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
    name = "voto",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"sessao_id", "cpf"})},
    indexes = {@Index(name = "idx_voto_sessao_tipo", columnList = "sessao_id, tipoVoto")})
public class Voto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "sessao_id")
  private SessaoVotacao sessao;

  @Embedded private Cpf cpf;

  @Enumerated(EnumType.STRING)
  private TipoVoto tipoVoto;

  @Column(nullable = false)
  private LocalDateTime dataHora;
}
