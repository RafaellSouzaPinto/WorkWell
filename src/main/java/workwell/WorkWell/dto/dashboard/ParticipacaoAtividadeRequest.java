package workwell.WorkWell.dto.dashboard;

import java.util.UUID;

public record ParticipacaoAtividadeRequest(
	UUID atividadeId,
	Boolean vaiParticipar
) {
}

