package com.ponto.inteligente.api.security.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class JwtAuthenticationDto {

	@NotEmpty(message = "Email não pode ser vazio")
	@Email(message = "Email inválido")
	private String email;
	
	@NotEmpty(message = "Senha não pode ser vazia")
	private String senha;
	
	public JwtAuthenticationDto() {
		//Não implementado
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
	
	
}
