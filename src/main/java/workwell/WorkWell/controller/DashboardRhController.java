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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import workwell.WorkWell.dto.dashboard.AtividadeBemEstarCreateRequest;
import workwell.WorkWell.dto.dashboard.AtividadeBemEstarResponse;
import workwell.WorkWell.dto.dashboard.DashboardRhResponse;
import workwell.WorkWell.dto.dashboard.EnqueteCreateRequest;
import workwell.WorkWell.dto.dashboard.EnqueteResponse;
import workwell.WorkWell.dto.dashboard.ParticipacaoAtividadeRequest;
import workwell.WorkWell.dto.dashboard.ParticipacaoAtividadeResponse;
import workwell.WorkWell.dto.dashboard.ParticipanteAtividadeResponse;
import workwell.WorkWell.dto.dashboard.RegistroHumorDetalhadoResponse;
import workwell.WorkWell.dto.dashboard.RegistroHumorRequest;
import workwell.WorkWell.dto.dashboard.RegistroHumorResponse;
import workwell.WorkWell.dto.dashboard.RespostaEnqueteRequest;
import workwell.WorkWell.dto.dashboard.RespostaEnqueteResponse;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.service.DashboardRhService;

@RestController
@RequestMapping("/api/dashboard-rh")
@PreAuthorize("hasAnyRole('RH', 'FUNCIONARIO')")
public class DashboardRhController {

	private final DashboardRhService dashboardRhService;

	public DashboardRhController(DashboardRhService dashboardRhService) {
		this.dashboardRhService = dashboardRhService;
	}

	@GetMapping
	@PreAuthorize("hasRole('RH')")
	public DashboardRhResponse obterDashboard(@AuthenticationPrincipal Usuario usuario) {
		return dashboardRhService.obterDashboard(usuario);
	}

	@PostMapping("/humor")
	public ResponseEntity<RegistroHumorResponse> registrarHumor(@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody RegistroHumorRequest request) {
		RegistroHumorResponse registro = dashboardRhService.registrarHumor(usuario, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(registro);
	}

	@PostMapping("/enquetes")
	@PreAuthorize("hasRole('RH')")
	public ResponseEntity<EnqueteResponse> criarEnquete(@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody EnqueteCreateRequest request) {
		EnqueteResponse enquete = dashboardRhService.criarEnquete(usuario, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(enquete);
	}

	@GetMapping("/enquetes")
	public List<EnqueteResponse> listarEnquetes(@AuthenticationPrincipal Usuario usuario) {
		UUID empresaId = usuario.getEmpresa().getId();
		return dashboardRhService.listarEnquetesAtivas(empresaId, usuario.getId());
	}

	@PostMapping("/enquetes/responder")
	public ResponseEntity<RespostaEnqueteResponse> responderEnquete(@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody RespostaEnqueteRequest request) {
		RespostaEnqueteResponse resposta = dashboardRhService.responderEnquete(usuario, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
	}

	@PostMapping("/atividades")
	@PreAuthorize("hasRole('RH')")
	public ResponseEntity<AtividadeBemEstarResponse> criarAtividade(@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody AtividadeBemEstarCreateRequest request) {
		AtividadeBemEstarResponse atividade = dashboardRhService.criarAtividade(usuario, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(atividade);
	}

	@GetMapping("/atividades")
	public List<AtividadeBemEstarResponse> listarAtividades(@AuthenticationPrincipal Usuario usuario) {
		UUID empresaId = usuario.getEmpresa().getId();
		return dashboardRhService.listarAtividades(empresaId, usuario.getId());
	}

	@PostMapping("/atividades/participar")
	public ResponseEntity<ParticipacaoAtividadeResponse> participarAtividade(@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody ParticipacaoAtividadeRequest request) {
		ParticipacaoAtividadeResponse participacao = dashboardRhService.participarAtividade(usuario, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(participacao);
	}

	@GetMapping("/humor/registros")
	@PreAuthorize("hasRole('RH')")
	public List<RegistroHumorDetalhadoResponse> listarRegistrosHumor(@AuthenticationPrincipal Usuario usuario) {
		UUID empresaId = usuario.getEmpresa().getId();
		return dashboardRhService.listarRegistrosHumor(empresaId);
	}

	@GetMapping("/atividades/{atividadeId}/participantes")
	@PreAuthorize("hasRole('RH')")
	public List<ParticipanteAtividadeResponse> listarParticipantesAtividade(
		@AuthenticationPrincipal Usuario usuario,
		@PathVariable UUID atividadeId) {
		UUID empresaId = usuario.getEmpresa().getId();
		return dashboardRhService.listarParticipantesAtividade(atividadeId, empresaId);
	}
}

