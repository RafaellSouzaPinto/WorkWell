package workwell.WorkWell.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.model.ChatResponse;
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

	public AIService(ChatModel chatModel, ObjectMapper objectMapper) {
		this.chatModel = chatModel;
		this.objectMapper = objectMapper;
	}

	public workwell.WorkWell.dto.ai.ChatResponse chat(ChatRequest request) {
		String promptText = buildChatPrompt(request.mensagem(), request.contexto());
		String resposta = callChatModel(promptText);
		return new workwell.WorkWell.dto.ai.ChatResponse(resposta, request.contexto());
	}

	public AnaliseSentimentoResponse analisarSentimento(AnaliseSentimentoRequest request) {
		String prompt = buildAnaliseSentimentoPrompt(request);
		String resposta = callChatModel(prompt);

		try {
			return parseAnaliseSentimento(resposta);
		} catch (Exception e) {
			// Fallback: retornar análise básica
			return criarAnaliseBasica(request, resposta);
		}
	}

	public InsightRhResponse gerarInsightsRh(InsightRhRequest request) {
		String prompt = buildInsightRhPrompt(request);
		String resposta = callChatModel(prompt);

		try {
			return parseInsightRh(resposta);
		} catch (Exception e) {
			// Fallback: retornar insights básicos
			return criarInsightBasico(request, resposta);
		}
	}

	public SugestaoAtividadeResponse sugerirAtividades(SugestaoAtividadeRequest request) {
		String prompt = buildSugestaoAtividadePrompt(request);
		String resposta = callChatModel(prompt);

		try {
			return parseSugestaoAtividade(resposta);
		} catch (Exception e) {
			// Fallback: retornar sugestões básicas
			return criarSugestaoBasica(request, resposta);
		}
	}

	private String buildChatPrompt(String mensagem, String contexto) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("Você é um assistente virtual do WorkWell, uma plataforma de bem-estar corporativo. ");
		prompt.append("Seu papel é ajudar funcionários, psicólogos e RH com questões relacionadas a saúde mental, ");
		prompt.append("bem-estar no trabalho e apoio psicológico.\n\n");

		if (contexto != null && !contexto.isEmpty()) {
			prompt.append("Contexto adicional: ").append(contexto).append("\n\n");
		}

		prompt.append("Pergunta do usuário: ").append(mensagem).append("\n\n");
		prompt.append("Por favor, forneça uma resposta útil, empática e profissional.");

		return prompt.toString();
	}

	private String buildAnaliseSentimentoPrompt(AnaliseSentimentoRequest request) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("Analise o sentimento e o estado emocional baseado nos seguintes dados:\n\n");
		prompt.append("Texto/Observações: ").append(request.texto() != null ? request.texto() : "Não informado").append("\n");
		prompt.append("Nível de Humor (1-10): ").append(request.nivelHumor() != null ? request.nivelHumor() : "Não informado").append("\n");
		prompt.append("Setor: ").append(request.setor() != null ? request.setor() : "Não informado").append("\n\n");

		prompt.append("Forneça uma análise estruturada em JSON com o seguinte formato:\n");
		prompt.append("{\n");
		prompt.append("  \"sentimento\": \"positivo/neutro/negativo\",\n");
		prompt.append("  \"score\": 0.0 a 1.0,\n");
		prompt.append("  \"resumo\": \"resumo da análise\",\n");
		prompt.append("  \"pontosChave\": [\"ponto1\", \"ponto2\"],\n");
		prompt.append("  \"recomendacoes\": [\"recomendação1\", \"recomendação2\"]\n");
		prompt.append("}\n\n");
		prompt.append("Seja específico e ofereça recomendações práticas para melhorar o bem-estar.");

		return prompt.toString();
	}

	private String buildInsightRhPrompt(InsightRhRequest request) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("Como especialista em RH e bem-estar corporativo, analise os seguintes dados do dashboard:\n\n");
		prompt.append("Nível Médio de Humor: ").append(request.nivelMedioHumor() != null ? request.nivelMedioHumor() : "N/A").append("\n");
		prompt.append("Frequência de Consultas: ").append(request.frequenciaConsultas()).append("\n");
		prompt.append("Aderência às Atividades: ").append(request.aderenciaAtividades() != null ? request.aderenciaAtividades() + "%" : "N/A").append("\n\n");

		if (request.setoresComEstresse() != null && !request.setoresComEstresse().isEmpty()) {
			prompt.append("Setores com Estresse:\n");
			request.setoresComEstresse().forEach(setor -> {
				prompt.append("- ").append(setor.setor()).append(": Nível ").append(setor.nivelEstresse()).append("\n");
			});
			prompt.append("\n");
		}

		prompt.append("Forneça insights estratégicos em JSON com o seguinte formato:\n");
		prompt.append("{\n");
		prompt.append("  \"resumoExecutivo\": \"resumo geral\",\n");
		prompt.append("  \"pontosCriticos\": [\"ponto1\", \"ponto2\"],\n");
		prompt.append("  \"pontosPositivos\": [\"ponto1\", \"ponto2\"],\n");
		prompt.append("  \"recomendacoes\": [\"recomendação1\", \"recomendação2\"],\n");
		prompt.append("  \"tendencia\": \"melhorando/estável/preocupante\"\n");
		prompt.append("}\n\n");
		prompt.append("Seja estratégico e ofereça recomendações acionáveis para o RH.");

		return prompt.toString();
	}

	private String buildSugestaoAtividadePrompt(SugestaoAtividadeRequest request) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("Sugira atividades de bem-estar personalizadas com base nos seguintes dados:\n\n");
		prompt.append("Perfil do Funcionário: ").append(request.perfilFuncionario() != null ? request.perfilFuncionario() : "Não informado").append("\n");
		prompt.append("Nível de Humor Atual: ").append(request.nivelHumor() != null ? request.nivelHumor() : "Não informado").append("\n");
		prompt.append("Setor: ").append(request.setor() != null ? request.setor() : "Não informado").append("\n");

		if (request.atividadesAnteriores() != null && !request.atividadesAnteriores().isEmpty()) {
			prompt.append("Atividades Anteriores: ").append(String.join(", ", request.atividadesAnteriores())).append("\n");
		}
		prompt.append("\n");

		prompt.append("Forneça sugestões em JSON com o seguinte formato:\n");
		prompt.append("{\n");
		prompt.append("  \"atividadesSugeridas\": [\"atividade1\", \"atividade2\", \"atividade3\"],\n");
		prompt.append("  \"justificativa\": \"por que essas atividades são adequadas\",\n");
		prompt.append("  \"prioridade\": 1 a 5 (1 = mais importante)\n");
		prompt.append("}\n\n");
		prompt.append("Sugira atividades práticas, acessíveis e relevantes para o contexto corporativo.");

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

