package workwell.WorkWell.dto.ai;

import java.util.List;

public record AnaliseSentimentoResponse(
	String sentimento,
	Double score,
	String resumo,
	List<String> pontosChave,
	List<String> recomendacoes
) {
}

