package workwell.WorkWell.dto.dashboard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import workwell.WorkWell.entity.enums.TipoAtividadeBemEstar;

public record AtividadeBemEstarCreateRequest(
	@NotNull(message = "O tipo da atividade é obrigatório")
	TipoAtividadeBemEstar tipo,
	@NotBlank(message = "O título é obrigatório")
	String titulo,
	String descricao,
	@NotNull(message = "A data/hora de início é obrigatória")
	LocalDateTime dataHoraInicio,
	LocalDateTime dataHoraFim,
	String local
) {
}

