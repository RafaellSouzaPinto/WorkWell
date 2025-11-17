package workwell.WorkWell.service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;
import workwell.WorkWell.dto.apoio.CancelamentoRequest;
import workwell.WorkWell.dto.apoio.ConsultaCreateRequest;
import workwell.WorkWell.dto.apoio.ConsultaResponse;
import workwell.WorkWell.dto.apoio.LinkCallRequest;
import workwell.WorkWell.dto.usuario.UsuarioResponse;
import workwell.WorkWell.entity.ConsultaPsicologica;
import workwell.WorkWell.entity.Empresa;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.entity.enums.ConsultaStatus;
import workwell.WorkWell.entity.enums.LocalAtendimento;
import workwell.WorkWell.entity.enums.RoleType;
import workwell.WorkWell.entity.enums.SalaAtendimento;
import workwell.WorkWell.exception.BusinessException;
import workwell.WorkWell.exception.ResourceNotFoundException;
import workwell.WorkWell.repository.ConsultaPsicologicaRepository;
import workwell.WorkWell.repository.UsuarioRepository;

@Service
public class ApoioPsicologicoService {

	private final ConsultaPsicologicaRepository consultaRepository;
	private final UsuarioRepository usuarioRepository;
	private final EmailNotificationService emailNotificationService;

	public ApoioPsicologicoService(ConsultaPsicologicaRepository consultaRepository,
		UsuarioRepository usuarioRepository, EmailNotificationService emailNotificationService) {
		this.consultaRepository = consultaRepository;
		this.usuarioRepository = usuarioRepository;
		this.emailNotificationService = emailNotificationService;
	}

	public List<UsuarioResponse> listarPsicologos(Usuario usuarioAutenticado) {
		UUID empresaId = garantirEmpresa(usuarioAutenticado).getId();
		return usuarioRepository.findByEmpresaIdAndRoleOrderByNomeAsc(empresaId, RoleType.PSICOLOGO)
			.stream()
			.map(this::mapUsuario)
			.toList();
	}

	public List<UsuarioResponse> listarFuncionarios(Usuario usuarioAutenticado) {
		if (usuarioAutenticado.getRole() != RoleType.PSICOLOGO && usuarioAutenticado.getRole() != RoleType.RH) {
			throw new BusinessException("Somente psicólogos e RH podem listar funcionários");
		}
		UUID empresaId = garantirEmpresa(usuarioAutenticado).getId();
		return usuarioRepository.findByEmpresaIdAndRoleOrderByNomeAsc(empresaId, RoleType.FUNCIONARIO)
			.stream()
			.map(this::mapUsuario)
			.toList();
	} 

