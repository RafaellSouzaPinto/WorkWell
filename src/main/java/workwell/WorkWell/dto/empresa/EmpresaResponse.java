package workwell.WorkWell.dto.empresa;

import java.util.UUID;

public record EmpresaResponse(
	UUID id,
	String nome,
	String cnpj,
	String descricao,
	String token,
	String adminEmail
) {
}

