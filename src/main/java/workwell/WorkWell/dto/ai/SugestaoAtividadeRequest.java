package workwell.WorkWell.dto.ai;

import java.util.List;

public record SugestaoAtividadeRequest(
	String perfilFuncionario,
	Integer nivelHumor,
	String setor,
	List<String> atividadesAnteriores
) {
}

