package workwell.WorkWell.dto.denuncia;

import java.time.LocalDateTime;
import java.util.UUID;

public record DenunciaEticaResponse(
	UUID id,
	UUID empresaId,
	UUID denuncianteId,
	String denuncianteNome,
	String denuncianteEmail,
	String tipoDenuncia,
	String descricao,
	String envolvidos,
	String localOcorrencia,
	LocalDateTime dataOcorrencia,
	String anexos,
	String status,
	String observacoesAdmin,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
}

