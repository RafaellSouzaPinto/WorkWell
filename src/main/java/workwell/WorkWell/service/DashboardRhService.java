package workwell.WorkWell.service;

import jakarta.transaction.Transactional;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import workwell.WorkWell.dto.dashboard.AlertaResponse;
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
import workwell.WorkWell.dto.dashboard.RespostaEstatisticaResponse;
import workwell.WorkWell.dto.dashboard.SetorEstresseResponse;
import workwell.WorkWell.entity.AtividadeBemEstar;
import workwell.WorkWell.entity.Empresa;
import workwell.WorkWell.entity.Enquete;
import workwell.WorkWell.entity.ParticipacaoAtividade;
import workwell.WorkWell.entity.RegistroHumor;
import workwell.WorkWell.entity.RespostaEnquete;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.entity.enums.RoleType;
import workwell.WorkWell.exception.BusinessException;
import workwell.WorkWell.exception.ResourceNotFoundException;
import workwell.WorkWell.repository.AtividadeBemEstarRepository;
import workwell.WorkWell.repository.ConsultaPsicologicaRepository;
import workwell.WorkWell.repository.EnqueteRepository;
import workwell.WorkWell.repository.ParticipacaoAtividadeRepository;
import workwell.WorkWell.repository.RegistroHumorRepository;
import workwell.WorkWell.repository.RespostaEnqueteRepository;
import workwell.WorkWell.repository.UsuarioRepository;

@Service
public class DashboardRhService {

	private final RegistroHumorRepository registroHumorRepository;
	private final ConsultaPsicologicaRepository consultaRepository;
	private final EnqueteRepository enqueteRepository;
	private final RespostaEnqueteRepository respostaEnqueteRepository;
	private final AtividadeBemEstarRepository atividadeRepository;
	private final ParticipacaoAtividadeRepository participacaoRepository;
	private final UsuarioRepository usuarioRepository;
	private final ObjectMapper objectMapper;

	private static final double TAXA_PARTICIPACAO_MINIMA = 0.6; // 60%
	private static final int DIAS_ANALISE = 30;

	public DashboardRhService(
		RegistroHumorRepository registroHumorRepository,
		ConsultaPsicologicaRepository consultaRepository,
		EnqueteRepository enqueteRepository,
		RespostaEnqueteRepository respostaEnqueteRepository,
		AtividadeBemEstarRepository atividadeRepository,
		ParticipacaoAtividadeRepository participacaoRepository,
		UsuarioRepository usuarioRepository,
		ObjectMapper objectMapper
	) {
		this.registroHumorRepository = registroHumorRepository;
		this.consultaRepository = consultaRepository;
		this.enqueteRepository = enqueteRepository;
		this.respostaEnqueteRepository = respostaEnqueteRepository;
		this.atividadeRepository = atividadeRepository;
		this.participacaoRepository = participacaoRepository;
		this.usuarioRepository = usuarioRepository;
		this.objectMapper = objectMapper;
	}


	public DashboardRhResponse obterDashboard(Usuario usuario) {
		Empresa empresa = garantirEmpresa(usuario);
		UUID empresaId = empresa.getId();

		LocalDateTime dataInicio = LocalDateTime.now().minusDays(DIAS_ANALISE);

		// Nível médio de humor
		Double nivelMedioHumor = registroHumorRepository.calcularMediaHumor(empresaId, dataInicio);

		// Setores com maior estresse
		List<SetorEstresseResponse> setoresComEstresse = buscarSetoresComEstresse(empresaId, dataInicio);

		// Frequência nas consultas psicológicas
		Long frequenciaConsultas = consultaRepository.count();

		// Aderência às atividades de bem-estar
		Double aderenciaAtividades = calcularAderenciaAtividades(empresaId, dataInicio);

		// Enquetes ativas
		List<EnqueteResponse> enquetesAtivas = listarEnquetesAtivas(empresaId, usuario.getId());

		// Alertas automáticos
		List<AlertaResponse> alertas = gerarAlertas(empresaId, dataInicio);

		return new DashboardRhResponse(
			nivelMedioHumor,
			setoresComEstresse,
			frequenciaConsultas,
			aderenciaAtividades,
			enquetesAtivas,
			alertas
		);
	}

	@Transactional
	public RegistroHumorResponse registrarHumor(Usuario usuario, RegistroHumorRequest request) {
		Empresa empresa = garantirEmpresa(usuario);

		RegistroHumor registro = new RegistroHumor();
		registro.setEmpresa(empresa);
		registro.setUsuario(usuario);
		registro.setNivelHumor(request.nivelHumor());
		registro.setSetor(request.setor());
		registro.setObservacoes(request.observacoes());

		registro = registroHumorRepository.save(registro);
		return new RegistroHumorResponse(
			registro.getId(),
			registro.getEmpresa().getId(),
			registro.getUsuario().getId(),
			registro.getNivelHumor(),
			registro.getSetor(),
			registro.getObservacoes(),
			registro.getCreatedAt()
		);
	}

