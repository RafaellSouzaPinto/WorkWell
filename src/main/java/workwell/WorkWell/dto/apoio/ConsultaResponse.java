package workwell.WorkWell.dto.apoio;

import java.time.LocalDateTime;
import java.util.UUID;
import workwell.WorkWell.entity.enums.ConsultaStatus;
import workwell.WorkWell.entity.enums.LocalAtendimento;
import workwell.WorkWell.entity.enums.SalaAtendimento;

public record ConsultaResponse(
	UUID id,
	ConsultaStatus status,
	LocalDateTime dataHoraInicio,
	LocalDateTime dataHoraFim,
	LocalAtendimento local,
	SalaAtendimento sala,
	String observacoes,
	String justificativaCancelamento,
	UUID funcionarioId,
	String funcionarioNome,
	UUID psicologoId,
	String psicologoNome,
	boolean aguardandoMinhaConfirmacao,
	String linkCall
) {
}

