package workwell.WorkWell.dto.auth;

import java.time.Instant;
import java.util.UUID;
import workwell.WorkWell.entity.enums.RoleType;

public record AuthResponse(
	String accessToken,
	String tokenType,
	Instant expiresAt,
	UUID usuarioId,
	RoleType role,
	UUID empresaId
) {
}

