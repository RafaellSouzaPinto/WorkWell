package workwell.WorkWell.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import workwell.WorkWell.entity.enums.ConsultaStatus;
import workwell.WorkWell.entity.enums.LocalAtendimento;
import workwell.WorkWell.entity.enums.SalaAtendimento;

@Entity
@Table(name = "consultas_psicologicas")
public class ConsultaPsicologica {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@JdbcTypeCode(SqlTypes.VARCHAR)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "empresa_id", nullable = false)
	private Empresa empresa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "funcionario_id", nullable = false)
	private Usuario funcionario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "psicologo_id", nullable = false)
	private Usuario psicologo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "criado_por_id", nullable = false)
	private Usuario criadoPor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aguardando_confirmacao_de_id")
	private Usuario aguardandoConfirmacaoDe;

	@Column(name = "data_hora_inicio", nullable = false)
	private LocalDateTime dataHoraInicio;

	@Column(name = "data_hora_fim", nullable = false)
	private LocalDateTime dataHoraFim;

	@Enumerated(EnumType.STRING)
	@Column(name = "local_atendimento", nullable = false, length = 20)
	private LocalAtendimento localAtendimento;

	@Enumerated(EnumType.STRING)
	@Column(name = "sala", length = 10)
	private SalaAtendimento sala;

	@Column(length = 255)
	private String observacoes;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private ConsultaStatus status = ConsultaStatus.PENDENTE_CONFIRMACAO;

	@Column(name = "justificativa_cancelamento", length = 255)
	private String justificativaCancelamento;

	@Column(name = "link_call", length = 500)
	private String linkCall;

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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Usuario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Usuario funcionario) {
		this.funcionario = funcionario;
	}

	public Usuario getPsicologo() {
		return psicologo;
	}

	public void setPsicologo(Usuario psicologo) {
		this.psicologo = psicologo;
	}

	public Usuario getCriadoPor() {
		return criadoPor;
	}

	public void setCriadoPor(Usuario criadoPor) {
		this.criadoPor = criadoPor;
	}

	public Usuario getAguardandoConfirmacaoDe() {
		return aguardandoConfirmacaoDe;
	}

	public void setAguardandoConfirmacaoDe(Usuario aguardandoConfirmacaoDe) {
		this.aguardandoConfirmacaoDe = aguardandoConfirmacaoDe;
	}

	public LocalDateTime getDataHoraInicio() {
		return dataHoraInicio;
	}

	public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
		this.dataHoraInicio = dataHoraInicio;
	}

	public LocalDateTime getDataHoraFim() {
		return dataHoraFim;
	}

	public void setDataHoraFim(LocalDateTime dataHoraFim) {
		this.dataHoraFim = dataHoraFim;
	}

	public LocalAtendimento getLocalAtendimento() {
		return localAtendimento;
	}

	public void setLocalAtendimento(LocalAtendimento localAtendimento) {
		this.localAtendimento = localAtendimento;
	}

	public SalaAtendimento getSala() {
		return sala;
	}

	public void setSala(SalaAtendimento sala) {
		this.sala = sala;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public ConsultaStatus getStatus() {
		return status;
	}

	public void setStatus(ConsultaStatus status) {
		this.status = status;
	}

	public String getJustificativaCancelamento() {
		return justificativaCancelamento;
	}

	public void setJustificativaCancelamento(String justificativaCancelamento) {
		this.justificativaCancelamento = justificativaCancelamento;
	}

	public String getLinkCall() {
		return linkCall;
	}

	public void setLinkCall(String linkCall) {
		this.linkCall = linkCall;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}

