package workwell.WorkWell.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import workwell.WorkWell.entity.Enquete;

public interface EnqueteRepository extends JpaRepository<Enquete, UUID> {

	List<Enquete> findByEmpresaIdAndAtivaTrueOrderByCreatedAtDesc(UUID empresaId);

	Page<Enquete> findByEmpresaIdAndAtivaTrueOrderByCreatedAtDesc(UUID empresaId, Pageable pageable);

	Optional<Enquete> findByIdAndEmpresaId(UUID id, UUID empresaId);

	@Query("""
		select e from Enquete e
		where e.empresa.id = :empresaId
		and e.ativa = true
		and (e.dataFim is null or e.dataFim > CURRENT_TIMESTAMP)
		order by e.createdAt desc
	""")
	List<Enquete> buscarEnquetesAtivas(@Param("empresaId") UUID empresaId);

	@Query("""
		select e from Enquete e
		where e.empresa.id = :empresaId
		and e.ativa = true
		and (e.dataFim is null or e.dataFim > CURRENT_TIMESTAMP)
		order by e.createdAt desc
	""")
	Page<Enquete> buscarEnquetesAtivas(@Param("empresaId") UUID empresaId, Pageable pageable);
}

