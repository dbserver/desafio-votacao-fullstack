package dev.rodrigo.desafiovotacao.repository;

import dev.rodrigo.desafiovotacao.entity.Voto;
import dev.rodrigo.desafiovotacao.enums.TipoVoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
  boolean existsBySessaoIdAndCpfNumero(Long sessaoId, String numero);

  long countBySessaoIdAndTipoVoto(Long sessaoId, TipoVoto voto);
  long countBySessao_Id(Long sessaoId);
}
