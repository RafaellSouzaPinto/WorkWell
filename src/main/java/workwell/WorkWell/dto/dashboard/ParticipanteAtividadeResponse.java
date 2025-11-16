package workwell.WorkWell.dto.dashboard;

import java.time.LocalDateTime;
import java.util.UUID;

public record ParticipanteAtividadeResponse(
	UUID id,
	UUID usuarioId,
	String nomeUsuario,
	String emailUsuario,
	LocalDateTime dataParticipacao
) {
}

