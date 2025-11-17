package workwell.WorkWell.dto.dashboard;

import java.time.LocalDateTime;
import java.util.UUID;
import workwell.WorkWell.entity.enums.TipoAtividadeBemEstar;

public record ParticipacaoHistoricoResponse(
	UUID id,
	UUID atividadeId,
	TipoAtividadeBemEstar tipo,
	String titulo,
	LocalDateTime dataHoraInicio,
	Boolean participou
) {
}

