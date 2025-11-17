package workwell.WorkWell.dto.ai;

import java.util.List;

public record SugestaoAtividadeResponse(
	List<String> atividadesSugeridas,
	String justificativa,
	Integer prioridade
) {
}