	@Transactional
	public EnqueteResponse criarEnquete(Usuario usuario, EnqueteCreateRequest request) {
		if (usuario.getRole() != RoleType.RH) {
			throw new BusinessException("Somente RH pode criar enquetes");
		}

		Empresa empresa = garantirEmpresa(usuario);

		Enquete enquete = new Enquete();
		enquete.setEmpresa(empresa);
		enquete.setCriadoPor(usuario);
		enquete.setPergunta(request.pergunta());
		enquete.setAtiva(true);
		enquete.setDataFim(request.dataFim());

		// Converter lista de opções para JSON
		List<String> opcoes = request.opcoesResposta() != null && !request.opcoesResposta().isEmpty()
			? request.opcoesResposta()
			: Arrays.asList("SIM", "NÃO", "TALVEZ"); // Opções padrão
		
		try {
			enquete.setOpcoesResposta(objectMapper.writeValueAsString(opcoes));
		} catch (Exception e) {
			throw new BusinessException("Erro ao processar opções de resposta: " + e.getMessage());
		}

		enquete = enqueteRepository.save(enquete);
		return mapEnquete(enquete, usuario.getId());
	}

	@Transactional
	public RespostaEnqueteResponse responderEnquete(Usuario usuario, RespostaEnqueteRequest request) {
		Empresa empresa = garantirEmpresa(usuario);
		Enquete enquete = enqueteRepository.findByIdAndEmpresaId(request.enqueteId(), empresa.getId())
			.orElseThrow(() -> new ResourceNotFoundException("Enquete não encontrada"));

		if (!enquete.getAtiva() || (enquete.getDataFim() != null && enquete.getDataFim().isBefore(LocalDateTime.now()))) {
			throw new BusinessException("Enquete não está mais ativa");
		}

		// Verificar se já respondeu
		if (respostaEnqueteRepository.findByEnqueteIdAndUsuarioId(enquete.getId(), usuario.getId()).isPresent()) {
			throw new BusinessException("Você já respondeu esta enquete");
		}

		RespostaEnquete resposta = new RespostaEnquete();
		resposta.setEnquete(enquete);
		resposta.setUsuario(usuario);
		resposta.setResposta(request.resposta());

		resposta = respostaEnqueteRepository.save(resposta);
		return new RespostaEnqueteResponse(
			resposta.getId(),
			resposta.getEnquete().getId(),
			resposta.getUsuario().getId(),
			resposta.getResposta(),
			resposta.getCreatedAt()
		);
	}

	public List<EnqueteResponse> listarEnquetesAtivas(UUID empresaId, UUID usuarioId) {
		List<Enquete> enquetes = enqueteRepository.buscarEnquetesAtivas(empresaId);
		return enquetes.stream()
			.map(e -> mapEnquete(e, usuarioId))
			.toList();
	}

	@Transactional
	public AtividadeBemEstarResponse criarAtividade(Usuario usuario, AtividadeBemEstarCreateRequest request) {
		if (usuario.getRole() != RoleType.RH) {
			throw new BusinessException("Somente RH pode criar atividades");
		}

		Empresa empresa = garantirEmpresa(usuario);

		AtividadeBemEstar atividade = new AtividadeBemEstar();
		atividade.setEmpresa(empresa);
		atividade.setCriadoPor(usuario);
		atividade.setTipo(request.tipo());
		atividade.setTitulo(request.titulo());
		atividade.setDescricao(request.descricao());
		atividade.setDataHoraInicio(request.dataHoraInicio());
		atividade.setDataHoraFim(request.dataHoraFim());
		atividade.setLocal(request.local());
		atividade.setAtiva(true);

		atividade = atividadeRepository.save(atividade);
		return mapAtividade(atividade, usuario.getId());
	}

	public List<AtividadeBemEstarResponse> listarAtividades(UUID empresaId, UUID usuarioId) {
		LocalDateTime dataInicio = LocalDateTime.now().minusDays(DIAS_ANALISE);
		List<AtividadeBemEstar> atividades = atividadeRepository.buscarAtividadesRecentes(empresaId, dataInicio);
		return atividades.stream()
			.map(a -> mapAtividade(a, usuarioId))
			.toList();
	}

