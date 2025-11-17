package workwell.WorkWell.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

@Controller
public class LocaleController {

	private final LocaleResolver localeResolver;

	public LocaleController(LocaleResolver localeResolver) {
		this.localeResolver = localeResolver;
	}

	@GetMapping("/change-locale")
	public String changeLocale(
		@RequestParam String lang,
		HttpServletRequest request,
		HttpServletResponse response) {
		
		// Validar idiomas suportados
		Locale locale;
		switch (lang.toLowerCase()) {
			case "en":
			case "en_us":
				locale = new Locale("en", "US");
				break;
			case "es":
			case "es_es":
				locale = new Locale("es", "ES");
				break;
			case "pt":
			case "pt_br":
			default:
				locale = new Locale("pt", "BR");
				break;
		}
		
		localeResolver.setLocale(request, response, locale);
		
		// Redirecionar para a página anterior ou home
		String referer = request.getHeader("Referer");
		if (referer != null && !referer.isEmpty()) {
			return "redirect:" + referer;
		}
		return "redirect:/";
	}
}

