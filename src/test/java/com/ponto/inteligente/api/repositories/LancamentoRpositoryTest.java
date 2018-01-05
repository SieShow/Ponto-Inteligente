package com.ponto.inteligente.api.repositories;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ponto.inteligente.api.entites.Empresa;
import com.ponto.inteligente.api.entites.Funcionario;
import com.ponto.inteligente.api.entites.Lancamento;
import com.ponto.inteligente.api.enums.PerfilEnum;
import com.ponto.inteligente.api.enums.TipoEnum;
import com.ponto.inteligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("teste")
public class LancamentoRpositoryTest {

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	private long funcionarioId;

	@Before
	public void setUp() throws Exception{
		Empresa empresa = this.empresaRepository.save(this.obterDadosEmpresa());
		Funcionario funcionario = this.funcionarioRepository.save(this.obterDadosFuncionario(empresa));
		this.funcionarioId = funcionario.getId();

		this.lancamentoRepository.save(this.obterDadosLancamento(funcionario));
		this.lancamentoRepository.save(this.obterDadosLancamento(funcionario));
	}

	@After
	public void tearDown() throws Exception{
		this.lancamentoRepository.deleteAll();
		this.funcionarioRepository.deleteAll();
		this.empresaRepository.deleteAll();
	}

	@Test
	public void buscarLancamentoPorFuncionario() {
		List<Lancamento> lancamento = this.lancamentoRepository.findByFuncionarioId(this.funcionarioId);
		assertEquals(2, lancamento.size());
	}

	@Test
	public void buscarLancamentoPorIdPaginado() {
		PageRequest page = new PageRequest(0, 10);
		Page<Lancamento> lancamento = this.lancamentoRepository.findByFuncionarioId(this.funcionarioId, page);

		assertEquals(2, lancamento.getTotalElements());
	}

	private Lancamento obterDadosLancamento(Funcionario funcionario) {
		Lancamento lancamento = new Lancamento();
		lancamento.setData(new Date());
		lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
		lancamento.setFuncionario(funcionario);
		return lancamento;
	}

	private Funcionario obterDadosFuncionario(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("fulanin");
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setCpf("123456");
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setEmail("fulano@gmail.com");
		funcionario.setEmpresa(empresa);
		return funcionario;
	}

	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa exemplo");
		empresa.setCnpj("5145606543");
		return empresa;
	}
}
