package workwell.WorkWell.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import workwell.WorkWell.dto.ai.AnaliseSentimentoRequest;
import workwell.WorkWell.dto.ai.AnaliseSentimentoResponse;
import workwell.WorkWell.dto.ai.ChatRequest;
import workwell.WorkWell.dto.ai.ChatResponse;
import workwell.WorkWell.dto.ai.InsightRhRequest;
import workwell.WorkWell.dto.ai.InsightRhResponse;
import workwell.WorkWell.dto.ai.SugestaoAtividadeRequest;
import workwell.WorkWell.dto.ai.SugestaoAtividadeResponse;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.service.AIService;

@RestController
@RequestMapping("/api/ai")
@PreAuthorize("hasAnyRole('FUNCIONARIO','PSICOLOGO','RH')")
public class AIController {

	private final AIService aiService;

	public AIController(AIService aiService) {
		this.aiService = aiService;
	}

	@PostMapping("/chat")
	public ResponseEntity<ChatResponse> chat(
		@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody ChatRequest request) {
		ChatResponse response = aiService.chat(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/analise-sentimento")
	public ResponseEntity<AnaliseSentimentoResponse> analisarSentimento(
		@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody AnaliseSentimentoRequest request) {
		AnaliseSentimentoResponse response = aiService.analisarSentimento(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/insights-rh")
	@PreAuthorize("hasRole('RH')")
	public ResponseEntity<InsightRhResponse> gerarInsightsRh(
		@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody InsightRhRequest request) {
		InsightRhResponse response = aiService.gerarInsightsRh(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/sugerir-atividades")
	public ResponseEntity<SugestaoAtividadeResponse> sugerirAtividades(
		@AuthenticationPrincipal Usuario usuario,
		@Valid @RequestBody SugestaoAtividadeRequest request) {
		SugestaoAtividadeResponse response = aiService.sugerirAtividades(request);
		return ResponseEntity.ok(response);
	}
}

