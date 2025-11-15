package workwell.WorkWell.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping({"/", "/cadastro/empresa"})
	public String cadastroEmpresa() {
		return "cadastro_empresa";
	}
}

