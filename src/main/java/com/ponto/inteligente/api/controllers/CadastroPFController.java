package com.ponto.inteligente.api.controllers;

import java.math.BigDecimal;
import java.util.Optional;

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

import com.ponto.inteligente.api.dtos.CadastroPFDTO;
import com.ponto.inteligente.api.entites.Empresa;
import com.ponto.inteligente.api.entites.Funcionario;
import com.ponto.inteligente.api.enums.PerfilEnum;
import com.ponto.inteligente.api.response.Response;
import com.ponto.inteligente.api.services.EmpresaService;
import com.ponto.inteligente.api.services.FuncionarioService;
import com.ponto.inteligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("api/cadastro-pf")
@CrossOrigin(origins="*")
public class CadastroPFController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);

	@Autowired
	private FuncionarioService funcionarioService;
	@Autowired EmpresaService empresaService;

	public CadastroPFController() {
		//Construtor não implementado
	}

	@PostMapping
	public ResponseEntity<Response<CadastroPFDTO>> cadastrar(@Valid @RequestBody CadastroPFDTO cadastropf, BindingResult result){

		log.info("Cadastrando pessoa física {0}", cadastropf.toString());
		Response<CadastroPFDTO> response = new Response<>();
		this.validarDadosExistentes(cadastropf, result);

		if(result.hasErrors()) {
			log.error("Erro validando dados para o cadastro de PF " + result.getAllErrors());
			result.getAllErrors().forEach(erro -> response.getErrors().add(erro.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastropf);
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastropf.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persistir(funcionario);
		response.setData(this.converterCadastroPJTO(funcionario));

		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a empresa está cadastrada e se o funcionário não existe no banco de dados
	 * @param cadastro
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPFDTO cadastro, BindingResult result) {
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastro.getCnpj());
		if(!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não cadastrada"));
		}

		this.funcionarioService.buscarPorCpf(cadastro.getCpf()).ifPresent(error ->
		result.addError(new ObjectError("funcionario", "CPF já cadastrado")));

		this.funcionarioService.buscarPorEmail(cadastro.getEmail()).ifPresent(error ->
		result.addError(new ObjectError("funcionario", "Email já cadastrado")));
	}

	/**
	 * Converte os dados do DTO para Funcionario
	 * @param cadastro
	 * @return
	 */
	private Funcionario converterDtoParaFuncionario(CadastroPFDTO cadastro) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCpf(cadastro.getCpf());
		funcionario.setNome(cadastro.getNome());
		funcionario.setEmail(cadastro.getEmail());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastro.getSenha()));
		cadastro.getQtdHorasAlmoco().ifPresent(qtdhoras -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdhoras)));
		cadastro.getQtdHorasTrabalhoDia().ifPresent(qtdhorastrabalho -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdhorastrabalho)));
		cadastro.getValorHora().ifPresent(qtdvalorhora -> funcionario.setValorhora(new BigDecimal(qtdvalorhora)));
		return funcionario;
	}

	private CadastroPFDTO converterCadastroPJTO(Funcionario funcionario) {
		CadastroPFDTO cadastro = new CadastroPFDTO();
		cadastro.setId(funcionario.getId());
		cadastro.setNome(funcionario.getNome());
		cadastro.setEmail(funcionario.getEmail());
		cadastro.setCpf(funcionario.getCpf());
		cadastro.setCnpj(funcionario.getEmpresa().getCnpj());
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(qtdtrabalhodia -> cadastro.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdtrabalhodia))));
		funcionario.getQtdHorasAlmocoOpt().ifPresent(qtdhorastrabalho -> cadastro.setQtdHorasAlmoco(Optional.of(Float.toString(qtdhorastrabalho))));
		funcionario.getValorHoraOpt().ifPresent(qtdvalorhora -> cadastro.setValorHora(Optional.ofNullable(qtdvalorhora.toString())));
		return cadastro;
	}
}
