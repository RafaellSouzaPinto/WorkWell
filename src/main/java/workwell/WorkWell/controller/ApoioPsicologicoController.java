package workwell.WorkWell.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import workwell.WorkWell.dto.PageResponse;
import workwell.WorkWell.dto.apoio.CancelamentoRequest;
import workwell.WorkWell.dto.apoio.ConsultaCreateRequest;
import workwell.WorkWell.dto.apoio.ConsultaResponse;
import workwell.WorkWell.dto.apoio.LinkCallRequest;
import workwell.WorkWell.dto.usuario.UsuarioResponse;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.service.ApoioPsicologicoService;

@RestController
@RequestMapping("/api/apoio")
@PreAuthorize("hasAnyRole('FUNCIONARIO','PSICOLOGO','RH')")
public class ApoioPsicologicoController {

	private final ApoioPsicologicoService service;

	public ApoioPsicologicoController(ApoioPsicologicoService service) {
		this.service = service;
	}

	@GetMapping("/psicologos")
	public List<UsuarioResponse> listarPsicologos(@AuthenticationPrincipal Usuario usuario) {
		return service.listarPsicologos(usuario);
	}

	@GetMapping("/funcionarios")
	@PreAuthorize("hasAnyRole('PSICOLOGO','RH')")
	public List<UsuarioResponse> listarFuncionarios(@AuthenticationPrincipal Usuario usuario) {
		return service.listarFuncionarios(usuario);
	}

	@PostMapping("/consultas")
	public ConsultaResponse agendar(@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody ConsultaCreateRequest request) {
		return service.agendarConsulta(usuario, request);
	}

	@GetMapping("/consultas/pendentes")
	public List<ConsultaResponse> pendentes(@AuthenticationPrincipal Usuario usuario) {
		return service.listarPendentes(usuario);
	}

	@GetMapping("/consultas/pendentes/paginado")
	public PageResponse<ConsultaResponse> pendentesPaginado(
		@AuthenticationPrincipal Usuario usuario,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		return service.listarPendentes(usuario, page, size);
	}

	@GetMapping("/consultas/proximas")
	public List<ConsultaResponse> proximas(@AuthenticationPrincipal Usuario usuario) {
		return service.listarProximas(usuario);
	}

	@GetMapping("/consultas/proximas/paginado")
	public PageResponse<ConsultaResponse> proximasPaginado(
		@AuthenticationPrincipal Usuario usuario,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		return service.listarProximas(usuario, page, size);
	}

	@GetMapping("/consultas/historico")
	public List<ConsultaResponse> historico(@AuthenticationPrincipal Usuario usuario) {
		return service.listarHistorico(usuario);
	}

	@GetMapping("/consultas/historico/paginado")
	public PageResponse<ConsultaResponse> historicoPaginado(
		@AuthenticationPrincipal Usuario usuario,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		return service.listarHistorico(usuario, page, size);
	}

	@GetMapping("/consultas/meus-agendamentos")
	@PreAuthorize("hasAnyRole('FUNCIONARIO','RH')")
	public List<ConsultaResponse> meusAgendamentos(@AuthenticationPrincipal Usuario usuario) {
		return service.listarMeusAgendamentos(usuario);
	}

	@GetMapping("/consultas/meus-agendamentos/paginado")
	@PreAuthorize("hasAnyRole('FUNCIONARIO','RH')")
	public PageResponse<ConsultaResponse> meusAgendamentosPaginado(
		@AuthenticationPrincipal Usuario usuario,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		return service.listarMeusAgendamentos(usuario, page, size);
	}

	@PatchMapping("/consultas/{id}/confirmar")
	public ConsultaResponse confirmar(@AuthenticationPrincipal Usuario usuario, @PathVariable UUID id) {
		return service.confirmarConsulta(usuario, id);
	}

	@PatchMapping("/consultas/{id}/cancelar")
	public ConsultaResponse cancelar(@AuthenticationPrincipal Usuario usuario, @PathVariable UUID id,
		@Valid @RequestBody CancelamentoRequest request) {
		return service.cancelarConsulta(usuario, id, request);
	}

	@PatchMapping("/consultas/{id}/concluir")
	@PreAuthorize("hasRole('PSICOLOGO')")
	public ConsultaResponse concluir(@AuthenticationPrincipal Usuario usuario, @PathVariable UUID id) {
		return service.concluirConsulta(usuario, id);
	}

	@PatchMapping("/consultas/{id}/link-call")
	@PreAuthorize("hasRole('PSICOLOGO')")
	public ConsultaResponse atualizarLinkCall(@AuthenticationPrincipal Usuario usuario, @PathVariable UUID id,
		@Valid @RequestBody LinkCallRequest request) {
		return service.atualizarLinkCall(usuario, id, request);
	}
}

