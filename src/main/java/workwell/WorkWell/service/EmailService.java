package workwell.WorkWell.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import workwell.WorkWell.dto.mensagem.EmailMessage;

@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

	public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
	}

	public void enviarEmail(EmailMessage emailMessage) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(
				message,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name()
			);

			helper.setTo(emailMessage.to());
			helper.setSubject(emailMessage.subject());
			helper.setFrom("noreply@workwell.com.br");

			Context context = new Context();
			if (emailMessage.variables() != null) {
				emailMessage.variables().forEach(context::setVariable);
			}

			String htmlContent = templateEngine.process(emailMessage.templateName(), context);
			helper.setText(htmlContent, true);

			mailSender.send(message);
			logger.info("E-mail enviado com sucesso para: {}", emailMessage.to());
		} catch (MessagingException e) {
			logger.error("Erro ao enviar e-mail para: {}", emailMessage.to(), e);
			throw new RuntimeException("Erro ao enviar e-mail", e);
		}
	}
}

