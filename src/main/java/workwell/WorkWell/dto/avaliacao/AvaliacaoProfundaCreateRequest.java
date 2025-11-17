package workwell.WorkWell.dto.avaliacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record AvaliacaoProfundaCreateRequest(
	@NotBlank(message = "O título é obrigatório")
	@Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
	String titulo,
	
	@Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
	String descricao,
	
	@NotNull(message = "As perguntas são obrigatórias")
	List<Map<String, Object>> perguntas, // Lista de perguntas: [{"id": 1, "pergunta": "...", "tipo": "TEXTO|NUMERICO|ESCOLHA"}]
	
	@NotNull(message = "A data de início é obrigatória")
	LocalDateTime dataInicio,
	
	@NotNull(message = "A data de fim é obrigatória")
	LocalDateTime dataFim
) {
}

