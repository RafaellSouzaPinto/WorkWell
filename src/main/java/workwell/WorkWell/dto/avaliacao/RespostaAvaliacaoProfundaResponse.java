package workwell.WorkWell.dto.avaliacao;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record RespostaAvaliacaoProfundaResponse(
	UUID id,
	UUID avaliacaoId,
	UUID usuarioId,
	Map<String, Object> respostas,
	String observacoes,
	LocalDateTime createdAt
) {
}

