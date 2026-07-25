package com.dbchal.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbchal.voting.domain.Pauta;

public interface PautaRepository extends JpaRepository<Pauta, Long> {

}
