package workwell.WorkWell.dto.dashboard;

import java.time.LocalDateTime;
import java.util.UUID;

public record RespostaEnqueteResponse(
	UUID id,
	UUID enqueteId,
	UUID usuarioId,
	String resposta,
	LocalDateTime createdAt
) {
}

