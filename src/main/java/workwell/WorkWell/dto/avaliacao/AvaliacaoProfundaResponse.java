package workwell.WorkWell.dto.avaliacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record AvaliacaoProfundaResponse(
	UUID id,
	String titulo,
	String descricao,
	List<Map<String, Object>> perguntas,
	Boolean ativa,
	LocalDateTime dataInicio,
	LocalDateTime dataFim,
	LocalDateTime createdAt,
	Long totalRespostas,
	Long totalUsuarios,
	Double taxaResposta,
	Boolean jaRespondeu
) {
}

