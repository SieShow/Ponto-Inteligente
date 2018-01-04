package com.ponto.inteligente.api.entites;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "empresa")
public class Empresa implements Serializable {

	private static final long serialVersionUID = -6909795819409836930L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "razao_social", nullable = false)
	private String razaoSocial;

	@Column(name = "cnpj", nullable = false)
	private String cnpj;

	@Column(name = "data_criacao", nullable = false)
	private Date dataCriacao;

	@Column(name = "data_atualizacao", nullable = false)
	private Date dataAtualizacao;

	@OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private transient List<Funcionario> funcionarios;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
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

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	@PreUpdate
	public void preUpdate() {
		this.dataAtualizacao = new Date();
	}

	@PrePersist
	public void prePersis() {
		final Date data = new Date();
		this.dataAtualizacao = data;
		this.dataCriacao = data;
	}

	@Override
	public String toString() {
		return "Empresa [id = " + this.id + ", razaoSocial = " + this.razaoSocial + " cnpj = " + this.cnpj
				+ " datacriacao = " + this.dataCriacao + " dataAtualizacao = " + this.dataAtualizacao + "]";

	}
}
