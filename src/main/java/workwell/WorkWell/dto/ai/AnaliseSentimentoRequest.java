package workwell.WorkWell.dto.ai;

public record AnaliseSentimentoRequest(
	String texto,
	String setor,
	Integer nivelHumor
) {
}

