package workwell.WorkWell.service;

import jakarta.transaction.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import workwell.WorkWell.dto.avaliacao.AvaliacaoProfundaCreateRequest;
import workwell.WorkWell.dto.avaliacao.AvaliacaoProfundaResponse;
import workwell.WorkWell.dto.avaliacao.RelatorioAvaliacaoProfundaResponse;
import workwell.WorkWell.dto.avaliacao.RespostaAvaliacaoProfundaRequest;
import workwell.WorkWell.dto.avaliacao.RespostaAvaliacaoProfundaResponse;
import workwell.WorkWell.dto.avaliacao.RespostaDetalhadaResponse;
import workwell.WorkWell.entity.AvaliacaoProfunda;
import workwell.WorkWell.entity.Empresa;
import workwell.WorkWell.entity.RespostaAvaliacaoProfunda;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.entity.enums.RoleType;
import workwell.WorkWell.exception.BusinessException;
import workwell.WorkWell.exception.ResourceNotFoundException;
import workwell.WorkWell.repository.AvaliacaoProfundaRepository;
import workwell.WorkWell.repository.RespostaAvaliacaoProfundaRepository;
import workwell.WorkWell.repository.UsuarioRepository;

@Service
public class AvaliacaoProfundaService {

	private final AvaliacaoProfundaRepository avaliacaoRepository;
	private final RespostaAvaliacaoProfundaRepository respostaRepository;
	private final UsuarioRepository usuarioRepository;
	private final ObjectMapper objectMapper;

	public AvaliacaoProfundaService(
		AvaliacaoProfundaRepository avaliacaoRepository,
		RespostaAvaliacaoProfundaRepository respostaRepository,
		UsuarioRepository usuarioRepository,
		ObjectMapper objectMapper
	) {
		this.avaliacaoRepository = avaliacaoRepository;
		this.respostaRepository = respostaRepository;
		this.usuarioRepository = usuarioRepository;
		this.objectMapper = objectMapper;
	}

	@Transactional
	@CacheEvict(value = {"avaliacoesAtivas", "dashboard"}, allEntries = true)
	public AvaliacaoProfundaResponse criarAvaliacao(Usuario usuario, AvaliacaoProfundaCreateRequest request) {
		if (usuario.getRole() != RoleType.PSICOLOGO) {
			throw new BusinessException("Somente psicólogos podem criar avaliações profundas");
		}

		Empresa empresa = garantirEmpresa(usuario);

		// Validar datas
		if (request.dataFim().isBefore(request.dataInicio())) {
			throw new BusinessException("A data de fim deve ser posterior à data de início");
		}

		if (request.dataInicio().isBefore(LocalDateTime.now())) {
			throw new BusinessException("A data de início não pode ser no passado");
		}

		AvaliacaoProfunda avaliacao = new AvaliacaoProfunda();
		avaliacao.setEmpresa(empresa);
		avaliacao.setCriadoPor(usuario);
		avaliacao.setTitulo(request.titulo());
		avaliacao.setDescricao(request.descricao());
		avaliacao.setDataInicio(request.dataInicio());
		avaliacao.setDataFim(request.dataFim());
		avaliacao.setAtiva(true);

		// Converter lista de perguntas para JSON
		try {
			avaliacao.setPerguntas(objectMapper.writeValueAsString(request.perguntas()));
		} catch (Exception e) {
			throw new BusinessException("Erro ao processar perguntas: " + e.getMessage());
		}

		avaliacao = avaliacaoRepository.save(avaliacao);
		return mapAvaliacao(avaliacao, usuario.getId());
	}

