package workwell.WorkWell.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import workwell.WorkWell.config.RabbitMQConfig;
import workwell.WorkWell.dto.mensagem.EmailMessage;

@Service
public class EmailProducer {

	private static final Logger logger = LoggerFactory.getLogger(EmailProducer.class);
	private final RabbitTemplate rabbitTemplate;

	public EmailProducer(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	public void enviarEmail(EmailMessage emailMessage) {
		try {
			logger.info("Enviando mensagem de e-mail para a fila: {}", emailMessage.to());
			rabbitTemplate.convertAndSend(
				RabbitMQConfig.EMAIL_EXCHANGE,
				RabbitMQConfig.EMAIL_ROUTING_KEY,
				emailMessage
			);
			logger.info("Mensagem de e-mail enviada com sucesso para: {}", emailMessage.to());
		} catch (Exception e) {
			logger.error("Erro ao enviar mensagem de e-mail para a fila: {}", emailMessage.to(), e);
			throw new RuntimeException("Erro ao enviar mensagem de e-mail para a fila", e);
		}
	}
}

