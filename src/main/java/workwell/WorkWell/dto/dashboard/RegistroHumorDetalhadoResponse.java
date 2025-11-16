package workwell.WorkWell.dto.dashboard;

import java.time.LocalDateTime;
import java.util.UUID;

public record RegistroHumorDetalhadoResponse(
	UUID id,
	UUID usuarioId,
	String nomeUsuario,
	Integer nivelHumor,
	String setor,
	String observacoes,
	LocalDateTime createdAt
) {
}

