package workwell.WorkWell.service;

import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import workwell.WorkWell.dto.empresa.EmpresaCadastroRequest;
import workwell.WorkWell.dto.empresa.EmpresaResponse;
import workwell.WorkWell.entity.Empresa;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.entity.enums.RoleType;
import workwell.WorkWell.exception.BusinessException;
import workwell.WorkWell.exception.ResourceNotFoundException;
import workwell.WorkWell.repository.EmpresaRepository;
import workwell.WorkWell.repository.UsuarioRepository;

@Service
public class EmpresaService {

	private final EmpresaRepository empresaRepository;
	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailNotificationService emailNotificationService;

	public EmpresaService(EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository,
		PasswordEncoder passwordEncoder, EmailNotificationService emailNotificationService) {
		this.empresaRepository = empresaRepository;
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.emailNotificationService = emailNotificationService;
	}

	@Transactional
	public EmpresaResponse cadastrar(EmpresaCadastroRequest request) {
		if (empresaRepository.existsByCnpj(request.cnpj())) {
			throw new BusinessException("Já existe uma empresa cadastrada com este CNPJ");
		}

		if (usuarioRepository.existsByEmail(request.email())) {
			throw new BusinessException("Já existe um usuário com este email");
		}

		Empresa empresa = new Empresa();
		empresa.setNome(request.nome());
		empresa.setCnpj(request.cnpj());
		empresa.setDescricao(request.descricao());
		empresa.setToken(gerarTokenUnico());

		Empresa empresaSalva = empresaRepository.save(empresa);

		Usuario admin = new Usuario();
		admin.setNome(request.nome());
		admin.setEmail(request.email());
		admin.setSenha(passwordEncoder.encode(request.senha()));
		admin.setRole(RoleType.ADMIN);
		admin.setEmpresa(empresaSalva);

		Usuario adminSalvo = usuarioRepository.save(admin);
		empresaSalva.setAdmin(adminSalvo);
		empresaRepository.save(empresaSalva);

		// Enviar e-mail de boas-vindas de forma assíncrona via RabbitMQ
		try {
			emailNotificationService.enviarBoasVindasEmpresa(empresaSalva, adminSalvo);
		} catch (Exception e) {
			// Log do erro mas não interrompe o cadastro
			System.err.println("Erro ao enviar e-mail de boas-vindas: " + e.getMessage());
		}

		return new EmpresaResponse(
			empresaSalva.getId(),
			empresaSalva.getNome(),
			empresaSalva.getCnpj(),
			empresaSalva.getDescricao(),
			empresaSalva.getToken(),
			adminSalvo.getEmail()
		);
	}

	public Empresa buscarPorToken(String token) {
		return empresaRepository.findByToken(token)
			.orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada para o token informado"));
	}

	private String gerarTokenUnico() {
		String token;
		do {
			token = UUID.randomUUID().toString();
		} while (empresaRepository.findByToken(token).isPresent());
		return token;
	}
}

