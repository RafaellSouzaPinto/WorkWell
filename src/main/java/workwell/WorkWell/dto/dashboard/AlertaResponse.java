package workwell.WorkWell.dto.dashboard;

public record AlertaResponse(
	String tipo,
	String mensagem,
	String severidade
) {
}

