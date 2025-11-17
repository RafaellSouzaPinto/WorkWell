package workwell.WorkWell.dto.avaliacao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record RelatorioAvaliacaoProfundaResponse(
	UUID avaliacaoId,
	String titulo,
	String descricao,
	LocalDateTime dataInicio,
	LocalDateTime dataFim,
	Long totalRespostas,
	Long totalUsuarios,
	Double taxaResposta,
	List<RespostaDetalhadaResponse> respostasDetalhadas,
	Map<String, Object> analiseEstatistica
) {
}

