package com.ponto.inteligente.api.dtos;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public class CadastroPJDTO {

	private Long id;
	@NotEmpty(message = "Nome não pode ser vazio")
	@Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
	private String nome;

	@NotEmpty(message = "Email não pode ser vazio")
	@Length(min = 5, max = 200, message ="Email deve conter entre 5 e 200 caracteres")
	@Email(message = "Email inválido")
	private String email;

	@NotEmpty(message = "Senha não pode ser vazia")
	private String senha;

	@NotEmpty(message = "Cpf não pode ser vazio")
	@CPF(message = "CPF inválido")
	private String cpf;

	@NotEmpty(message = "Razão social não pode ser vazio")
	@Length(min = 5, max = 200, message = "Razão social deve conter entre 5 e 200 caracteres")
	private String razaoSocial;

	@NotEmpty(message = "CNPJ não pode ser vazio")
	@CNPJ(message = "CPNJ inválido")
	private String cnpj;

	public CadastroPJDTO() {

	}

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

	@Override
	public String toString() {
		return "CadastroPJDTO [ id = " + this.id + " nome = " + this.nome + " email = " + this.email + " senha = "
	+ this.senha + " cpf = " + this.cpf + " razaoSocial = " + this.razaoSocial + " cnjp = " + this.cnpj + "]";
	}
}