	@Transactional
	@CacheEvict(value = {"avaliacoesAtivas", "estatisticas"}, allEntries = true)
	public RespostaAvaliacaoProfundaResponse responderAvaliacao(Usuario usuario, RespostaAvaliacaoProfundaRequest request) {
		if (usuario.getRole() != RoleType.FUNCIONARIO) {
			throw new BusinessException("Somente funcionários podem responder avaliações profundas");
		}

		Empresa empresa = garantirEmpresa(usuario);
		AvaliacaoProfunda avaliacao = avaliacaoRepository.findByIdAndEmpresaId(request.avaliacaoId(), empresa.getId())
			.orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));

		if (!avaliacao.getAtiva()) {
			throw new BusinessException("Avaliação não está mais ativa");
		}

		LocalDateTime agora = LocalDateTime.now();
		if (agora.isBefore(avaliacao.getDataInicio()) || agora.isAfter(avaliacao.getDataFim())) {
			throw new BusinessException("Avaliação não está disponível no momento");
		}

		// Verificar se já respondeu
		if (respostaRepository.findByAvaliacaoIdAndUsuarioId(avaliacao.getId(), usuario.getId()).isPresent()) {
			throw new BusinessException("Você já respondeu esta avaliação");
		}

		RespostaAvaliacaoProfunda resposta = new RespostaAvaliacaoProfunda();
		resposta.setAvaliacao(avaliacao);
		resposta.setUsuario(usuario);
		resposta.setObservacoes(request.observacoes());

		// Converter respostas para JSON
		try {
			resposta.setRespostas(objectMapper.writeValueAsString(request.respostas()));
		} catch (Exception e) {
			throw new BusinessException("Erro ao processar respostas: " + e.getMessage());
		}

		resposta = respostaRepository.save(resposta);
		return new RespostaAvaliacaoProfundaResponse(
			resposta.getId(),
			resposta.getAvaliacao().getId(),
			resposta.getUsuario().getId(),
			request.respostas(),
			resposta.getObservacoes(),
			resposta.getCreatedAt()
		);
	}

	@Cacheable(value = "avaliacoesAtivas", key = "#empresaId + '_' + #usuarioId")
	public List<AvaliacaoProfundaResponse> listarAvaliacoesAtivas(UUID empresaId, UUID usuarioId) {
		List<AvaliacaoProfunda> avaliacoes = avaliacaoRepository.buscarAvaliacoesAtivas(empresaId);
		return avaliacoes.stream()
			.map(a -> mapAvaliacao(a, usuarioId))
			.toList();
	}

	@Cacheable(value = "avaliacoesAtivas", key = "'psicologo_' + #usuario.empresa.id + '_' + #usuario.id")
	public List<AvaliacaoProfundaResponse> listarAvaliacoesPorPsicologo(Usuario usuario) {
		if (usuario.getRole() != RoleType.PSICOLOGO) {
			throw new BusinessException("Somente psicólogos podem visualizar suas avaliações criadas");
		}

		Empresa empresa = garantirEmpresa(usuario);
		List<AvaliacaoProfunda> avaliacoes = avaliacaoRepository.buscarAvaliacoesPorPsicologo(empresa.getId(), usuario.getId());
		return avaliacoes.stream()
			.map(a -> mapAvaliacao(a, usuario.getId()))
			.toList();
	}

	public RelatorioAvaliacaoProfundaResponse obterRelatorio(UUID avaliacaoId, Usuario usuario) {
		if (usuario.getRole() != RoleType.PSICOLOGO) {
			throw new BusinessException("Somente psicólogos podem visualizar relatórios de avaliações");
		}

		Empresa empresa = garantirEmpresa(usuario);
		AvaliacaoProfunda avaliacao = avaliacaoRepository.findByIdAndEmpresaId(avaliacaoId, empresa.getId())
			.orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada"));

		// Verificar se o psicólogo criou esta avaliação
		if (!avaliacao.getCriadoPor().getId().equals(usuario.getId())) {
			throw new BusinessException("Você não tem permissão para visualizar este relatório");
		}

		Long totalRespostas = respostaRepository.contarRespostasPorAvaliacao(avaliacaoId);
		int totalUsuarios = usuarioRepository.findByEmpresaIdAndRoleOrderByNomeAsc(empresa.getId(), RoleType.FUNCIONARIO).size();
		Double taxaResposta = totalUsuarios > 0 ? (double) totalRespostas / totalUsuarios : 0.0;

		// Buscar todas as respostas
		List<RespostaAvaliacaoProfunda> respostas = respostaRepository.buscarRespostasPorAvaliacao(empresa.getId(), avaliacaoId);
		List<RespostaDetalhadaResponse> respostasDetalhadas = respostas.stream()
			.map(r -> {
				Map<String, Object> respostasMap = new HashMap<>();
				try {
					if (r.getRespostas() != null && !r.getRespostas().isEmpty()) {
						respostasMap = objectMapper.readValue(r.getRespostas(), new TypeReference<Map<String, Object>>() {});
					}
				} catch (Exception e) {
					// Ignorar erro de parsing
				}

				return new RespostaDetalhadaResponse(
					r.getId(),
					r.getUsuario().getId(),
					r.getUsuario().getNome(),
					r.getUsuario().getEmail(),
					respostasMap,
					r.getObservacoes(),
					r.getCreatedAt()
				);
			})
			.toList();

		// Análise estatística básica
		Map<String, Object> analiseEstatistica = gerarAnaliseEstatistica(avaliacao, respostas);

		return new RelatorioAvaliacaoProfundaResponse(
			avaliacao.getId(),
			avaliacao.getTitulo(),
			avaliacao.getDescricao(),
			avaliacao.getDataInicio(),
			avaliacao.getDataFim(),
			totalRespostas,
			(long) totalUsuarios,
			taxaResposta,
			respostasDetalhadas,
			analiseEstatistica
		);
	}

	private Map<String, Object> gerarAnaliseEstatistica(AvaliacaoProfunda avaliacao, List<RespostaAvaliacaoProfunda> respostas) {
		Map<String, Object> analise = new HashMap<>();
		analise.put("totalRespostas", (long) respostas.size());
		analise.put("dataGeracao", LocalDateTime.now());

		// Tentar extrair estatísticas das perguntas numéricas
		try {
			List<Map<String, Object>> perguntas = objectMapper.readValue(
				avaliacao.getPerguntas(),
				new TypeReference<List<Map<String, Object>>>() {}
			);

			Map<String, List<Double>> valoresPorPergunta = new HashMap<>();
			for (RespostaAvaliacaoProfunda resposta : respostas) {
				Map<String, Object> respostasMap = objectMapper.readValue(
					resposta.getRespostas(),
					new TypeReference<Map<String, Object>>() {}
				);

				for (Map<String, Object> pergunta : perguntas) {
					String perguntaId = String.valueOf(pergunta.get("id"));
					String tipo = (String) pergunta.get("tipo");
					
					if ("NUMERICO".equals(tipo) && respostasMap.containsKey(perguntaId)) {
						Object valorObj = respostasMap.get(perguntaId);
						if (valorObj instanceof Number) {
							valoresPorPergunta.computeIfAbsent(perguntaId, k -> new ArrayList<>())
								.add(((Number) valorObj).doubleValue());
						}
					}
				}
			}

			Map<String, Map<String, Double>> estatisticas = new HashMap<>();
			for (Map.Entry<String, List<Double>> entry : valoresPorPergunta.entrySet()) {
				List<Double> valores = entry.getValue();
				double media = valores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
				double max = valores.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
				double min = valores.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);

				Map<String, Double> stats = new HashMap<>();
				stats.put("media", media);
				stats.put("maximo", max);
				stats.put("minimo", min);
				estatisticas.put(entry.getKey(), stats);
			}

			analise.put("estatisticasPorPergunta", estatisticas);
		} catch (Exception e) {
			// Ignorar erro de parsing
		}

		return analise;
	}

	private AvaliacaoProfundaResponse mapAvaliacao(AvaliacaoProfunda avaliacao, UUID usuarioId) {
		Long totalRespostas = respostaRepository.contarRespostasPorAvaliacao(avaliacao.getId());
		int totalUsuarios = usuarioRepository.findByEmpresaIdAndRoleOrderByNomeAsc(avaliacao.getEmpresa().getId(), RoleType.FUNCIONARIO).size();
		Double taxaResposta = totalUsuarios > 0 ? (double) totalRespostas / totalUsuarios : 0.0;

		Boolean jaRespondeu = respostaRepository.findByAvaliacaoIdAndUsuarioId(avaliacao.getId(), usuarioId).isPresent();

		// Converter perguntas de JSON para List
		List<Map<String, Object>> perguntas = new ArrayList<>();
		if (avaliacao.getPerguntas() != null && !avaliacao.getPerguntas().isEmpty()) {
			try {
				perguntas = objectMapper.readValue(avaliacao.getPerguntas(), new TypeReference<List<Map<String, Object>>>() {});
			} catch (Exception e) {
				// Se falhar, deixar vazio
			}
		}

		return new AvaliacaoProfundaResponse(
			avaliacao.getId(),
			avaliacao.getTitulo(),
			avaliacao.getDescricao(),
			perguntas,
			avaliacao.getAtiva(),
			avaliacao.getDataInicio(),
			avaliacao.getDataFim(),
			avaliacao.getCreatedAt(),
			totalRespostas,
			(long) totalUsuarios,
			taxaResposta,
			jaRespondeu
		);
	}

	private Empresa garantirEmpresa(Usuario usuario) {
		if (usuario.getEmpresa() == null) {
			throw new BusinessException("Usuário não vinculado a nenhuma empresa");
		}
		return usuario.getEmpresa();
	}
}