	@Transactional
	public ConsultaResponse agendarConsulta(Usuario usuarioAutenticado, ConsultaCreateRequest request) {
		validarPerfilParaAgendamento(usuarioAutenticado);

		Empresa empresa = garantirEmpresa(usuarioAutenticado);
		Usuario psicologo = buscarUsuarioPorRole(request.psicologoId(), empresa.getId(), RoleType.PSICOLOGO);

		Usuario funcionario;
		if (usuarioAutenticado.getRole() == RoleType.FUNCIONARIO) {
			funcionario = usuarioAutenticado;
		}
		else if (usuarioAutenticado.getRole() == RoleType.RH) {
			if (request.funcionarioId() == null) {
				throw new BusinessException("Selecione o funcionário para o agendamento");
			}
			funcionario = buscarUsuarioPorRole(request.funcionarioId(), empresa.getId(), RoleType.FUNCIONARIO);
		}
		else {
			throw new BusinessException("Perfil não autorizado para agendar");
		}

		LocalDateTime inicio = combinarDataHora(request.data(), request.horaInicio());
		LocalDateTime fim = inicio.plusMinutes(request.duracaoMinutos());
		if (inicio.isBefore(LocalDateTime.now())) {
			throw new BusinessException("Agendamentos devem ser feitos para uma data futura");
		}

		LocalAtendimento local = request.local();
		SalaAtendimento sala = null;
		if (local == LocalAtendimento.PRESENCIAL) {
			sala = Objects.requireNonNull(request.sala(), "Selecione a sala para atendimentos presenciais");
			if (consultaRepository.existsSalaOcupada(empresa.getId(), local, sala, inicio, fim)) {
				throw new BusinessException("Sala indisponível no período selecionado");
			}
		}

		if (consultaRepository.existsConflitoAgenda(empresa.getId(), funcionario.getId(), inicio, fim)) {
			throw new BusinessException("Funcionário já possui atendimento marcado nesse horário");
		}
		if (consultaRepository.existsConflitoAgenda(empresa.getId(), psicologo.getId(), inicio, fim)) {
			throw new BusinessException("Psicólogo já possui atendimento marcado nesse horário");
		}

		ConsultaPsicologica consulta = new ConsultaPsicologica();
		consulta.setEmpresa(empresa);
		consulta.setFuncionario(funcionario);
		consulta.setPsicologo(psicologo);
		consulta.setCriadoPor(usuarioAutenticado);
		consulta.setAguardandoConfirmacaoDe(definirResponsavelConfirmacao(usuarioAutenticado.getRole(), funcionario, psicologo));
		consulta.setDataHoraInicio(inicio);
		consulta.setDataHoraFim(fim);
		consulta.setLocalAtendimento(local);
		consulta.setSala(sala);
		consulta.setObservacoes(request.observacoes());
		consulta.setStatus(ConsultaStatus.PENDENTE_CONFIRMACAO);

		ConsultaPsicologica consultaSalva = consultaRepository.save(consulta);

		// Enviar e-mails de notificação de forma assíncrona via RabbitMQ
		try {
			emailNotificationService.enviarNotificacaoAgendamentoConsulta(consultaSalva, funcionario);
			emailNotificationService.enviarNotificacaoAgendamentoConsulta(consultaSalva, psicologo);
		} catch (Exception e) {
			// Log do erro mas não interrompe o agendamento
			System.err.println("Erro ao enviar e-mails de agendamento: " + e.getMessage());
		}

		return mapConsulta(consultaSalva, usuarioAutenticado);
	}

	public List<ConsultaResponse> listarPendentes(Usuario usuarioAutenticado) {
		UUID empresaId = garantirEmpresa(usuarioAutenticado).getId();
		return consultaRepository.buscarPendentesParaUsuario(empresaId, usuarioAutenticado.getId())
			.stream()
			.map(c -> mapConsulta(c, usuarioAutenticado))
			.toList();
	}

	public List<ConsultaResponse> listarProximas(Usuario usuarioAutenticado) {
		UUID empresaId = garantirEmpresa(usuarioAutenticado).getId();
		// Apenas consultas confirmadas devem aparecer em "Próximos atendimentos"
		// Consultas pendentes aparecem em "Solicitações dos pacientes"
		List<ConsultaStatus> status = List.of(ConsultaStatus.CONFIRMADA);
		return consultaRepository.buscarPorParticipanteEStatus(empresaId, usuarioAutenticado.getId(), status)
			.stream()
			.filter(c -> c.getDataHoraFim().isAfter(LocalDateTime.now()))
			.map(c -> mapConsulta(c, usuarioAutenticado))
			.toList();
	}

	public List<ConsultaResponse> listarHistorico(Usuario usuarioAutenticado) {
		UUID empresaId = garantirEmpresa(usuarioAutenticado).getId();
		List<ConsultaStatus> status = List.of(ConsultaStatus.CONFIRMADA, ConsultaStatus.CONCLUIDA, ConsultaStatus.CANCELADA);
		return consultaRepository.buscarPorParticipanteEStatus(empresaId, usuarioAutenticado.getId(), status)
			.stream()
			.filter(c -> c.getDataHoraFim().isBefore(LocalDateTime.now()) || c.getStatus() == ConsultaStatus.CANCELADA
				|| c.getStatus() == ConsultaStatus.CONCLUIDA)
			.map(c -> mapConsulta(c, usuarioAutenticado))
			.toList();
	}

	public List<ConsultaResponse> listarMeusAgendamentos(Usuario usuarioAutenticado) {
		if (usuarioAutenticado.getRole() != RoleType.FUNCIONARIO && usuarioAutenticado.getRole() != RoleType.RH) {
			throw new BusinessException("Somente funcionários e RH podem visualizar seus agendamentos");
		}
		UUID empresaId = garantirEmpresa(usuarioAutenticado).getId();
		// Buscar todas as consultas onde o usuário é o funcionário (ou foi criado por ele se for RH)
		return consultaRepository.buscarPorFuncionarioOuCriadoPor(empresaId, usuarioAutenticado.getId())
			.stream()
			.map(c -> mapConsulta(c, usuarioAutenticado))
			.sorted((c1, c2) -> c2.dataHoraInicio().compareTo(c1.dataHoraInicio())) // Mais recentes primeiro
			.toList();
	}

