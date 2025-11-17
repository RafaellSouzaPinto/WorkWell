package workwell.WorkWell.dto.dashboard;

import java.time.LocalDateTime;
import java.util.UUID;
import workwell.WorkWell.entity.enums.TipoAtividadeBemEstar;

public record AtividadeDiaResponse(
	UUID id,
	TipoAtividadeBemEstar tipo,
	String titulo,
	String descricao,
	LocalDateTime dataHoraInicio,
	LocalDateTime dataHoraFim,
	String local,
	Boolean vaiParticipar,
	Long minutosRestantes
) {
}

