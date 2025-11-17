package workwell.WorkWell.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import workwell.WorkWell.entity.AvaliacaoProfunda;

public interface AvaliacaoProfundaRepository extends JpaRepository<AvaliacaoProfunda, UUID> {

	Optional<AvaliacaoProfunda> findByIdAndEmpresaId(UUID id, UUID empresaId);

	@Query("""
		select a from AvaliacaoProfunda a
		where a.empresa.id = :empresaId
		and a.ativa = true
		and a.dataInicio <= CURRENT_TIMESTAMP
		and a.dataFim >= CURRENT_TIMESTAMP
		order by a.createdAt desc
	""")
	List<AvaliacaoProfunda> buscarAvaliacoesAtivas(@Param("empresaId") UUID empresaId);

	@Query("""
		select a from AvaliacaoProfunda a
		where a.empresa.id = :empresaId
		and a.criadoPor.id = :psicologoId
		order by a.createdAt desc
	""")
	List<AvaliacaoProfunda> buscarAvaliacoesPorPsicologo(@Param("empresaId") UUID empresaId, @Param("psicologoId") UUID psicologoId);

	List<AvaliacaoProfunda> findByEmpresaIdOrderByCreatedAtDesc(UUID empresaId);

	@Query("""
		select count(a) from AvaliacaoProfunda a
		where a.empresa.id = :empresaId
		and a.ativa = true
		and a.dataInicio >= :dataInicio
	""")
	Long contarAvaliacoesNoPeriodo(@Param("empresaId") UUID empresaId, @Param("dataInicio") LocalDateTime dataInicio);
}

