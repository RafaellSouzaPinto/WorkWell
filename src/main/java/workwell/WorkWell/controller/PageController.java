package workwell.WorkWell.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping("/")
	public String landingPage() {
		return "index";
	}

	@GetMapping("/cadastro")
	public String cadastro() {
		return "index";
	}

	@GetMapping("/cadastro/empresa")
	public String cadastroEmpresa() {
		return "cadastro_empresa";
	}

	@GetMapping("/cadastro/funcionario")
	public String cadastroFuncionario() {
		return "cadastro_funcionario";
	}

	@GetMapping("/cadastro/psicologo")
	public String cadastroPsicologo() {
		return "cadastro_psicologo";
	}

	@GetMapping("/cadastro/rh")
	public String cadastroRh() {
		return "cadastro_rh";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}

	@GetMapping("/espaco-apoio")
	public String apoioPsicologico() {
		return "apoio_psicologico";
	}

	@GetMapping("/dashboard-rh")
	public String dashboardRh() {
		return "dashboard_rh";
	}
}

