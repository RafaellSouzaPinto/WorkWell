package workwell.WorkWell.dto.apoio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CancelamentoRequest(
	@NotBlank(message = "Justificativa é obrigatória")
	@Size(min = 10, max = 255, message = "Justificativa deve ter no mínimo 10 caracteres")
	String justificativa
) {
}

