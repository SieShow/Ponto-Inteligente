package com.ponto.inteligente.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ponto.inteligente.api.entites.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

	@Transactional(readOnly = true)
	Empresa findByCnpj(String cnpj);
}
