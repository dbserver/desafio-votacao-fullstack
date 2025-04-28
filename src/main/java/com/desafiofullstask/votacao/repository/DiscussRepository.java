package com.desafiofullstask.votacao.repository;

import com.desafiofullstask.votacao.entity.Discuss;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DiscussRepository extends JpaRepository<Discuss, Integer> {

    Optional<Discuss> findBySessionId(Integer id);

    Page<Discuss> findBySessionStatus(int searchStatus, PageRequest pageRequest);
}
