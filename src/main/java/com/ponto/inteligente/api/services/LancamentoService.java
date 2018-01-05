package com.ponto.inteligente.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ponto.inteligente.api.entites.Lancamento;

public interface LancamentoService {

	/**
	 * Busca e retorna um lancamento pelo id
	 * @param id
	 * @return
	 */
	Optional<Lancamento> buscarPorId(Long id);

	/**
	 * Busca e retorna um lancamento pelo id do funcionario
	 * @param id
	 * @return
	 */
	Page<Lancamento> buscarPorFuncionarioId(Long id, Pageable page);

	/**
	 * Registra um lancamento no banco de dados
	 * @param lancamento
	 * @return
	 */
	Lancamento persistir(Lancamento lancamento);

	/**
	 * Remove um lancamento dado um id
	 * @param id
	 */
	void remover(Long id);
}
