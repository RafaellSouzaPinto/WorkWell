package workwell.WorkWell.service;

import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import workwell.WorkWell.dto.usuario.UsuarioCadastroRequest;
import workwell.WorkWell.dto.usuario.UsuarioResponse;
import workwell.WorkWell.entity.Empresa;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.entity.enums.RoleType;
import workwell.WorkWell.exception.BusinessException;
import workwell.WorkWell.repository.UsuarioRepository;

@Service
public class UsuarioService {

	private final UsuarioRepository usuarioRepository;
	private final EmpresaService empresaService;
	private final PasswordEncoder passwordEncoder;

	public UsuarioService(UsuarioRepository usuarioRepository, EmpresaService empresaService,
		PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.empresaService = empresaService;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public UsuarioResponse cadastrar(UsuarioCadastroRequest request) {
		if (request.tipoUsuario() == RoleType.ADMIN) {
			throw new BusinessException("Somente a empresa pode ser administradora");
		}

		if (usuarioRepository.existsByEmail(request.email())) {
			throw new BusinessException("Já existe um usuário com este email");
		}

		Empresa empresa = empresaService.buscarPorToken(request.tokenEmpresa());

		if (request.tipoUsuario() == RoleType.PSICOLOGO && !StringUtils.hasText(request.crp())) {
			throw new BusinessException("Psicólogos devem informar o número do CRP");
		}

		Usuario usuario = new Usuario();
		usuario.setNome(request.nome());
		usuario.setEmail(request.email());
		usuario.setSenha(passwordEncoder.encode(request.senha()));
		usuario.setRole(request.tipoUsuario());
		usuario.setCrp(request.crp());
		usuario.setEmpresa(empresa);

		Usuario salvo = usuarioRepository.save(usuario);

		UUID empresaId = empresa.getId();
		return new UsuarioResponse(
			salvo.getId(),
			salvo.getNome(),
			salvo.getEmail(),
			salvo.getRole(),
			empresaId,
			salvo.getCrp()
		);
	}

	public UsuarioResponse dadosAtuais(Usuario usuario) {
		UUID empresaId = usuario.getEmpresa() != null ? usuario.getEmpresa().getId() : null;
		return new UsuarioResponse(
			usuario.getId(),
			usuario.getNome(),
			usuario.getEmail(),
			usuario.getRole(),
			empresaId,
			usuario.getCrp()
		);
	}
}