	@Transactional
	public ConsultaResponse confirmarConsulta(Usuario usuarioAutenticado, UUID consultaId) {
		ConsultaPsicologica consulta = buscarConsulta(consultaId, usuarioAutenticado);
		if (consulta.getStatus() != ConsultaStatus.PENDENTE_CONFIRMACAO) {
			throw new BusinessException("Somente consultas pendentes podem ser confirmadas");
		}
		if (consulta.getAguardandoConfirmacaoDe() == null
			|| !consulta.getAguardandoConfirmacaoDe().getId().equals(usuarioAutenticado.getId())) {
			throw new BusinessException("Esta consulta não está aguardando sua confirmação");
		}
		consulta.setStatus(ConsultaStatus.CONFIRMADA);
		consulta.setAguardandoConfirmacaoDe(null);
		ConsultaPsicologica consultaSalva = consultaRepository.save(consulta);

		// Enviar e-mails de confirmação de forma assíncrona via RabbitMQ
		try {
			emailNotificationService.enviarNotificacaoConfirmacaoConsulta(consultaSalva, consulta.getFuncionario());
			emailNotificationService.enviarNotificacaoConfirmacaoConsulta(consultaSalva, consulta.getPsicologo());
		} catch (Exception e) {
			// Log do erro mas não interrompe a confirmação
			System.err.println("Erro ao enviar e-mails de confirmação: " + e.getMessage());
		}

		return mapConsulta(consultaSalva, usuarioAutenticado);
	}

	@Transactional
	public ConsultaResponse cancelarConsulta(Usuario usuarioAutenticado, UUID consultaId, CancelamentoRequest request) {
		ConsultaPsicologica consulta = buscarConsulta(consultaId, usuarioAutenticado);
		if (consulta.getStatus() == ConsultaStatus.CANCELADA) {
			throw new BusinessException("Consulta já cancelada");
		}
		if (!participaDaConsulta(consulta, usuarioAutenticado)) {
			throw new BusinessException("Você não participa desta consulta");
		}
		consulta.setStatus(ConsultaStatus.CANCELADA);
		consulta.setJustificativaCancelamento(request.justificativa());
		consulta.setAguardandoConfirmacaoDe(null);
		ConsultaPsicologica consultaSalva = consultaRepository.save(consulta);

		// Enviar e-mails de cancelamento de forma assíncrona via RabbitMQ
		try {
			emailNotificationService.enviarNotificacaoCancelamentoConsulta(consultaSalva, consulta.getFuncionario());
			emailNotificationService.enviarNotificacaoCancelamentoConsulta(consultaSalva, consulta.getPsicologo());
		} catch (Exception e) {
			// Log do erro mas não interrompe o cancelamento
			System.err.println("Erro ao enviar e-mails de cancelamento: " + e.getMessage());
		}

		return mapConsulta(consultaSalva, usuarioAutenticado);
	}

	@Transactional
	public ConsultaResponse concluirConsulta(Usuario usuarioAutenticado, UUID consultaId) {
		if (usuarioAutenticado.getRole() != RoleType.PSICOLOGO) {
			throw new BusinessException("Somente psicólogos podem concluir atendimentos");
		}
		ConsultaPsicologica consulta = buscarConsulta(consultaId, usuarioAutenticado);
		if (!consulta.getPsicologo().getId().equals(usuarioAutenticado.getId())) {
			throw new BusinessException("Somente o psicólogo responsável pode concluir o atendimento");
		}
		if (consulta.getStatus() != ConsultaStatus.CONFIRMADA) {
			throw new BusinessException("Somente consultas confirmadas podem ser concluídas");
		}
		consulta.setStatus(ConsultaStatus.CONCLUIDA);
		ConsultaPsicologica consultaSalva = consultaRepository.save(consulta);

		// Enviar e-mails de conclusão de forma assíncrona via RabbitMQ
		try {
			emailNotificationService.enviarNotificacaoConclusaoConsulta(consultaSalva, consulta.getFuncionario());
			emailNotificationService.enviarNotificacaoConclusaoConsulta(consultaSalva, consulta.getPsicologo());
		} catch (Exception e) {
			// Log do erro mas não interrompe a conclusão
			System.err.println("Erro ao enviar e-mails de conclusão: " + e.getMessage());
		}

		return mapConsulta(consultaSalva, usuarioAutenticado);
	}

