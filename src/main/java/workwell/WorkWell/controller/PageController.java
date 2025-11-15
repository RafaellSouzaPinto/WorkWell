package workwell.WorkWell.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping("/")
	public String landingPage() {
		return "index";
	}

	@GetMapping("/empresa")
	public String cadastroEmpresa() {
		return "cadastro_empresa";
	}

	@GetMapping("/funcionario")
	public String cadastroFuncionario() {
		return "cadastro_funcionario";
	}

	@GetMapping("/psicologo")
	public String cadastroPsicologo() {
		return "cadastro_psicologo";
	}

	@GetMapping("/rh")
	public String cadastroRh() {
		return "cadastro_rh";
	}
}

