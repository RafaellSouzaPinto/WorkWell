package workwell.WorkWell;

import jakarta.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WorkWellApplication {

	private static final ZoneId SAO_PAULO_TIMEZONE = ZoneId.of("America/Sao_Paulo");

	@PostConstruct
	public void init() {
		// Configurar timezone padrão da JVM para São Paulo
		TimeZone.setDefault(TimeZone.getTimeZone(SAO_PAULO_TIMEZONE));
		System.setProperty("user.timezone", "America/Sao_Paulo");
	}

	public static void main(String[] args) {
		// Garantir timezone antes de iniciar a aplicação
		TimeZone.setDefault(TimeZone.getTimeZone(SAO_PAULO_TIMEZONE));
		System.setProperty("user.timezone", "America/Sao_Paulo");
		SpringApplication.run(WorkWellApplication.class, args);
	}

}
