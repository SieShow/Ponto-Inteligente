package com.ponto.inteligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ponto.inteligente.api.entites.Lancamento;
import com.ponto.inteligente.api.repositories.LancamentoRepository;
import com.ponto.inteligente.api.services.LancamentoService;

@Service
public class LancamentoServiceImpl implements LancamentoService {

	private static final Logger log = LoggerFactory.getLogger(LancamentoServiceImpl.class);

	@Autowired
	private LancamentoRepository lancamentoRepositorio;

	@Override
	@Cacheable("lancamentoPorId")
	public Optional<Lancamento> buscarPorId(Long id) {
		log.info("Buscando um lancamento através de um id {}", id);
		return Optional.ofNullable(this.lancamentoRepositorio.findOne(id));
	}

	@Override
	@CachePut("lancamentoPorId")
	public Lancamento persistir(Lancamento lancamento) {
		log.info("Salva um lancamento no banco de dados {}", lancamento);
		return this.lancamentoRepositorio.save(lancamento);
	}

	@Override
	public void remover(Long id) {
		log.info("Remove um lancamento dado um id {}", id);
		this.lancamentoRepositorio.delete(id);
	}

	@Override
	public Page<Lancamento> buscarPorFuncionarioId(Long id, Pageable page) {
		log.info("Buscando um lancamento informando id e pageable {}", id);
		return this.lancamentoRepositorio.findByFuncionarioId(id, page);
	}
}
