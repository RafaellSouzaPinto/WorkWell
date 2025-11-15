package workwell.WorkWell.dto.empresa;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmpresaCadastroRequest(
	@NotBlank(message = "Nome da empresa é obrigatório")
	@Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
	String nome,

	@NotBlank(message = "CNPJ é obrigatório")
	@Pattern(regexp = "\\d{14}", message = "CNPJ deve conter 14 dígitos numéricos")
	String cnpj,

	@Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
	String descricao,

	@NotBlank(message = "Email corporativo é obrigatório")
	@Email(message = "Email inválido")
	String email,

	@NotBlank(message = "Senha é obrigatória")
	@Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
	String senha
) {
}

