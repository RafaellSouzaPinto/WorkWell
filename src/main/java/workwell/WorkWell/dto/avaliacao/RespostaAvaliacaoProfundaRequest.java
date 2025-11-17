package workwell.WorkWell.dto.avaliacao;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record RespostaAvaliacaoProfundaRequest(
	@NotNull(message = "O ID da avaliação é obrigatório")
	java.util.UUID avaliacaoId,
	
	@NotNull(message = "As respostas são obrigatórias")
	Map<String, Object> respostas, // Map: {"perguntaId1": "resposta1", "perguntaId2": "resposta2"}
	
	String observacoes
) {
}