	@Transactional
	public ConsultaResponse atualizarLinkCall(Usuario usuarioAutenticado, UUID consultaId, LinkCallRequest request) {
		if (usuarioAutenticado.getRole() != RoleType.PSICOLOGO) {
			throw new BusinessException("Somente psicólogos podem atualizar o link da chamada");
		}
		ConsultaPsicologica consulta = buscarConsulta(consultaId, usuarioAutenticado);
		if (!consulta.getPsicologo().getId().equals(usuarioAutenticado.getId())) {
			throw new BusinessException("Somente o psicólogo responsável pode atualizar o link");
		}
		if (consulta.getLocalAtendimento() != LocalAtendimento.ONLINE) {
			throw new BusinessException("Link de chamada só pode ser adicionado para consultas online");
		}
		consulta.setLinkCall(request.linkCall());
		return mapConsulta(consultaRepository.save(consulta), usuarioAutenticado);
	}

	private ConsultaPsicologica buscarConsulta(UUID consultaId, Usuario usuario) {
		UUID empresaId = garantirEmpresa(usuario).getId();
		return consultaRepository.findByIdAndEmpresaId(consultaId, empresaId)
			.orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada"));
	}

	private Usuario buscarUsuarioPorRole(UUID id, UUID empresaId, RoleType role) {
		return usuarioRepository.findByIdAndEmpresaId(id, empresaId)
			.filter(u -> u.getRole() == role)
			.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado para o perfil informado"));
	}

	private Usuario definirResponsavelConfirmacao(RoleType roleSolicitante, Usuario funcionario, Usuario psicologo) {
		// Quando FUNCIONARIO agenda para si mesmo, psicólogo deve confirmar
		// Quando RH agenda para um funcionário, psicólogo deve confirmar
		if (roleSolicitante == RoleType.FUNCIONARIO || roleSolicitante == RoleType.RH) {
			return psicologo;
		}
		// Caso contrário (não deveria acontecer normalmente), funcionário confirma
		return funcionario;
	}

	private Empresa garantirEmpresa(Usuario usuario) {
		if (usuario.getEmpresa() == null) {
			throw new BusinessException("Usuário não vinculado a nenhuma empresa");
		}
		return usuario.getEmpresa();
	}

	private void validarPerfilParaAgendamento(Usuario usuario) {
		if (usuario.getRole() != RoleType.FUNCIONARIO && usuario.getRole() != RoleType.RH) {
			throw new BusinessException("Somente funcionários e RH podem agendar atendimentos");
		}
	}

	private boolean participaDaConsulta(ConsultaPsicologica consulta, Usuario usuario) {
		UUID id = usuario.getId();
		return consulta.getFuncionario().getId().equals(id) || consulta.getPsicologo().getId().equals(id);
	}

	private ConsultaResponse mapConsulta(ConsultaPsicologica consulta, Usuario usuario) {
		boolean aguardandoMinhaConfirmacao = consulta.getAguardandoConfirmacaoDe() != null
			&& consulta.getAguardandoConfirmacaoDe().getId().equals(usuario.getId());
		return new ConsultaResponse(
			consulta.getId(),
			consulta.getStatus(),
			consulta.getDataHoraInicio(),
			consulta.getDataHoraFim(),
			consulta.getLocalAtendimento(),
			consulta.getSala(),
			consulta.getObservacoes(),
			consulta.getJustificativaCancelamento(),
			consulta.getFuncionario().getId(),
			consulta.getFuncionario().getNome(),
			consulta.getPsicologo().getId(),
			consulta.getPsicologo().getNome(),
			aguardandoMinhaConfirmacao,
			consulta.getLinkCall()
		);
	}

	private UsuarioResponse mapUsuario(Usuario usuario) {
		return new UsuarioResponse(
			usuario.getId(),
			usuario.getNome(),
			usuario.getEmail(),
			usuario.getRole(),
			usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null,
			usuario.getCrp()
		);
	}

	private LocalDateTime combinarDataHora(LocalDate data, LocalTime hora) {
		return LocalDateTime.of(data, hora);
	}
}

