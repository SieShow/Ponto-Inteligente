package com.ponto.inteligente.api.security.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ponto.inteligente.api.entites.Funcionario;
import com.ponto.inteligente.api.services.FuncionarioService;

public class JwtFuncionarioDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private FuncionarioService funcionarioService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorEmail(username);
		if(funcionario.isPresent()) {
			return JwtUserFactory.create(funcionario.get());
		}
		return null;
	}


}
