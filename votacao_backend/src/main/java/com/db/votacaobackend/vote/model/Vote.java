package com.db.votacaobackend.vote.model;

import com.db.votacaobackend.section.model.Section;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "votes")
@Getter
@Builder
@EqualsAndHashCode
public class Vote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private VoteOption voteOption;
  private String cpf;

  @ManyToOne
  @JoinColumn(name = "section_id", nullable = false)
  private Section section;
}
