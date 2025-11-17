package workwell.WorkWell.dto.dashboard;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificacaoResponse(
	UUID id,
	String tipo,
	String titulo,
	String mensagem,
	LocalDateTime dataHora,
	Long minutosRestantes,
	String cor
) {
}

