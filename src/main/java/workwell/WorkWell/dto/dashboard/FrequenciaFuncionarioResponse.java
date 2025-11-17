package workwell.WorkWell.dto.dashboard;

public record FrequenciaFuncionarioResponse(
	Long totalAtividades,
	Long atividadesParticipadas,
	Double percentualFrequencia,
	String mensagemMotivacional,
	String corBarra
) {
}

