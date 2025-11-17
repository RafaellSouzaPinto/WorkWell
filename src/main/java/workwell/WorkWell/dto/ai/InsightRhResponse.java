package workwell.WorkWell.dto.ai;

import java.util.List;

public record InsightRhResponse(
	String resumoExecutivo,
	List<String> pontosCriticos,
	List<String> pontosPositivos,
	List<String> recomendacoes,
	String tendencia
) {
}

