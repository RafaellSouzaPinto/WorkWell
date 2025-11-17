package workwell.WorkWell.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.LocaleResolver;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class PageController {

	private final LocaleResolver localeResolver;

	public PageController(LocaleResolver localeResolver) {
		this.localeResolver = localeResolver;
	}

	@GetMapping("/")
	public String landingPage(HttpServletRequest request, Model model) {
		Locale locale = localeResolver.resolveLocale(request);
		model.addAttribute("locale", locale);
		return "index";
	}

	@GetMapping("/cadastro")
	public String cadastro(HttpServletRequest request, Model model) {
		Locale locale = localeResolver.resolveLocale(request);
		model.addAttribute("locale", locale);
		return "index";
	}

	@GetMapping("/cadastro/empresa")
	public String cadastroEmpresa(HttpServletRequest request, Model model) {
		Locale locale = localeResolver.resolveLocale(request);
		model.addAttribute("locale", locale);
		return "cadastro_empresa";
	}

	@GetMapping("/cadastro/funcionario")
	public String cadastroFuncionario(HttpServletRequest request, Model model) {
		Locale locale = localeResolver.resolveLocale(request);
		model.addAttribute("locale", locale);
		return "cadastro_funcionario";
	}

	@GetMapping("/cadastro/psicologo")
	public String cadastroPsicologo(HttpServletRequest request, Model model) {
		Locale locale = localeResolver.resolveLocale(request);
		model.addAttribute("locale", locale);
		return "cadastro_psicologo";
	}

	@GetMapping("/cadastro/rh")
	public String cadastroRh(HttpServletRequest request, Model model) {
		Locale locale = localeResolver.resolveLocale(request);
		model.addAttribute("locale", locale);
		return "cadastro_rh";
	}

	@GetMapping("/login")
	public String loginPage(HttpServletRequest request, Model model) {
		Locale locale = localeResolver.resolveLocale(request);
		model.addAttribute("locale", locale);
		return "login";
	}

	@GetMapping("/dashboard")
	public String dashboard(HttpServletRequest request, Model model) {
		Locale locale = localeResolver.resolveLocale(request);
		model.addAttribute("locale", locale);
		return "dashboard";
	}

	@GetMapping("/espaco-apoio")
	public String apoioPsicologico(HttpServletRequest request, Model model) {
		Locale locale = localeResolver.resolveLocale(request);
		model.addAttribute("locale", locale);
		return "apoio_psicologico";
	}

	@GetMapping("/dashboard-rh")
	public String dashboardRh(HttpServletRequest request, Model model) {
		Locale locale = localeResolver.resolveLocale(request);
		model.addAttribute("locale", locale);
		return "dashboard_rh";
	}
}

