package workwell.WorkWell.dto.dashboard;

import java.util.List;

public record DashboardRhResponse(
	Double nivelMedioHumor,
	List<SetorEstresseResponse> setoresComEstresse,
	Long frequenciaConsultas,
	Double aderenciaAtividades,
	List<EnqueteResponse> enquetesAtivas,
	List<AlertaResponse> alertas
) {
}

