package com.ponto.inteligente.api.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ponto.inteligente.api.dtos.CadastroPJDTO;
import com.ponto.inteligente.api.entites.Empresa;
import com.ponto.inteligente.api.entites.Funcionario;
import com.ponto.inteligente.api.enums.PerfilEnum;
import com.ponto.inteligente.api.response.Response;
import com.ponto.inteligente.api.services.EmpresaService;
import com.ponto.inteligente.api.services.FuncionarioService;
import com.ponto.inteligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {
	
	private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);
	
	@Autowired
	private EmpresaService empresaService;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	public FuncionarioService getFuncionarioService() {
		return funcionarioService;
	}

	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	
	public CadastroPJController() {
		
	}
	
	@PostMapping
	public ResponseEntity<Response<CadastroPJDTO>> cadastrar(@Valid @RequestBody CadastroPJDTO cadastropjdto,
			BindingResult result) throws NoSuchAlgorithmException{
		log.info("Cadastrando PJ: {}", cadastropjdto.toString());
		
		Response<CadastroPJDTO> response = new Response<CadastroPJDTO>();
		this.validarDadosExistentes(cadastropjdto, result);
		
		Empresa empresa = this.converterDtoParaEmpresa(cadastropjdto);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastropjdto);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados para cadastro PJ : {}", result.getAllErrors());
			result.getAllErrors().forEach(erro -> response.getErrors().add(erro.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persistir(funcionario);
		
		response.setData(this.converterCadastroPJTO(funcionario));
		return ResponseEntity.ok(response);
	}
	
	public void validarDadosExistentes(CadastroPJDTO cadastro, BindingResult result) {
		
		this.funcionarioService.buscarPorCpf(cadastro.getCpf()).ifPresent(func -> 
		result.addError(new ObjectError("funcionario", "funcionario já existente")));
		
		this.empresaService.buscarPorCnpj(cadastro.getCnpj()).ifPresent(emp -> 
		result.addError(new ObjectError("empresa", "empresa já existente")));
		
		this.funcionarioService.buscarPorEmail(cadastro.getEmail()).ifPresent(func -> 
		result.addError(new ObjectError("funcionario", "funcionario já existente")));	
	}
	
	private Empresa converterDtoParaEmpresa(CadastroPJDTO cadastro) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(cadastro.getCnpj());
		empresa.setRazaoSocial(cadastro.getRazaoSocial());
		return empresa;
	}
	
	private Funcionario converterDtoParaFuncionario(CadastroPJDTO cadastro) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCpf(cadastro.getCpf());
		funcionario.setNome(cadastro.getNome());
		funcionario.setEmail(cadastro.getEmail());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastro.getSenha()));
		return funcionario;
		
	}
	
	private CadastroPJDTO converterCadastroPJTO(Funcionario funcionario) {
		CadastroPJDTO cadastro = new CadastroPJDTO();
		cadastro.setId(funcionario.getId());
		cadastro.setNome(funcionario.getNome());
		cadastro.setEmail(funcionario.getEmail());
		cadastro.setCpf(funcionario.getCpf());
		cadastro.setCnpj(funcionario.getEmpresa().getCnpj());
		cadastro.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		
		return cadastro;
	}
}
