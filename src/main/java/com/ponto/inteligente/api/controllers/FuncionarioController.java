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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ponto.inteligente.api.dtos.FuncionarioDTO;
import com.ponto.inteligente.api.entites.Funcionario;
import com.ponto.inteligente.api.response.Response;
import com.ponto.inteligente.api.services.FuncionarioService;
import com.ponto.inteligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/funcionario")
@CrossOrigin(origins = "*")
public class FuncionarioController {

	private static final Logger log = LoggerFactory.getLogger(FuncionarioController.class);

	@Autowired
	private FuncionarioService funcionarioService;

	public FuncionarioController() {
		// Não implementado
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<FuncionarioDTO>> atualizar(@PathVariable("id") Long id, BindingResult result,
			@Valid @RequestBody FuncionarioDTO funcionariodto) {

		Response<FuncionarioDTO> response = new Response<>();
		log.info("Atualizando registro do funcionário: ", funcionariodto.toString());
		Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(id);

		if (!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionário não encontrado"));
		} else {
			this.atualizarDadosFuncionario(funcionario.get(), funcionariodto, result);

			if (result.hasErrors()) {
				log.error("Erro validando funcionário: {}", result.getAllErrors());
				result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			} else {
				this.funcionarioService.persistir(funcionario.get());
				response.setData(this.converterFuncionarioDto(funcionario.get()));
			}
		}
		return null;
	}

	public void atualizarDadosFuncionario(Funcionario funcionario, FuncionarioDTO funcionariodto,
			BindingResult result) {
		funcionario.setNome(funcionariodto.getNome());

		if (!funcionario.getEmail().equals(funcionariodto.getEmail())) {
			this.funcionarioService.buscarPorEmail(funcionariodto.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("email", "Email já existente")));
		}

		funcionario.setQtdHorasAlmoco(null);
		funcionariodto.getQtdHorasAlmoco()
				.ifPresent(qtdhoras -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdhoras)));

		funcionario.setQtdHorasTrabalhoDia(null);
		funcionariodto.getQtdHorasTrabalhoDia()
				.ifPresent(qtdhorasdia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdhorasdia)));

		funcionario.setValorhora(null);
		funcionariodto.getValorHora().ifPresent(valor -> funcionario.setValorhora(new BigDecimal(valor)));

		if (funcionariodto.getSenha().isPresent()) {
			funcionario.setSenha(PasswordUtils.gerarBCrypt(funcionariodto.getSenha().get()));
		}
	}

	public FuncionarioDTO converterFuncionarioDto(Funcionario funcionario) {
		FuncionarioDTO funcionariodto = new FuncionarioDTO();
		funcionariodto.setId(funcionario.getId());
		funcionariodto.setEmail(funcionario.getEmail());
		funcionariodto.setNome(funcionario.getNome());
		funcionario.getQtdHorasAlmocoOpt()
				.ifPresent(qtdhorasa -> funcionariodto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdhorasa))));
		funcionario.getQtdHorasTrabalhoDiaOpt()
		.ifPresent(qtdhorastrabalho -> funcionariodto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdhorastrabalho))));
		funcionario.getValorHoraOpt()
		.ifPresent(valor -> funcionariodto.setValorHora(Optional.of(valor.toString())));

		return funcionariodto;
	}
}
