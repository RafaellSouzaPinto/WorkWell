package workwell.WorkWell.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import workwell.WorkWell.entity.ParticipacaoAtividade;

public interface ParticipacaoAtividadeRepository extends JpaRepository<ParticipacaoAtividade, UUID> {

	Optional<ParticipacaoAtividade> findByAtividadeIdAndUsuarioId(UUID atividadeId, UUID usuarioId);

	List<ParticipacaoAtividade> findByAtividadeId(UUID atividadeId);

	@Query("""
		select count(p) from ParticipacaoAtividade p
		where p.atividade.id = :atividadeId
	""")
	Long contarParticipacoesPorAtividade(@Param("atividadeId") UUID atividadeId);

	@Query("""
		select count(p) from ParticipacaoAtividade p
		where p.atividade.id = :atividadeId
		and p.vaiParticipar = true
	""")
	Long contarParticipantesConfirmados(@Param("atividadeId") UUID atividadeId);

	@Query("""
		select count(distinct p.usuario.id) from ParticipacaoAtividade p
		where p.atividade.empresa.id = :empresaId
		and p.atividade.dataHoraInicio >= :dataInicio
	""")
	Long contarUsuariosUnicosParticipantes(@Param("empresaId") UUID empresaId, @Param("dataInicio") LocalDateTime dataInicio);
}

