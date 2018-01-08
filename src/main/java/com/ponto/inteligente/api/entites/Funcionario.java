package com.ponto.inteligente.api.entites;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ponto.inteligente.api.enums.PerfilEnum;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "funcionario")
public class Funcionario implements Serializable {

	static final long serialVersionUID = 352360787567907007L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "nome", nullable = false)
	private String nome;
	@Column(name = "email", nullable = false)
	private String email;
	@Column(name = "senha", nullable = false)
	private String senha;
	@Column(name = "cpf", nullable = false)
	private String cpf;
	@Column(name = "valor_hora", nullable = true)
	private BigDecimal valorhora;
	@Column(name = "qtd_horas_trabalho_dia", nullable = true)
	private Float qtdHorasTrabalhoDia;
	@Column(name = "qtd_horas_almoco", nullable = true)
	private Float qtdHorasAlmoco;
	@Enumerated(EnumType.STRING)
	@Column(name = "perfil", nullable = false)
	private PerfilEnum perfil;
	@Column(name = "data_criacao", nullable = false)
	private Date dataCriacao;
	@Column(name = "data_atualizacao", nullable = false)
	private Date dataAtualizacao;
	@ManyToOne(fetch = FetchType.EAGER)
	private Empresa empresa;
	@OneToMany(mappedBy = "funcionario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private transient List<Lancamento> lancamentos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public BigDecimal getValorhora() {
		return valorhora;
	}

	@Transient
	public Optional<BigDecimal> getValorhoraOpt() {
		return Optional.ofNullable(valorhora);
	}

	@Transient
	public Optional<BigDecimal> getValorHoraOpt() {
		return Optional.ofNullable(this.valorhora);
	}

	public void setValorhora(BigDecimal valorhora) {
		this.valorhora = valorhora;
	}

	public Float getQtdHorasTrabalhoDia() {
		return qtdHorasTrabalhoDia;
	}

	@Transient
	public Optional<Float> getQtdHorasTrabalhoDiaOpt() {
		return Optional.ofNullable(this.qtdHorasTrabalhoDia);
	}

	public void setQtdHorasTrabalhoDia(Float qtdHorasTrabalhoDia) {
		this.qtdHorasTrabalhoDia = qtdHorasTrabalhoDia;
	}

	public Float getQtdHorasAlmoco() {
		return qtdHorasAlmoco;
	}

	@Transient
	public Optional<Float> getQtdHorasAlmocoOpt() {
		return Optional.ofNullable(qtdHorasAlmoco);
	}

	public void setQtdHorasAlmoco(Float qtdHorasAlmoco) {
		this.qtdHorasAlmoco = qtdHorasAlmoco;
	}

	public PerfilEnum getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilEnum perfil) {
		this.perfil = perfil;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	@PreUpdate
	public void preUpdate() {
		this.dataAtualizacao = new Date();
	}

	@PrePersist
	public void prePersist() {
		final Date data = new Date();
		this.dataCriacao = data;
		this.dataAtualizacao = data;
	}

	@Override
	public String toString() {
		return "Funcionario [id = " + this.id + " nome = " + this.nome + " email = " + this.email + " senha = "
				+ this.senha + " cpf = " + this.cpf + " valorHora = " + this.valorhora + " qtdHorasTrabalhoDia = "
				+ this.qtdHorasTrabalhoDia + " qtdHorasAlmoco = " + this.qtdHorasAlmoco + " perfil = " + this.perfil
				+ " dataCriacao = " + this.dataCriacao + " dataAtualizacao = " + this.dataAtualizacao + " empresa = "
				+ this.empresa + "]";
	}
}
