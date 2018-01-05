package com.ponto.inteligente.api.services.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ponto.inteligente.api.entites.Empresa;
import com.ponto.inteligente.api.repositories.EmpresaRepository;
import com.ponto.inteligente.api.services.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService{

	private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

	@Autowired
	private EmpresaRepository empresaRepository;

	@Override
	public Optional<Empresa> buscarPorCnpj(String cnpj) {
		log.info("Buscando uma empresa por CNPJ {}", cnpj);
		return Optional.ofNullable(this.empresaRepository.findByCnpj(cnpj));
	}

	@Override
	public Empresa persistir(Empresa empresa) {
		log.info("Cadastrando empresa no banco de dados {}", empresa);
		return this.empresaRepository.save(empresa);
	}
}
