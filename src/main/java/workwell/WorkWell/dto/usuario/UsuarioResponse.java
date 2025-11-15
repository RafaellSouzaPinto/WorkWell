package workwell.WorkWell.dto.usuario;

import java.util.UUID;
import workwell.WorkWell.entity.enums.RoleType;

public record UsuarioResponse(
	UUID id,
	String nome,
	String email,
	RoleType role,
	UUID empresaId,
	String crp
) {
}

