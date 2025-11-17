package workwell.WorkWell.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import workwell.WorkWell.config.RabbitMQConfig;
import workwell.WorkWell.dto.mensagem.EmailMessage;
import workwell.WorkWell.service.EmailService;

@Component
public class EmailConsumer {

	private static final Logger logger = LoggerFactory.getLogger(EmailConsumer.class);
	private final EmailService emailService;

	public EmailConsumer(EmailService emailService) {
		this.emailService = emailService;
	}

	@RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
	public void processarEmail(EmailMessage emailMessage) {
		try {
			logger.info("Processando e-mail da fila para: {}", emailMessage.to());
			emailService.enviarEmail(emailMessage);
			logger.info("E-mail processado e enviado com sucesso para: {}", emailMessage.to());
		} catch (Exception e) {
			logger.error("Erro ao processar e-mail da fila para: {}", emailMessage.to(), e);
			throw new RuntimeException("Erro ao processar e-mail da fila", e);
		}
	}
}

