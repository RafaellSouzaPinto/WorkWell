package workwell.WorkWell.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import workwell.WorkWell.dto.empresa.EmpresaCadastroRequest;
import workwell.WorkWell.dto.empresa.EmpresaResponse;
import workwell.WorkWell.service.EmpresaService;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

	private final EmpresaService empresaService;

	public EmpresaController(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	@PostMapping
	public ResponseEntity<EmpresaResponse> cadastrar(@Valid @RequestBody EmpresaCadastroRequest request) {
		EmpresaResponse response = empresaService.cadastrar(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}

