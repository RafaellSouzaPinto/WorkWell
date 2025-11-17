package workwell.WorkWell.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import workwell.WorkWell.entity.RespostaAvaliacaoProfunda;

public interface RespostaAvaliacaoProfundaRepository extends JpaRepository<RespostaAvaliacaoProfunda, UUID> {

	Optional<RespostaAvaliacaoProfunda> findByAvaliacaoIdAndUsuarioId(UUID avaliacaoId, UUID usuarioId);

	List<RespostaAvaliacaoProfunda> findByAvaliacaoId(UUID avaliacaoId);

	@Query("""
		select count(r) from RespostaAvaliacaoProfunda r
		where r.avaliacao.id = :avaliacaoId
	""")
	Long contarRespostasPorAvaliacao(@Param("avaliacaoId") UUID avaliacaoId);

	@Query("""
		select r from RespostaAvaliacaoProfunda r
		where r.avaliacao.empresa.id = :empresaId
		and r.avaliacao.id = :avaliacaoId
		order by r.createdAt desc
	""")
	List<RespostaAvaliacaoProfunda> buscarRespostasPorAvaliacao(@Param("empresaId") UUID empresaId, @Param("avaliacaoId") UUID avaliacaoId);
}

