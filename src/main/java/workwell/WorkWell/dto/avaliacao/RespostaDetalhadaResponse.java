package workwell.WorkWell.dto.avaliacao;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record RespostaDetalhadaResponse(
	UUID id,
	UUID usuarioId,
	String nomeUsuario,
	String emailUsuario,
	Map<String, Object> respostas,
	String observacoes,
	LocalDateTime createdAt
) {
}

