package com.ponto.inteligente.api.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ponto.inteligente.api.dtos.EmpresaDTO;
import com.ponto.inteligente.api.entites.Empresa;
import com.ponto.inteligente.api.response.Response;
import com.ponto.inteligente.api.services.EmpresaService;

@RestController
@RequestMapping("api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

	private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

	@Autowired
	private EmpresaService empresaService;

	public EmpresaController() {
		//Não implementado
	}

	@GetMapping(value = "/cnpj/{cnpj}")
	public ResponseEntity<Response<EmpresaDTO>> buscarPorCnpj(@PathVariable("cnpj") String cnpj){
		log.info("Buscando empresa por CNPJ ", cnpj);
		Response<EmpresaDTO> response = new Response<>();
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cnpj);

		if(!empresa.isPresent()) {
			log.info("Empresa não encontrada para o CNPJ: {}", cnpj);
			response.getErrors().add("Empresa não encontrada para o CNPJ: " + cnpj);
			return ResponseEntity.badRequest().body(response);
		}
		else {
			response.setData(this.converterEmpresaDTO(empresa.get()));
			return ResponseEntity.ok(response);
		}
	}

	private EmpresaDTO converterEmpresaDTO(Empresa empresa) {
		EmpresaDTO empresadto = new EmpresaDTO();
		empresadto.setCnpj(empresa.getCnpj());
		empresadto.setId(empresa.getId());
		empresadto.setRazaoSocial(empresa.getRazaoSocial());
		return empresadto;
	}
}
