package workwell.WorkWell.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import workwell.WorkWell.dto.mensagem.EmailMessage;
import workwell.WorkWell.entity.ConsultaPsicologica;
import workwell.WorkWell.entity.Empresa;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.entity.enums.LocalAtendimento;
import workwell.WorkWell.entity.enums.SalaAtendimento;
import workwell.WorkWell.messaging.EmailProducer;

@Service
public class EmailNotificationService {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
	private final EmailProducer emailProducer;

	public EmailNotificationService(EmailProducer emailProducer) {
		this.emailProducer = emailProducer;
	}

	public void enviarBoasVindasEmpresa(Empresa empresa, Usuario admin) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("nomeAdmin", admin.getNome());
		variables.put("nomeEmpresa", empresa.getNome());
		variables.put("emailAdmin", admin.getEmail());

		EmailMessage emailMessage = new EmailMessage(
			admin.getEmail(),
			"Bem-vindo ao WorkWell - " + empresa.getNome(),
			"email/boas-vindas-empresa",
			variables
		);

		emailProducer.enviarEmail(emailMessage);
	}

	public void enviarNotificacaoAgendamentoConsulta(ConsultaPsicologica consulta, Usuario destinatario) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("nomeDestinatario", destinatario.getNome());
		variables.put("dataHoraInicio", formatarDataHora(consulta.getDataHoraInicio()));
		variables.put("duracao", calcularDuracao(consulta.getDataHoraInicio(), consulta.getDataHoraFim()));
		variables.put("localAtendimento", formatarLocalAtendimento(consulta.getLocalAtendimento()));
		if (consulta.getSala() != null) {
			variables.put("sala", formatarSalaAtendimento(consulta.getSala()));
		}
		variables.put("nomeFuncionario", consulta.getFuncionario().getNome());
		variables.put("nomePsicologo", consulta.getPsicologo().getNome());
		if (consulta.getObservacoes() != null && !consulta.getObservacoes().isBlank()) {
			variables.put("observacoes", consulta.getObservacoes());
		}
		variables.put("aguardandoConfirmacao", consulta.getAguardandoConfirmacaoDe() != null
			&& consulta.getAguardandoConfirmacaoDe().getId().equals(destinatario.getId()));

		EmailMessage emailMessage = new EmailMessage(
			destinatario.getEmail(),
			"Nova Consulta Agendada - WorkWell",
			"email/agendamento-consulta",
			variables
		);

		emailProducer.enviarEmail(emailMessage);
	}

	public void enviarNotificacaoConfirmacaoConsulta(ConsultaPsicologica consulta, Usuario destinatario) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("nomeDestinatario", destinatario.getNome());
		variables.put("dataHoraInicio", formatarDataHora(consulta.getDataHoraInicio()));
		variables.put("duracao", calcularDuracao(consulta.getDataHoraInicio(), consulta.getDataHoraFim()));
		variables.put("localAtendimento", formatarLocalAtendimento(consulta.getLocalAtendimento()));
		if (consulta.getLinkCall() != null && !consulta.getLinkCall().isBlank()) {
			variables.put("linkCall", consulta.getLinkCall());
		}
		variables.put("nomeFuncionario", consulta.getFuncionario().getNome());
		variables.put("nomePsicologo", consulta.getPsicologo().getNome());

		EmailMessage emailMessage = new EmailMessage(
			destinatario.getEmail(),
			"Consulta Confirmada - WorkWell",
			"email/confirmacao-consulta",
			variables
		);

		emailProducer.enviarEmail(emailMessage);
	}

	public void enviarNotificacaoCancelamentoConsulta(ConsultaPsicologica consulta, Usuario destinatario) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("nomeDestinatario", destinatario.getNome());
		variables.put("dataHoraInicio", formatarDataHora(consulta.getDataHoraInicio()));
		variables.put("nomeFuncionario", consulta.getFuncionario().getNome());
		variables.put("nomePsicologo", consulta.getPsicologo().getNome());
		if (consulta.getJustificativaCancelamento() != null
			&& !consulta.getJustificativaCancelamento().isBlank()) {
			variables.put("justificativa", consulta.getJustificativaCancelamento());
		}

		EmailMessage emailMessage = new EmailMessage(
			destinatario.getEmail(),
			"Consulta Cancelada - WorkWell",
			"email/cancelamento-consulta",
			variables
		);

		emailProducer.enviarEmail(emailMessage);
	}

	public void enviarNotificacaoConclusaoConsulta(ConsultaPsicologica consulta, Usuario destinatario) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("nomeDestinatario", destinatario.getNome());
		variables.put("dataHoraInicio", formatarDataHora(consulta.getDataHoraInicio()));
		variables.put("nomeFuncionario", consulta.getFuncionario().getNome());
		variables.put("nomePsicologo", consulta.getPsicologo().getNome());

		EmailMessage emailMessage = new EmailMessage(
			destinatario.getEmail(),
			"Consulta Concluída - WorkWell",
			"email/conclusao-consulta",
			variables
		);

		emailProducer.enviarEmail(emailMessage);
	}

	private String formatarDataHora(LocalDateTime dataHora) {
		return dataHora.format(DATE_TIME_FORMATTER);
	}

	private String calcularDuracao(LocalDateTime inicio, LocalDateTime fim) {
		Duration duracao = Duration.between(inicio, fim);
		long minutos = duracao.toMinutes();
		if (minutos == 60) {
			return "1 hora";
		}
		return minutos + " minutos";
	}

	private String formatarLocalAtendimento(LocalAtendimento local) {
		return switch (local) {
			case PRESENCIAL -> "Presencial";
			case ONLINE -> "Online";
		};
	}

	private String formatarSalaAtendimento(SalaAtendimento sala) {
		return switch (sala) {
			case SALA01 -> "Sala 01";
			case SALA02 -> "Sala 02";
			case SALA03 -> "Sala 03";
		};
	}
}

