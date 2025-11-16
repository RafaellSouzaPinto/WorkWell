package workwell.WorkWell.dto.dashboard;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegistroHumorResponse(
	UUID id,
	UUID empresaId,
	UUID usuarioId,
	Integer nivelHumor,
	String setor,
	String observacoes,
	LocalDateTime createdAt
) {
}

