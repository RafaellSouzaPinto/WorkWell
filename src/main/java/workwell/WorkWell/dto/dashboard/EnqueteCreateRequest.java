package workwell.WorkWell.dto.dashboard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public record EnqueteCreateRequest(
	@NotBlank(message = "A pergunta é obrigatória")
	@Size(max = 255, message = "A pergunta deve ter no máximo 255 caracteres")
	String pergunta,
	List<String> opcoesResposta, // Lista de opções de resposta
	LocalDateTime dataFim
) {
}

