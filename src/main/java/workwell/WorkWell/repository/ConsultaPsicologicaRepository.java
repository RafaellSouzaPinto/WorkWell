package workwell.WorkWell.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import workwell.WorkWell.entity.ConsultaPsicologica;
import workwell.WorkWell.entity.enums.ConsultaStatus;
import workwell.WorkWell.entity.enums.LocalAtendimento;
import workwell.WorkWell.entity.enums.SalaAtendimento;

public interface ConsultaPsicologicaRepository extends JpaRepository<ConsultaPsicologica, UUID> {

	@Query("""
		select case when count(c) > 0 then true else false end
		from ConsultaPsicologica c
		where c.empresa.id = :empresaId
			and c.localAtendimento = :local
			and c.sala = :sala
			and c.status in ('PENDENTE_CONFIRMACAO', 'CONFIRMADA')
			and c.dataHoraInicio < :fim
			and c.dataHoraFim > :inicio
	""")
	boolean existsSalaOcupada(@Param("empresaId") UUID empresaId,
		@Param("local") LocalAtendimento local,
		@Param("sala") SalaAtendimento sala,
		@Param("inicio") LocalDateTime inicio,
		@Param("fim") LocalDateTime fim);

	@Query("""
		select case when count(c) > 0 then true else false end
		from ConsultaPsicologica c
		where c.empresa.id = :empresaId
			and c.status in ('PENDENTE_CONFIRMACAO', 'CONFIRMADA')
			and (c.funcionario.id = :usuarioId or c.psicologo.id = :usuarioId)
			and c.dataHoraInicio < :fim
			and c.dataHoraFim > :inicio
	""")
	boolean existsConflitoAgenda(@Param("empresaId") UUID empresaId,
		@Param("usuarioId") UUID usuarioId,
		@Param("inicio") LocalDateTime inicio,
		@Param("fim") LocalDateTime fim);

	@Query("""
		select c from ConsultaPsicologica c
		where c.empresa.id = :empresaId
			and (c.funcionario.id = :usuarioId or c.psicologo.id = :usuarioId)
			and c.status in :status
		order by c.dataHoraInicio
	""")
	List<ConsultaPsicologica> buscarPorParticipanteEStatus(@Param("empresaId") UUID empresaId,
		@Param("usuarioId") UUID usuarioId,
		@Param("status") List<ConsultaStatus> status);

	@Query("""
		select c from ConsultaPsicologica c
		where c.empresa.id = :empresaId
			and c.aguardandoConfirmacaoDe.id = :usuarioId
			and c.status = 'PENDENTE_CONFIRMACAO'
		order by c.dataHoraInicio
	""")
	List<ConsultaPsicologica> buscarPendentesParaUsuario(@Param("empresaId") UUID empresaId,
		@Param("usuarioId") UUID usuarioId);

	Optional<ConsultaPsicologica> findByIdAndEmpresaId(UUID id, UUID empresaId);

	@Query("""
		select c from ConsultaPsicologica c
		where c.empresa.id = :empresaId
			and (c.funcionario.id = :usuarioId or c.criadoPor.id = :usuarioId)
		order by c.dataHoraInicio desc
	""")
	List<ConsultaPsicologica> buscarPorFuncionarioOuCriadoPor(@Param("empresaId") UUID empresaId,
		@Param("usuarioId") UUID usuarioId);
}

