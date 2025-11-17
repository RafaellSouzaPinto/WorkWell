package workwell.WorkWell.dto.dashboard;

import java.util.List;

public record HistoricoParticipacaoResponse(
	List<ParticipacaoHistoricoResponse> participacoes,
	Long totalParticipacoes
) {
}

