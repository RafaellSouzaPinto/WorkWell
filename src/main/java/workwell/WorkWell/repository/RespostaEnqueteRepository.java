package workwell.WorkWell.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import workwell.WorkWell.entity.RespostaEnquete;

public interface RespostaEnqueteRepository extends JpaRepository<RespostaEnquete, UUID> {

	Optional<RespostaEnquete> findByEnqueteIdAndUsuarioId(UUID enqueteId, UUID usuarioId);

	List<RespostaEnquete> findByEnqueteId(UUID enqueteId);

	@Query("""
		select count(r) from RespostaEnquete r
		where r.enquete.id = :enqueteId
	""")
	Long contarRespostasPorEnquete(@Param("enqueteId") UUID enqueteId);

	@Query("""
		select r.resposta, count(r) as total
		from RespostaEnquete r
		where r.enquete.id = :enqueteId
		group by r.resposta
	""")
	List<Object[]> contarRespostasPorTipo(@Param("enqueteId") UUID enqueteId);
}

