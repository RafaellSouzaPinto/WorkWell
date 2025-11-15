package workwell.WorkWell.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import workwell.WorkWell.dto.usuario.UsuarioCadastroRequest;
import workwell.WorkWell.dto.usuario.UsuarioResponse;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	@PostMapping
	@PreAuthorize("permitAll()")
	public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody UsuarioCadastroRequest request) {
		UsuarioResponse response = usuarioService.cadastrar(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public UsuarioResponse usuarioAtual(@AuthenticationPrincipal Usuario usuario) {
		return usuarioService.dadosAtuais(usuario);
	}
}

