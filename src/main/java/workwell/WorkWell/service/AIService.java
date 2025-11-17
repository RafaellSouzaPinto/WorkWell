package workwell.WorkWell.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import workwell.WorkWell.dto.ai.AnaliseSentimentoRequest;
import workwell.WorkWell.dto.ai.AnaliseSentimentoResponse;
import workwell.WorkWell.dto.ai.ChatRequest;
import workwell.WorkWell.dto.ai.InsightRhRequest;
import workwell.WorkWell.dto.ai.InsightRhResponse;
import workwell.WorkWell.dto.ai.SugestaoAtividadeRequest;
import workwell.WorkWell.dto.ai.SugestaoAtividadeResponse;

@Service
public class AIService {

	private final ChatModel chatModel;
	private final ObjectMapper objectMapper;
	private final MessageSource messageSource;

	public AIService(ChatModel chatModel, ObjectMapper objectMapper, MessageSource messageSource) {
		this.chatModel = chatModel;
		this.objectMapper = objectMapper;
		this.messageSource = messageSource;
	}

	public workwell.WorkWell.dto.ai.ChatResponse chat(ChatRequest request, Locale locale) {
		String promptText = buildChatPrompt(request.mensagem(), request.contexto(), locale);
		String resposta = callChatModel(promptText);
		return new workwell.WorkWell.dto.ai.ChatResponse(resposta, request.contexto());
	}

	public AnaliseSentimentoResponse analisarSentimento(AnaliseSentimentoRequest request, Locale locale) {
		String prompt = buildAnaliseSentimentoPrompt(request, locale);
		String resposta = callChatModel(prompt);

		try {
			return parseAnaliseSentimento(resposta);
		} catch (Exception e) {
			// Fallback: retornar análise básica
			return criarAnaliseBasica(request, resposta);
		}
	}

	@Cacheable(value = "insightsAI", key = "#request.nivelMedioHumor + '_' + #request.frequenciaConsultas + '_' + #request.aderenciaAtividades + '_' + #locale")
	public InsightRhResponse gerarInsightsRh(InsightRhRequest request, Locale locale) {
		String prompt = buildInsightRhPrompt(request, locale);
		String resposta = callChatModel(prompt);

		try {
			return parseInsightRh(resposta);
		} catch (Exception e) {
			// Fallback: retornar insights básicos
			return criarInsightBasico(request, resposta);
		}
	}

	public SugestaoAtividadeResponse sugerirAtividades(SugestaoAtividadeRequest request, Locale locale) {
		String prompt = buildSugestaoAtividadePrompt(request, locale);
		String resposta = callChatModel(prompt);

		try {
			return parseSugestaoAtividade(resposta);
		} catch (Exception e) {
			// Fallback: retornar sugestões básicas
			return criarSugestaoBasica(request, resposta);
		}
	}

	private String buildChatPrompt(String mensagem, String contexto, Locale locale) {
		StringBuilder prompt = new StringBuilder();
		prompt.append(messageSource.getMessage("ai.assistant.role", null, locale)).append(" ");
		prompt.append(messageSource.getMessage("ai.assistant.help", null, locale)).append("\n\n");

		if (contexto != null && !contexto.isEmpty()) {
			prompt.append(messageSource.getMessage("ai.assistant.context", null, locale)).append(" ").append(contexto).append("\n\n");
		}

		prompt.append(messageSource.getMessage("ai.assistant.question", null, locale)).append(" ").append(mensagem).append("\n\n");
		prompt.append(messageSource.getMessage("ai.assistant.response", null, locale));

		return prompt.toString();
	}

