package workwell.WorkWell.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import workwell.WorkWell.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {

	boolean existsByCnpj(String cnpj);

	Optional<Empresa> findByToken(String token);
}

