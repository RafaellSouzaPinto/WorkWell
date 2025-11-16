package workwell.WorkWell.dto.dashboard;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RegistroHumorRequest(
	@NotNull(message = "O nível de humor é obrigatório")
	@Min(value = 1, message = "O nível de humor deve ser entre 1 e 5")
	@Max(value = 5, message = "O nível de humor deve ser entre 1 e 5")
	Integer nivelHumor,
	String setor,
	String observacoes
) {
}

