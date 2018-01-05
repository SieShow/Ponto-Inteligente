package com.ponto.inteligente.api.services;

import java.util.Optional;

import com.ponto.inteligente.api.entites.Funcionario;

public interface FuncionarioService {

	/**
	 * Insere o funcionário no banco de dados
	 * @param funcionario
	 * @return
	 */
	Funcionario persistir(Funcionario funcionario);

	/**
	 * Busca e retorna um funcionário dado um cpf
	 * @param cpf
	 * @return
	 */
	Optional<Funcionario> buscarPorCpf(String cpf);

	/**
	 * Busca e retorna um funcionário dado um email
	 * @param email
	 * @return
	 */
	Optional<Funcionario> buscarPorEmail(String email);

	/**
	 * Busca e retorna um funcionário dado um id
	 * @param id
	 * @return
	 */
	Optional<Funcionario> buscarPorId(Long id);
}
