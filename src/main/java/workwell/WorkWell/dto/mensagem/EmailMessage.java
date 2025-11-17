package workwell.WorkWell.dto.mensagem;

import java.util.Map;

public record EmailMessage(
	String to,
	String subject,
	String templateName,
	Map<String, Object> variables
) {
}

