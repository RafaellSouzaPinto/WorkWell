package workwell.WorkWell.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import workwell.WorkWell.entity.Usuario;
import workwell.WorkWell.entity.enums.RoleType;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

	boolean existsByEmail(String email);

	Optional<Usuario> findByEmail(String email);

	Optional<Usuario> findByIdAndEmpresaId(UUID id, UUID empresaId);

	List<Usuario> findByEmpresaIdAndRoleOrderByNomeAsc(UUID empresaId, RoleType role);
}

