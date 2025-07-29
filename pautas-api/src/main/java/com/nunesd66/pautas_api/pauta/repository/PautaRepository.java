package com.nunesd66.pautas_api.pauta.repository;

import com.nunesd66.pautas_api.pauta.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

    @Query("select pa from Pauta pa " +
            "join pa.sessaoVotacao sv " +
            "where sv.dataFechamento < current_timestamp " +
            "order by sv.dataFechamento desc")
    List<Pauta> findAllClosed();

    @Query("select pa from Pauta pa " +
            "join pa.sessaoVotacao sv " +
            "where sv.dataFechamento >= current_timestamp " +
            "order by sv.dataFechamento desc")
    Optional<Pauta> getLastRecord();

    Pauta findByTitulo(String titulo);
}
