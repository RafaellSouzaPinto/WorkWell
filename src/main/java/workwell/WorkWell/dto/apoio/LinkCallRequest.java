package workwell.WorkWell.dto.apoio;

import jakarta.validation.constraints.Size;

public record LinkCallRequest(
	@Size(max = 500, message = "Link deve ter no máximo 500 caracteres")
	String linkCall
) {
}

