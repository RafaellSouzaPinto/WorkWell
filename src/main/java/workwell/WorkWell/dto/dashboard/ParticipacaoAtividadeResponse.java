package workwell.WorkWell.dto.dashboard;

import java.time.LocalDateTime;
import java.util.UUID;

public record ParticipacaoAtividadeResponse(
	UUID id,
	UUID atividadeId,
	UUID usuarioId,
	LocalDateTime createdAt
) {
}

