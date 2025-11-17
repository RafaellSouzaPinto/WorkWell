package workwell.WorkWell.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import workwell.WorkWell.dto.denuncia.DenunciaEticaCreateRequest;
import workwell.WorkWell.dto.denuncia.DenunciaEticaResponse;
import workwell.WorkWell.dto.denuncia.DenunciaEticaUpdateStatusRequest;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.service.DenunciaEticaService;

@RestController
@RequestMapping("/api/denuncias-eticas")
public class DenunciaEticaController {

	private final DenunciaEticaService denunciaEticaService;

	public DenunciaEticaController(DenunciaEticaService denunciaEticaService) {
		this.denunciaEticaService = denunciaEticaService;
	}

	@PostMapping
	public ResponseEntity<DenunciaEticaResponse> criarDenuncia(
		@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody DenunciaEticaCreateRequest request
	) {
		DenunciaEticaResponse denuncia = denunciaEticaService.criarDenuncia(usuario, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(denuncia);
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public List<DenunciaEticaResponse> listarDenuncias(@AuthenticationPrincipal Usuario usuario) {
		return denunciaEticaService.listarDenuncias(usuario);
	}

	@GetMapping("/status/{status}")
	@PreAuthorize("hasRole('ADMIN')")
	public List<DenunciaEticaResponse> listarDenunciasPorStatus(
		@AuthenticationPrincipal Usuario usuario,
		@PathVariable String status
	) {
		return denunciaEticaService.listarDenunciasPorStatus(usuario, status);
	}

	@PutMapping("/{denunciaId}/status")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<DenunciaEticaResponse> atualizarStatus(
		@AuthenticationPrincipal Usuario usuario,
		@PathVariable UUID denunciaId,
		@Valid @RequestBody DenunciaEticaUpdateStatusRequest request
	) {
		DenunciaEticaResponse denuncia = denunciaEticaService.atualizarStatus(usuario, denunciaId, request);
		return ResponseEntity.ok(denuncia);
	}

	@GetMapping("/pendentes/contagem")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Long> contarDenunciasPendentes(@AuthenticationPrincipal Usuario usuario) {
		Long contagem = denunciaEticaService.contarDenunciasPendentes(usuario);
		return ResponseEntity.ok(contagem);
	}
}

