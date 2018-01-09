package com.ponto.inteligente.api.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ponto.inteligente.api.dtos.LancamentoDTO;
import com.ponto.inteligente.api.entites.Funcionario;
import com.ponto.inteligente.api.entites.Lancamento;
import com.ponto.inteligente.api.enums.TipoEnum;
import com.ponto.inteligente.api.response.Response;
import com.ponto.inteligente.api.services.FuncionarioService;
import com.ponto.inteligente.api.services.LancamentoService;

@RestController
@RequestMapping("api/lancamentos")
@CrossOrigin(origins="*")
public class LancamentoController {

	private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Autowired
	private LancamentoService lancamentoService;	
	@Autowired
	private FuncionarioService funcionarioService;
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;
	
	public LancamentoController() {
		//Não implementado
	}
	@GetMapping(value = "/funcionario/{id}")
	public ResponseEntity<Response<Page<LancamentoDTO>>> listaPorFuncionarioId(@PathVariable("id") Long id,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue="DESC") String dir){
		
		log.info("Buscando lançamentos por id do usuário " + id);
		Response<Page<LancamentoDTO>> response = new Response<>();
		
		PageRequest pageRequest = new PageRequest(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(id, pageRequest);
		Page<LancamentoDTO> lancamentodto = lancamentos.map(lancamento -> this.converterLancamentoDto(lancamento));
		response.setData(lancamentodto);
		
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Lista um único lancamento pelo id informado
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Response<LancamentoDTO>> listarPorId(@PathVariable("id") Long id){
		
		log.info("Buscando lancamento por id: " + id);
		Response<LancamentoDTO> response = new Response<>();
		Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);
		
		if(!lancamento.isPresent()) {
			log.info("Lançamento não encontrado para o ID: " + id);
			response.getErrors().add("Lancamento não encontrado para o ID: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		else {
			response.setData(this.converterLancamentoDto(lancamento.get()));
			return ResponseEntity.ok(response);
		}
	}
	
	@PostMapping
	public ResponseEntity<Response<LancamentoDTO>> adicionar(@PathVariable @RequestBody LancamentoDTO lancamentodto, BindingResult result)
			throws ParseException{
		
		log.info("Adicionando lancamento" + lancamentodto.toString());
		Response<LancamentoDTO> response = new Response<>();
		this.validarFuncionario(lancamentodto, result);
		Lancamento lancamento = this.converterDtoParaLancamento(lancamentodto, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando lancamento: ", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		lancamento = this.lancamentoService.persistir(lancamento);
		response.setData(this.converterLancamentoDto(lancamento));
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Response<LancamentoDTO>> atualizar(@PathVariable("id") Long id, @Valid @RequestBody LancamentoDTO lancamentodto,
			BindingResult result){
		log.info("Atualizando lancamentos: " + lancamentodto.toString());
		Response<LancamentoDTO> lancamento = new Response<>();
		
		this.validarFuncionario(lancamentodto, result);
		lancamentodto.setId(Optional.of(id));
		Lancamento lancamento = this.converterLancamentoDto(lancamento);
	}
	
	private void validarFuncionario(LancamentoDTO lancamentodto, BindingResult result) {
		if(lancamentodto.getFuncionarioId() == null) {
			result.addError(new ObjectError("funcionario", "Funcionário não informado"));
			return;
		}
		else {
			log.info("Validando funcionario ID: ", lancamentodto.getFuncionarioId());
			Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentodto.getFuncionarioId());
			if(!funcionario.isPresent()) {
				result.addError(new ObjectError("Funcionario", "Funcionario não encontrado"));
			}
		}
	}
	
	/**
	 * Converte um lancamento para LancamentoDTO
	 * @param lancamento
	 * @return
	 */
	private LancamentoDTO converterLancamentoDto(Lancamento lancamento) {
		LancamentoDTO lancamentodto = new LancamentoDTO();
		lancamentodto.setId(Optional.of(lancamento.getId()));
		lancamentodto.setData(this.dateFormat.format(lancamento.getData()));
		lancamentodto.setTipo(lancamento.getTipo().toString());
		lancamentodto.setDescricao(lancamento.getDescricao());
		lancamentodto.setLocalizacao(lancamento.getLocalizacao());
		lancamentodto.setFuncionarioId(lancamento.getFuncionario().getId());
		
		return lancamentodto;		
	}
	
	private Lancamento converterDtoParaLancamento(LancamentoDTO lancamentoDto, BindingResult result) throws ParseException {
		Lancamento lancamento = new Lancamento();

		if (lancamentoDto.getId().isPresent()) {
			Optional<Lancamento> lanc = this.lancamentoService.buscarPorId(lancamentoDto.getId().get());
			if (lanc.isPresent()) {
				lancamento = lanc.get();
			} else {
				result.addError(new ObjectError("lancamento", "Lançamento não encontrado."));
			}
		} else {
			lancamento.setFuncionario(new Funcionario());
			lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
		}

		lancamento.setDescricao(lancamentoDto.getDescricao());
		lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
		lancamento.setData(this.dateFormat.parse(lancamentoDto.getData()));

		if (EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
			lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
		} else {
			result.addError(new ObjectError("tipo", "Tipo inválido."));
		}

		return lancamento;
	}
	
}
