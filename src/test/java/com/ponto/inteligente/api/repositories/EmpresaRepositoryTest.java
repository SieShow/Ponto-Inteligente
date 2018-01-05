package com.ponto.inteligente.api.repositories;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ponto.inteligente.api.entites.Empresa;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositoryTest {

	@Autowired
	private EmpresaRepository empresaRepository;

	private static final String CNPJ = "51463645000100";

	@Before
	public void setUp() throws Exception {
		Empresa empresaTeste = new Empresa();
		empresaTeste.setRazaoSocial("Empresa teste");
		empresaTeste.setCnpj(CNPJ);
		this.empresaRepository.save(empresaTeste);
	}

	@After
	public final void tearDown() {
		this.empresaRepository.deleteAll();
	}

	@Test
	public void testBuscarPorCnpj() {
		Empresa empresa = this.empresaRepository.findByCnpj(CNPJ);

		assertEquals(CNPJ, empresa.getCnpj());
	}
}
