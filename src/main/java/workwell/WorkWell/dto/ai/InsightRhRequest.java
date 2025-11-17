package workwell.WorkWell.dto.ai;

import java.util.List;
import java.util.Map;

public record InsightRhRequest(
	Double nivelMedioHumor,
	List<SetorEstresseData> setoresComEstresse,
	Long frequenciaConsultas,
	Double aderenciaAtividades,
	Map<String, Object> dadosAdicionais
) {
	public record SetorEstresseData(
		String setor,
		Double nivelEstresse
	) {
	}
}