	@Transactional
	public ParticipacaoAtividadeResponse participarAtividade(Usuario usuario, ParticipacaoAtividadeRequest request) {
		Empresa empresa = garantirEmpresa(usuario);
		AtividadeBemEstar atividade = atividadeRepository.findByIdAndEmpresaId(request.atividadeId(), empresa.getId())
			.orElseThrow(() -> new ResourceNotFoundException("Atividade não encontrada"));

		if (!atividade.getAtiva()) {
			throw new BusinessException("Atividade não está mais ativa");
		}

		// Verificar se já registrou participação (pode atualizar se já existe)
		ParticipacaoAtividade participacao = participacaoRepository
			.findByAtividadeIdAndUsuarioId(atividade.getId(), usuario.getId())
			.orElse(new ParticipacaoAtividade());

		participacao.setAtividade(atividade);
		participacao.setUsuario(usuario);
		participacao.setVaiParticipar(request.vaiParticipar() != null ? request.vaiParticipar() : true);

		participacao = participacaoRepository.save(participacao);
		return new ParticipacaoAtividadeResponse(
			participacao.getId(),
			participacao.getAtividade().getId(),
			participacao.getUsuario().getId(),
			participacao.getVaiParticipar(),
			participacao.getCreatedAt()
		);
	}

	private List<SetorEstresseResponse> buscarSetoresComEstresse(UUID empresaId, LocalDateTime dataInicio) {
		List<Object[]> resultados = registroHumorRepository.buscarSetoresComEstresse(empresaId, dataInicio);
		return resultados.stream()
			.map(r -> new SetorEstresseResponse(
				(String) r[0],
				((Number) r[1]).doubleValue(),
				((Number) r[2]).longValue()
			))
			.toList();
	}

	private Double calcularAderenciaAtividades(UUID empresaId, LocalDateTime dataInicio) {
		int totalUsuarios = usuarioRepository.findByEmpresaIdAndRoleOrderByNomeAsc(empresaId, RoleType.FUNCIONARIO).size();
		if (totalUsuarios == 0) {
			return 0.0;
		}

		Long usuariosParticipantes = participacaoRepository.contarUsuariosUnicosParticipantes(empresaId, dataInicio);
		return usuariosParticipantes != null ? (double) usuariosParticipantes / totalUsuarios : 0.0;
	}

	private List<AlertaResponse> gerarAlertas(UUID empresaId, LocalDateTime dataInicio) {
		List<AlertaResponse> alertas = new ArrayList<>();

		// Verificar aderência às atividades
		Double aderencia = calcularAderenciaAtividades(empresaId, dataInicio);
		if (aderencia < TAXA_PARTICIPACAO_MINIMA) {
			alertas.add(new AlertaResponse(
				"ADERENCIA_BAIXA",
				String.format("Aderência às atividades está em %.1f%%. Considere criar questionários para entender a baixa participação.", aderencia * 100),
				"MEDIA"
			));
		}

		// Verificar participação em enquetes
		List<Enquete> enquetes = enqueteRepository.buscarEnquetesAtivas(empresaId);
		for (Enquete enquete : enquetes) {
			Long totalRespostas = respostaEnqueteRepository.contarRespostasPorEnquete(enquete.getId());
			int totalUsuarios = usuarioRepository.findByEmpresaIdAndRoleOrderByNomeAsc(empresaId, RoleType.FUNCIONARIO).size();
			if (totalUsuarios > 0) {
				double taxaResposta = (double) totalRespostas / totalUsuarios;
				if (taxaResposta < TAXA_PARTICIPACAO_MINIMA) {
					alertas.add(new AlertaResponse(
						"ENQUETE_PARTICIPACAO_BAIXA",
						String.format("Enquete '%s' tem apenas %.1f%% de participação.", enquete.getPergunta(), taxaResposta * 100),
						"BAIXA"
					));
				}
			}
		}

		// Verificar nível de humor baixo
		Double nivelMedioHumor = registroHumorRepository.calcularMediaHumor(empresaId, dataInicio);
		if (nivelMedioHumor != null && nivelMedioHumor < 2.5) {
			alertas.add(new AlertaResponse(
				"HUMOR_BAIXO",
				String.format("Nível médio de humor está em %.1f/5.0. Considere ações para melhorar o bem-estar.", nivelMedioHumor),
				"ALTA"
			));
		}

		return alertas;
	}

