package workwell.WorkWell.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import workwell.WorkWell.entity.DenunciaEtica;

public interface DenunciaEticaRepository extends JpaRepository<DenunciaEtica, UUID> {

	List<DenunciaEtica> findByEmpresaIdOrderByCreatedAtDesc(UUID empresaId);

	Page<DenunciaEtica> findByEmpresaIdOrderByCreatedAtDesc(UUID empresaId, Pageable pageable);

	@Query("""
		select d from DenunciaEtica d
		where d.empresa.id = :empresaId
		and d.status = :status
		order by d.createdAt desc
	""")
	List<DenunciaEtica> findByEmpresaIdAndStatusOrderByCreatedAtDesc(
		@Param("empresaId") UUID empresaId,
		@Param("status") String status
	);

	@Query("""
		select d from DenunciaEtica d
		where d.empresa.id = :empresaId
		and d.status = :status
		order by d.createdAt desc
	""")
	Page<DenunciaEtica> findByEmpresaIdAndStatusOrderByCreatedAtDesc(
		@Param("empresaId") UUID empresaId,
		@Param("status") String status,
		Pageable pageable
	);

	@Query("""
		select count(d) from DenunciaEtica d
		where d.empresa.id = :empresaId
		and d.status = 'PENDENTE'
	""")
	Long contarDenunciasPendentes(@Param("empresaId") UUID empresaId);
}

