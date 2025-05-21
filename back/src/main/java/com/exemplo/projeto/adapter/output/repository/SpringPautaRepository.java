package com.exemplo.projeto.adapter.output.repository;

import com.exemplo.projeto.adapter.output.entity.PautaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringPautaRepository extends JpaRepository<PautaEntity, Long> {

}
