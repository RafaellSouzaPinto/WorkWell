package workwell.WorkWell.dto.dashboard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import workwell.WorkWell.entity.enums.TipoAtividadeBemEstar;

public record AtividadeBemEstarResponse(
	UUID id,
	TipoAtividadeBemEstar tipo,
	String titulo,
	String descricao,
	LocalDateTime dataHoraInicio,
	LocalDateTime dataHoraFim,
	String local,
	Boolean ativa,
	Long totalParticipantes,
	Long totalUsuarios,
	Double taxaParticipacao,
	Boolean jaParticipou,
	Boolean vaiParticipar,
	List<ParticipanteAtividadeResponse> participantes
) {
}

