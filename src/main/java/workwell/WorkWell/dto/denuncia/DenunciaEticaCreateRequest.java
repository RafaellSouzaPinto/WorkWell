package workwell.WorkWell.dto.denuncia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record DenunciaEticaCreateRequest(
	@NotBlank(message = "Tipo de denúncia é obrigatório")
	@Size(max = 100, message = "Tipo de denúncia deve ter no máximo 100 caracteres")
	String tipoDenuncia,

	@NotBlank(message = "Descrição é obrigatória")
	@Size(max = 4000, message = "Descrição deve ter no máximo 4000 caracteres")
	String descricao,

	@Size(max = 500, message = "Envolvidos deve ter no máximo 500 caracteres")
	String envolvidos,

	@Size(max = 255, message = "Local da ocorrência deve ter no máximo 255 caracteres")
	String localOcorrencia,

	LocalDateTime dataOcorrencia,

	@Size(max = 2000, message = "Anexos deve ter no máximo 2000 caracteres")
	String anexos
) {
}

