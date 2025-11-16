package workwell.WorkWell.dto.dashboard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record EnqueteCreateRequest(
	@NotBlank(message = "A pergunta é obrigatória")
	@Size(max = 255, message = "A pergunta deve ter no máximo 255 caracteres")
	String pergunta,
	LocalDateTime dataFim
) {
}

