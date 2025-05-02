package com.desafioVotacaoBackend.desafioVotacaoBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafioVotacaoBackend.desafioVotacaoBackend.model.Pauta;

public interface PautaRepository extends JpaRepository<Pauta, Long> {

}