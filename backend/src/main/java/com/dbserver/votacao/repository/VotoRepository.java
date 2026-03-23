package com.dbserver.votacao.repository;

import com.dbserver.votacao.entity.Voto;
import com.dbserver.votacao.enums.VotoOpcao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsBySessaoIdAndAssociadoId(Long sessaoId, String associadoId);

    long countBySessaoIdAndVoto(Long sessaoId, VotoOpcao voto);
}
