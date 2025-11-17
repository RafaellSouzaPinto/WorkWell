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
import workwell.WorkWell.dto.avaliacao.AvaliacaoProfundaCreateRequest;
import workwell.WorkWell.dto.avaliacao.AvaliacaoProfundaResponse;
import workwell.WorkWell.dto.avaliacao.RelatorioAvaliacaoProfundaResponse;
import workwell.WorkWell.dto.avaliacao.RespostaAvaliacaoProfundaRequest;
import workwell.WorkWell.dto.avaliacao.RespostaAvaliacaoProfundaResponse;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.service.AvaliacaoProfundaService;

@RestController
@RequestMapping("/api/avaliacoes-profundas")
public class AvaliacaoProfundaController {

	private final AvaliacaoProfundaService avaliacaoProfundaService;

	public AvaliacaoProfundaController(AvaliacaoProfundaService avaliacaoProfundaService) {
		this.avaliacaoProfundaService = avaliacaoProfundaService;
	}

	@PostMapping
	@PreAuthorize("hasRole('PSICOLOGO')")
	public ResponseEntity<AvaliacaoProfundaResponse> criarAvaliacao(@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody AvaliacaoProfundaCreateRequest request) {
		AvaliacaoProfundaResponse avaliacao = avaliacaoProfundaService.criarAvaliacao(usuario, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(avaliacao);
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('PSICOLOGO', 'FUNCIONARIO')")
	public List<AvaliacaoProfundaResponse> listarAvaliacoesAtivas(@AuthenticationPrincipal Usuario usuario) {
		UUID empresaId = usuario.getEmpresa().getId();
		return avaliacaoProfundaService.listarAvaliacoesAtivas(empresaId, usuario.getId());
	}

	@GetMapping("/minhas")
	@PreAuthorize("hasRole('PSICOLOGO')")
	public List<AvaliacaoProfundaResponse> listarMinhasAvaliacoes(@AuthenticationPrincipal Usuario usuario) {
		return avaliacaoProfundaService.listarAvaliacoesPorPsicologo(usuario);
	}

	@PostMapping("/responder")
	@PreAuthorize("hasRole('FUNCIONARIO')")
	public ResponseEntity<RespostaAvaliacaoProfundaResponse> responderAvaliacao(@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody RespostaAvaliacaoProfundaRequest request) {
		RespostaAvaliacaoProfundaResponse resposta = avaliacaoProfundaService.responderAvaliacao(usuario, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
	}

	@GetMapping("/{avaliacaoId}/relatorio")
	@PreAuthorize("hasRole('PSICOLOGO')")
	public RelatorioAvaliacaoProfundaResponse obterRelatorio(@AuthenticationPrincipal Usuario usuario,
		@PathVariable UUID avaliacaoId) {
		return avaliacaoProfundaService.obterRelatorio(avaliacaoId, usuario);
	}
}

