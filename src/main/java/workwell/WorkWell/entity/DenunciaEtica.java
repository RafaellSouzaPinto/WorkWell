package workwell.WorkWell.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "denuncias_eticas")
public class DenunciaEtica {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empresa_id", nullable = false)
	private Empresa empresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "denunciante_id", nullable = false)
	private Usuario denunciante;

	@Column(name = "tipo_denuncia", nullable = false, length = 100)
	private String tipoDenuncia; // Assédio moral, Assédio sexual, Racismo, etc.

	@Column(name = "descricao", nullable = false, length = 4000)
	private String descricao;

	@Column(name = "envolvidos", length = 500)
	private String envolvidos; // Nomes ou cargos das pessoas envolvidas

	@Column(name = "local_ocorrencia", length = 255)
	private String localOcorrencia;

	@Column(name = "data_ocorrencia")
	private LocalDateTime dataOcorrencia;

	@Column(name = "anexos", length = 2000)
	private String anexos; // URLs ou referências a arquivos anexados

	@Column(name = "status", nullable = false, length = 50)
	private String status = "PENDENTE"; // PENDENTE, EM_ANALISE, RESOLVIDA, ARQUIVADA

	@Column(name = "observacoes_admin", length = 2000)
	private String observacoesAdmin; // Observações do admin sobre a denúncia

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = this.createdAt;
	}

	@PreUpdate
	void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Usuario getDenunciante() {
		return denunciante;
	}

	public void setDenunciante(Usuario denunciante) {
		this.denunciante = denunciante;
	}

	public String getTipoDenuncia() {
		return tipoDenuncia;
	}

	public void setTipoDenuncia(String tipoDenuncia) {
		this.tipoDenuncia = tipoDenuncia;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getEnvolvidos() {
		return envolvidos;
	}

	public void setEnvolvidos(String envolvidos) {
		this.envolvidos = envolvidos;
	}

	public String getLocalOcorrencia() {
		return localOcorrencia;
	}

	public void setLocalOcorrencia(String localOcorrencia) {
		this.localOcorrencia = localOcorrencia;
	}

	public LocalDateTime getDataOcorrencia() {
		return dataOcorrencia;
	}

	public void setDataOcorrencia(LocalDateTime dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}

	public String getAnexos() {
		return anexos;
	}

	public void setAnexos(String anexos) {
		this.anexos = anexos;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getObservacoesAdmin() {
		return observacoesAdmin;
	}

	public void setObservacoesAdmin(String observacoesAdmin) {
		this.observacoesAdmin = observacoesAdmin;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}

