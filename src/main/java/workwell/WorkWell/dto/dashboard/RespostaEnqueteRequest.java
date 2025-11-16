package workwell.WorkWell.dto.dashboard;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record RespostaEnqueteRequest(
	UUID enqueteId,
	@NotBlank(message = "A resposta é obrigatória")
	String resposta
) {
}

