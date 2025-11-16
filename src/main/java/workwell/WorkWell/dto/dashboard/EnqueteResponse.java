package workwell.WorkWell.dto.dashboard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EnqueteResponse(
	UUID id,
	String pergunta,
	Boolean ativa,
	LocalDateTime dataFim,
	LocalDateTime createdAt,
	Long totalRespostas,
	Long totalUsuarios,
	Double taxaResposta,
	List<RespostaEstatisticaResponse> estatisticas,
	Boolean jaRespondeu,
	List<String> opcoesResposta
) {
}