	private String buildAnaliseSentimentoPrompt(AnaliseSentimentoRequest request, Locale locale) {
		StringBuilder prompt = new StringBuilder();
		prompt.append(messageSource.getMessage("ai.analysis.sentiment", null, locale)).append("\n\n");
		prompt.append(messageSource.getMessage("ai.analysis.text", null, locale)).append(" ").append(request.texto() != null ? request.texto() : messageSource.getMessage("ai.analysis.not.informed", null, locale)).append("\n");
		prompt.append(messageSource.getMessage("ai.analysis.mood.level", null, locale)).append(" ").append(request.nivelHumor() != null ? request.nivelHumor() : messageSource.getMessage("ai.analysis.not.informed", null, locale)).append("\n");
		prompt.append(messageSource.getMessage("ai.analysis.sector", null, locale)).append(" ").append(request.setor() != null ? request.setor() : messageSource.getMessage("ai.analysis.not.informed", null, locale)).append("\n\n");

		prompt.append(messageSource.getMessage("ai.analysis.format", null, locale)).append("\n");
		prompt.append("{\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.analysis.sentiment.type", null, locale)).append("\": \"positivo/neutro/negativo\",\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.analysis.score", null, locale)).append("\": 0.0 a 1.0,\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.analysis.summary", null, locale)).append("\": \"resumo da análise\",\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.analysis.key.points", null, locale)).append("\": [\"ponto1\", \"ponto2\"],\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.analysis.recommendations", null, locale)).append("\": [\"recomendação1\", \"recomendação2\"]\n");
		prompt.append("}\n\n");
		prompt.append(messageSource.getMessage("ai.analysis.specific", null, locale));

		return prompt.toString();
	}

	private String buildInsightRhPrompt(InsightRhRequest request, Locale locale) {
		StringBuilder prompt = new StringBuilder();
		prompt.append(messageSource.getMessage("ai.insights.role", null, locale)).append("\n\n");
		prompt.append(messageSource.getMessage("ai.insights.avg.mood", null, locale)).append(" ").append(request.nivelMedioHumor() != null ? request.nivelMedioHumor() : "N/A").append("\n");
		prompt.append(messageSource.getMessage("ai.insights.consultation.frequency", null, locale)).append(" ").append(request.frequenciaConsultas()).append("\n");
		prompt.append(messageSource.getMessage("ai.insights.activity.adherence", null, locale)).append(" ").append(request.aderenciaAtividades() != null ? request.aderenciaAtividades() + "%" : "N/A").append("\n\n");

		if (request.setoresComEstresse() != null && !request.setoresComEstresse().isEmpty()) {
			prompt.append(messageSource.getMessage("ai.insights.stressed.sectors", null, locale)).append(":\n");
			request.setoresComEstresse().forEach(setor -> {
				prompt.append("- ").append(messageSource.getMessage("ai.insights.sector", null, locale)).append(": ").append(setor.setor()).append(" - ").append(messageSource.getMessage("ai.insights.level", null, locale)).append(" ").append(setor.nivelEstresse()).append("\n");
			});
			prompt.append("\n");
		}

		prompt.append(messageSource.getMessage("ai.insights.format", null, locale)).append("\n");
		prompt.append("{\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.insights.executive.summary", null, locale)).append("\": \"resumo geral\",\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.insights.critical.points", null, locale)).append("\": [\"ponto1\", \"ponto2\"],\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.insights.positive.points", null, locale)).append("\": [\"ponto1\", \"ponto2\"],\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.insights.recommendations", null, locale)).append("\": [\"recomendação1\", \"recomendação2\"],\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.insights.trend", null, locale)).append("\": \"melhorando/estável/preocupante\"\n");
		prompt.append("}\n\n");
		prompt.append(messageSource.getMessage("ai.insights.strategic", null, locale));

		return prompt.toString();
	}

	private String buildSugestaoAtividadePrompt(SugestaoAtividadeRequest request, Locale locale) {
		StringBuilder prompt = new StringBuilder();
		prompt.append(messageSource.getMessage("ai.suggestions.role", null, locale)).append("\n\n");
		prompt.append(messageSource.getMessage("ai.suggestions.employee.profile", null, locale)).append(" ").append(request.perfilFuncionario() != null ? request.perfilFuncionario() : messageSource.getMessage("ai.analysis.not.informed", null, locale)).append("\n");
		prompt.append(messageSource.getMessage("ai.suggestions.current.mood", null, locale)).append(" ").append(request.nivelHumor() != null ? request.nivelHumor() : messageSource.getMessage("ai.analysis.not.informed", null, locale)).append("\n");
		prompt.append(messageSource.getMessage("ai.suggestions.sector", null, locale)).append(" ").append(request.setor() != null ? request.setor() : messageSource.getMessage("ai.analysis.not.informed", null, locale)).append("\n");

		if (request.atividadesAnteriores() != null && !request.atividadesAnteriores().isEmpty()) {
			prompt.append(messageSource.getMessage("ai.suggestions.previous.activities", null, locale)).append(" ").append(String.join(", ", request.atividadesAnteriores())).append("\n");
		}
		prompt.append("\n");

		prompt.append(messageSource.getMessage("ai.suggestions.format", null, locale)).append("\n");
		prompt.append("{\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.suggestions.activities", null, locale)).append("\": [\"atividade1\", \"atividade2\", \"atividade3\"],\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.suggestions.justification", null, locale)).append("\": \"por que essas atividades são adequadas\",\n");
		prompt.append("  \"").append(messageSource.getMessage("ai.suggestions.priority", null, locale)).append("\": 1 a 5 (1 = mais importante)\n");
		prompt.append("}\n\n");
		prompt.append(messageSource.getMessage("ai.suggestions.practical", null, locale));

		return prompt.toString();
	}

	private String callChatModel(String prompt) {
		Prompt chatPrompt = new Prompt(new UserMessage(prompt));
		ChatResponse response = chatModel.call(chatPrompt);
		return response.getResult().getOutput().getText();
	}

	private AnaliseSentimentoResponse parseAnaliseSentimento(String resposta) {
		try {
			// Tentar extrair JSON da resposta
			String json = extrairJson(resposta);
			Map<String, Object> data = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});

			String sentimento = (String) data.getOrDefault("sentimento", "neutro");
			Double score = parseDouble(data.get("score"), 0.5);
			String resumo = (String) data.getOrDefault("resumo", "Análise não disponível");
			List<String> pontosChave = parseList(data.get("pontosChave"));
			List<String> recomendacoes = parseList(data.get("recomendacoes"));

			return new AnaliseSentimentoResponse(sentimento, score, resumo, pontosChave, recomendacoes);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao parsear análise de sentimento", e);
		}
	}

	private InsightRhResponse parseInsightRh(String resposta) {
		try {
			String json = extrairJson(resposta);
			Map<String, Object> data = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});

			String resumoExecutivo = (String) data.getOrDefault("resumoExecutivo", "Insights não disponíveis");
			List<String> pontosCriticos = parseList(data.get("pontosCriticos"));
			List<String> pontosPositivos = parseList(data.get("pontosPositivos"));
			List<String> recomendacoes = parseList(data.get("recomendacoes"));
			String tendencia = (String) data.getOrDefault("tendencia", "estável");

			return new InsightRhResponse(resumoExecutivo, pontosCriticos, pontosPositivos, recomendacoes, tendencia);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao parsear insights RH", e);
		}
	}

	private SugestaoAtividadeResponse parseSugestaoAtividade(String resposta) {
		try {
			String json = extrairJson(resposta);
			Map<String, Object> data = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});

			List<String> atividadesSugeridas = parseList(data.get("atividadesSugeridas"));
			String justificativa = (String) data.getOrDefault("justificativa", "Sugestões baseadas no perfil");
			Integer prioridade = parseInteger(data.get("prioridade"), 3);

			return new SugestaoAtividadeResponse(atividadesSugeridas, justificativa, prioridade);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao parsear sugestões de atividade", e);
		}
	}

	private String extrairJson(String texto) {
		// Tentar encontrar JSON no texto
		int inicio = texto.indexOf("{");
		int fim = texto.lastIndexOf("}") + 1;
		if (inicio >= 0 && fim > inicio) {
			return texto.substring(inicio, fim);
		}
		return texto;
	}

	@SuppressWarnings("unchecked")
	private List<String> parseList(Object obj) {
		if (obj == null) {
			return new ArrayList<>();
		}
		if (obj instanceof List) {
			return (List<String>) obj;
		}
		return new ArrayList<>();
	}

	private Double parseDouble(Object obj, Double defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		if (obj instanceof Number) {
			return ((Number) obj).doubleValue();
		}
		try {
			return Double.parseDouble(obj.toString());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	private Integer parseInteger(Object obj, Integer defaultValue) {
		if (obj == null) {
			return defaultValue;
		}
		if (obj instanceof Number) {
			return ((Number) obj).intValue();
		}
		try {
			return Integer.parseInt(obj.toString());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	private AnaliseSentimentoResponse criarAnaliseBasica(AnaliseSentimentoRequest request, String resposta) {
		String sentimento = request.nivelHumor() != null && request.nivelHumor() >= 7 ? "positivo" :
			request.nivelHumor() != null && request.nivelHumor() <= 4 ? "negativo" : "neutro";
		Double score = request.nivelHumor() != null ? request.nivelHumor() / 10.0 : 0.5;

		return new AnaliseSentimentoResponse(
			sentimento,
			score,
			resposta.length() > 200 ? resposta.substring(0, 200) + "..." : resposta,
			Arrays.asList("Análise baseada no nível de humor informado"),
			Arrays.asList("Considere atividades de bem-estar", "Mantenha comunicação aberta")
		);
	}

	private InsightRhResponse criarInsightBasico(InsightRhRequest request, String resposta) {
		return new InsightRhResponse(
			resposta.length() > 300 ? resposta.substring(0, 300) + "..." : resposta,
			Arrays.asList("Revisar dados de humor", "Monitorar setores com estresse"),
			Arrays.asList("Sistema de apoio psicológico ativo"),
			Arrays.asList("Manter acompanhamento regular", "Promover atividades de bem-estar"),
			"estável"
		);
	}

	private SugestaoAtividadeResponse criarSugestaoBasica(SugestaoAtividadeRequest request, String resposta) {
		List<String> atividades = Arrays.asList(
			"Meditação guiada",
			"Pausa ativa",
			"Workshop de gestão de estresse"
		);
		return new SugestaoAtividadeResponse(
			atividades,
			resposta.length() > 200 ? resposta.substring(0, 200) + "..." : resposta,
			3
		);
	}
}

