package workwell.WorkWell.dto.denuncia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DenunciaEticaUpdateStatusRequest(
	@NotBlank(message = "Status é obrigatório")
	String status,

	@Size(max = 2000, message = "Observações deve ter no máximo 2000 caracteres")
	String observacoesAdmin
) {
}

