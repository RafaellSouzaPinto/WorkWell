package workwell.WorkWell.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import workwell.WorkWell.entity.AtividadeBemEstar;

public interface AtividadeBemEstarRepository extends JpaRepository<AtividadeBemEstar, UUID> {

	List<AtividadeBemEstar> findByEmpresaIdAndAtivaTrueOrderByDataHoraInicioDesc(UUID empresaId);

	Optional<AtividadeBemEstar> findByIdAndEmpresaId(UUID id, UUID empresaId);

	@Query("""
		select a from AtividadeBemEstar a
		where a.empresa.id = :empresaId
		and a.ativa = true
		and a.dataHoraInicio >= :dataInicio
		order by a.dataHoraInicio desc
	""")
	List<AtividadeBemEstar> buscarAtividadesRecentes(@Param("empresaId") UUID empresaId, @Param("dataInicio") LocalDateTime dataInicio);

	@Query("""
		select a from AtividadeBemEstar a
		where a.empresa.id = :empresaId
		and a.ativa = true
		and a.dataHoraInicio >= :inicioDia
		and a.dataHoraInicio < :fimDia
		order by a.dataHoraInicio asc
	""")
	List<AtividadeBemEstar> buscarAtividadesDoDia(@Param("empresaId") UUID empresaId, 
		@Param("inicioDia") LocalDateTime inicioDia, 
		@Param("fimDia") LocalDateTime fimDia);
}

