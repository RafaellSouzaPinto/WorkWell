package workwell.WorkWell.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import workwell.WorkWell.dto.denuncia.DenunciaEticaCreateRequest;
import workwell.WorkWell.dto.denuncia.DenunciaEticaResponse;
import workwell.WorkWell.dto.denuncia.DenunciaEticaUpdateStatusRequest;
import workwell.WorkWell.entity.DenunciaEtica;
import workwell.WorkWell.entity.Empresa;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.entity.enums.RoleType;
import workwell.WorkWell.exception.BusinessException;
import workwell.WorkWell.exception.ResourceNotFoundException;
import workwell.WorkWell.repository.DenunciaEticaRepository;

@Service
public class DenunciaEticaService {

	private final DenunciaEticaRepository denunciaEticaRepository;

	public DenunciaEticaService(DenunciaEticaRepository denunciaEticaRepository) {
		this.denunciaEticaRepository = denunciaEticaRepository;
	}

	@Transactional
	public DenunciaEticaResponse criarDenuncia(Usuario usuario, DenunciaEticaCreateRequest request) {
		Empresa empresa = garantirEmpresa(usuario);

		DenunciaEtica denuncia = new DenunciaEtica();
		denuncia.setEmpresa(empresa);
		denuncia.setDenunciante(usuario);
		denuncia.setTipoDenuncia(request.tipoDenuncia());
		denuncia.setDescricao(request.descricao());
		denuncia.setEnvolvidos(request.envolvidos());
		denuncia.setLocalOcorrencia(request.localOcorrencia());
		denuncia.setDataOcorrencia(request.dataOcorrencia());
		denuncia.setAnexos(request.anexos());
		denuncia.setStatus("PENDENTE");

		denuncia = denunciaEticaRepository.save(denuncia);
		return mapToResponse(denuncia);
	}

	public List<DenunciaEticaResponse> listarDenuncias(Usuario usuario) {
		if (usuario.getRole() != RoleType.ADMIN) {
			throw new BusinessException("Somente ADMIN pode visualizar denúncias");
		}

		Empresa empresa = garantirEmpresa(usuario);
		List<DenunciaEtica> denuncias = denunciaEticaRepository.findByEmpresaIdOrderByCreatedAtDesc(empresa.getId());
		return denuncias.stream()
			.map(this::mapToResponse)
			.collect(Collectors.toList());
	}

	public List<DenunciaEticaResponse> listarDenunciasPorStatus(Usuario usuario, String status) {
		if (usuario.getRole() != RoleType.ADMIN) {
			throw new BusinessException("Somente ADMIN pode visualizar denúncias");
		}

		Empresa empresa = garantirEmpresa(usuario);
		List<DenunciaEtica> denuncias = denunciaEticaRepository.findByEmpresaIdAndStatusOrderByCreatedAtDesc(
			empresa.getId(),
			status
		);
		return denuncias.stream()
			.map(this::mapToResponse)
			.collect(Collectors.toList());
	}

	@Transactional
	public DenunciaEticaResponse atualizarStatus(
		Usuario usuario,
		UUID denunciaId,
		DenunciaEticaUpdateStatusRequest request
	) {
		if (usuario.getRole() != RoleType.ADMIN) {
			throw new BusinessException("Somente ADMIN pode atualizar status de denúncias");
		}

		Empresa empresa = garantirEmpresa(usuario);
		DenunciaEtica denuncia = denunciaEticaRepository.findById(denunciaId)
			.orElseThrow(() -> new ResourceNotFoundException("Denúncia não encontrada"));

		if (!denuncia.getEmpresa().getId().equals(empresa.getId())) {
			throw new BusinessException("Denúncia não pertence à sua empresa");
		}

		denuncia.setStatus(request.status());
		if (request.observacoesAdmin() != null) {
			denuncia.setObservacoesAdmin(request.observacoesAdmin());
		}

		denuncia = denunciaEticaRepository.save(denuncia);
		return mapToResponse(denuncia);
	}

	public Long contarDenunciasPendentes(Usuario usuario) {
		if (usuario.getRole() != RoleType.ADMIN) {
			throw new BusinessException("Somente ADMIN pode visualizar estatísticas de denúncias");
		}

		Empresa empresa = garantirEmpresa(usuario);
		return denunciaEticaRepository.contarDenunciasPendentes(empresa.getId());
	}

	private DenunciaEticaResponse mapToResponse(DenunciaEtica denuncia) {
		return new DenunciaEticaResponse(
			denuncia.getId(),
			denuncia.getEmpresa().getId(),
			denuncia.getDenunciante().getId(),
			denuncia.getDenunciante().getNome(),
			denuncia.getDenunciante().getEmail(),
			denuncia.getTipoDenuncia(),
			denuncia.getDescricao(),
			denuncia.getEnvolvidos(),
			denuncia.getLocalOcorrencia(),
			denuncia.getDataOcorrencia(),
			denuncia.getAnexos(),
			denuncia.getStatus(),
			denuncia.getObservacoesAdmin(),
			denuncia.getCreatedAt(),
			denuncia.getUpdatedAt()
		);
	}

	private Empresa garantirEmpresa(Usuario usuario) {
		if (usuario.getEmpresa() == null) {
			throw new BusinessException("Usuário não vinculado a nenhuma empresa");
		}
		return usuario.getEmpresa();
	}
}