	private EnqueteResponse mapEnquete(Enquete enquete, UUID usuarioId) {
		Long totalRespostas = respostaEnqueteRepository.contarRespostasPorEnquete(enquete.getId());
		Long totalUsuarios = (long) usuarioRepository.findByEmpresaIdAndRoleOrderByNomeAsc(enquete.getEmpresa().getId(), RoleType.FUNCIONARIO).size();
		Double taxaResposta = totalUsuarios > 0 ? (double) totalRespostas / totalUsuarios : 0.0;

		List<Object[]> estatisticasRaw = respostaEnqueteRepository.contarRespostasPorTipo(enquete.getId());
		List<RespostaEstatisticaResponse> estatisticas = estatisticasRaw.stream()
			.map(r -> new RespostaEstatisticaResponse(
				(String) r[0],
				((Number) r[1]).longValue()
			))
			.toList();

		Boolean jaRespondeu = respostaEnqueteRepository.findByEnqueteIdAndUsuarioId(enquete.getId(), usuarioId).isPresent();

		// Converter opções de JSON para List
		List<String> opcoesResposta = new ArrayList<>();
		if (enquete.getOpcoesResposta() != null && !enquete.getOpcoesResposta().isEmpty()) {
			try {
				opcoesResposta = objectMapper.readValue(enquete.getOpcoesResposta(), new TypeReference<List<String>>() {});
			} catch (Exception e) {
				// Se falhar, usar opções padrão
				opcoesResposta = Arrays.asList("SIM", "NÃO", "TALVEZ");
			}
		} else {
			opcoesResposta = Arrays.asList("SIM", "NÃO", "TALVEZ");
		}

		return new EnqueteResponse(
			enquete.getId(),
			enquete.getPergunta(),
			enquete.getAtiva(),
			enquete.getDataFim(),
			enquete.getCreatedAt(),
			totalRespostas,
			totalUsuarios,
			taxaResposta,
			estatisticas,
			jaRespondeu,
			opcoesResposta
		);
	}

	private AtividadeBemEstarResponse mapAtividade(AtividadeBemEstar atividade, UUID usuarioId) {
		Long totalParticipantes = participacaoRepository.contarParticipantesConfirmados(atividade.getId());
		Long totalUsuarios = (long) usuarioRepository.findByEmpresaIdAndRoleOrderByNomeAsc(atividade.getEmpresa().getId(), RoleType.FUNCIONARIO).size();
		Double taxaParticipacao = totalUsuarios > 0 ? (double) totalParticipantes / totalUsuarios : 0.0;

		Optional<ParticipacaoAtividade> participacaoOpt = participacaoRepository.findByAtividadeIdAndUsuarioId(atividade.getId(), usuarioId);
		Boolean jaParticipou = participacaoOpt.isPresent();
		Boolean vaiParticipar = participacaoOpt.map(ParticipacaoAtividade::getVaiParticipar).orElse(null);

		// Buscar lista de participantes (apenas quem vai participar)
		List<ParticipacaoAtividade> participacoes = participacaoRepository.findByAtividadeId(atividade.getId());
		List<ParticipanteAtividadeResponse> participantes = participacoes.stream()
			.filter(p -> Boolean.TRUE.equals(p.getVaiParticipar()))
			.map(p -> new ParticipanteAtividadeResponse(
				p.getId(),
				p.getUsuario().getId(),
				p.getUsuario().getNome(),
				p.getUsuario().getEmail(),
				p.getCreatedAt()
			))
			.toList();

		return new AtividadeBemEstarResponse(
			atividade.getId(),
			atividade.getTipo(),
			atividade.getTitulo(),
			atividade.getDescricao(),
			atividade.getDataHoraInicio(),
			atividade.getDataHoraFim(),
			atividade.getLocal(),
			atividade.getAtiva(),
			totalParticipantes,
			totalUsuarios,
			taxaParticipacao,
			jaParticipou,
			vaiParticipar,
			participantes
		);
	}

	public List<RegistroHumorDetalhadoResponse> listarRegistrosHumor(UUID empresaId) {
		List<RegistroHumor> registros = registroHumorRepository.findByEmpresaIdOrderByCreatedAtDesc(empresaId);
		return registros.stream()
			.map(r -> new RegistroHumorDetalhadoResponse(
				r.getId(),
				r.getUsuario().getId(),
				r.getUsuario().getNome(),
				r.getNivelHumor(),
				r.getSetor(),
				r.getObservacoes(),
				r.getCreatedAt()
			))
			.toList();
	}

	public List<ParticipanteAtividadeResponse> listarParticipantesAtividade(UUID atividadeId, UUID empresaId) {
		AtividadeBemEstar atividade = atividadeRepository.findByIdAndEmpresaId(atividadeId, empresaId)
			.orElseThrow(() -> new ResourceNotFoundException("Atividade não encontrada"));
		
		List<ParticipacaoAtividade> participacoes = participacaoRepository.findByAtividadeId(atividade.getId());
		return participacoes.stream()
			.filter(p -> Boolean.TRUE.equals(p.getVaiParticipar()))
			.map(p -> new ParticipanteAtividadeResponse(
				p.getId(),
				p.getUsuario().getId(),
				p.getUsuario().getNome(),
				p.getUsuario().getEmail(),
				p.getCreatedAt()
			))
			.toList();
	}

	private Empresa garantirEmpresa(Usuario usuario) {
		if (usuario.getEmpresa() == null) {
			throw new BusinessException("Usuário não vinculado a nenhuma empresa");
		}
		return usuario.getEmpresa();
	}
}

