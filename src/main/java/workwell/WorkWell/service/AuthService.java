package workwell.WorkWell.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import workwell.WorkWell.dto.auth.AuthRequest;
import workwell.WorkWell.dto.auth.AuthResponse;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.security.JwtService;

@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	public AuthResponse autenticar(AuthRequest request) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.email(), request.senha())
		);

		Usuario usuario = (Usuario) authentication.getPrincipal();
		JwtService.TokenPayload tokenPayload = jwtService.generateToken(usuario);

		return new AuthResponse(
			tokenPayload.token(),
			"Bearer",
			tokenPayload.expiresAt(),
			usuario.getId(),
			usuario.getRole(),
			usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null
		);
	}
}

