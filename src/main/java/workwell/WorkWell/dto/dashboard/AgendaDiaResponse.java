package workwell.WorkWell.dto.dashboard;

import java.util.List;
import workwell.WorkWell.dto.apoio.ConsultaResponse;

public record AgendaDiaResponse(
	List<AtividadeDiaResponse> atividades,
	List<ConsultaResponse> consultas,
	List<NotificacaoResponse> notificacoes
) {
}

