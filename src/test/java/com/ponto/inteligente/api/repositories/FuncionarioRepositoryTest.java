package com.ponto.inteligente.api.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ponto.inteligente.api.entites.Empresa;
import com.ponto.inteligente.api.entites.Funcionario;
import com.ponto.inteligente.api.enums.PerfilEnum;
import com.ponto.inteligente.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("teste")
public class FuncionarioRepositoryTest {

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private EmpresaRepository empresaRepository;

	private static final String CPF = "13649180642";
	private static final String EMAIL = "lucasgsm88@gmail.com";

	@Before
	public void setUp() throws Exception{
		Empresa empresa = this.empresaRepository.save(this.obterDadosEmpresa());
		this.funcionarioRepository.save(this.obterDadosFuncionario(empresa));
	}

	@After
	public final void tearDown() {
		this.funcionarioRepository.deleteAll();
		this.empresaRepository.deleteAll();
	}

	@Test
	public void testBuscarFuncionarioPorEmail() {
		Funcionario funcionario = this.funcionarioRepository.findByEmail(EMAIL);

		assertEquals(EMAIL, funcionario.getEmail());
	}

	@Test
	public void testBuscarFuncionarioPorCpf() {
		Funcionario funcionario = this.funcionarioRepository.findByCpf(CPF);

		assertEquals(CPF, funcionario.getCpf());
	}

	@Test
	public void testBuscarFuncionarioPorCpfEemail() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, EMAIL);

		assertNotNull(funcionario);
	}

	@Test
	public void testBuscarFuncionarioPorCpfEemailInvalido() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, "batata@gmail.com");

		assertNotNull(funcionario);
	}

	@Test
	public void testBuscarFuncionarioPorCpfInvalidoEemail() {
		Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail("123", EMAIL);

		assertNotNull(funcionario);
	}

	private Funcionario obterDadosFuncionario(Empresa empresa) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("fulanin");
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setCpf(CPF);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setEmail(EMAIL);
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
