package workwell.WorkWell.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import workwell.WorkWell.entity.RegistroHumor;

public interface RegistroHumorRepository extends JpaRepository<RegistroHumor, UUID> {

	@Query("""
		select avg(r.nivelHumor) from RegistroHumor r
		where r.empresa.id = :empresaId
		and r.createdAt >= :dataInicio
	""")
	Double calcularMediaHumor(@Param("empresaId") UUID empresaId, @Param("dataInicio") LocalDateTime dataInicio);

	@Query("""
		select r.setor, avg(r.nivelHumor) as mediaHumor, count(r) as total
		from RegistroHumor r
		where r.empresa.id = :empresaId
		and r.createdAt >= :dataInicio
		and r.setor is not null
		group by r.setor
		order by mediaHumor asc
	""")
	List<Object[]> buscarSetoresComEstresse(@Param("empresaId") UUID empresaId, @Param("dataInicio") LocalDateTime dataInicio);

	List<RegistroHumor> findByEmpresaIdOrderByCreatedAtDesc(UUID empresaId);
}

