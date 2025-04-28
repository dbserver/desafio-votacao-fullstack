package com.desafiofullstask.votacao.repository;

import com.desafiofullstask.votacao.entity.Associated;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociatedRepository extends JpaRepository<Associated, Integer> {

    Associated findByCpf(String cpf);
}
