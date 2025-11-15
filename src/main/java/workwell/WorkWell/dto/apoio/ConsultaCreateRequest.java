package workwell.WorkWell.dto.apoio;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import workwell.WorkWell.entity.enums.LocalAtendimento;
import workwell.WorkWell.entity.enums.SalaAtendimento;

public record ConsultaCreateRequest(
	@NotNull(message = "Psicólogo é obrigatório")
	UUID psicologoId,

	UUID funcionarioId,

	@NotNull(message = "Data é obrigatória")
	@FutureOrPresent(message = "Data deve estar no presente ou futuro")
	LocalDate data,

	@NotNull(message = "Horário de início é obrigatório")
	LocalTime horaInicio,

	@NotNull(message = "Duração é obrigatória")
	@Min(value = 30, message = "Duração mínima é de 30 minutos")
	Integer duracaoMinutos,

	@NotNull(message = "Local é obrigatório")
	LocalAtendimento local,

	SalaAtendimento sala,

	@Size(max = 255, message = "Observações deve ter no máximo 255 caracteres")
	String observacoes
) {
}

