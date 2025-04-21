package com.db.votacaobackend.section.repository;

import com.db.votacaobackend.section.model.Section;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

  List<Section> findByEndAfter(LocalDateTime now);
}
