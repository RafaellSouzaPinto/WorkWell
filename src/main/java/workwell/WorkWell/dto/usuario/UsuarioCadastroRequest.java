package workwell.WorkWell.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import workwell.WorkWell.entity.enums.RoleType;

public record UsuarioCadastroRequest(
	@NotBlank(message = "Nome é obrigatório")
	@Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
	String nome,

	@NotBlank(message = "Email é obrigatório")
	@Email(message = "Email inválido")
	String email,

	@NotBlank(message = "Senha é obrigatória")
	@Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
	String senha,

	@NotBlank(message = "Token da empresa é obrigatório")
	String tokenEmpresa,

	@NotNull(message = "Tipo de usuário é obrigatório")
	RoleType tipoUsuario,

	@Size(max = 20, message = "CRP deve ter no máximo 20 caracteres")
	String crp
) {
}

