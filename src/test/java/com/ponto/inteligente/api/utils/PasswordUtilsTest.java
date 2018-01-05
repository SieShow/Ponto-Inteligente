package com.ponto.inteligente.api.utils;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtilsTest {

	private static final String SENHA = "123lucas";
	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Test
	public void testeSenhaNulla() throws Exception{
		assertNull(PasswordUtils.gerarBCrypt(null));
	}

	@Test
	public void testarGerarHashSenha() throws Exception{
		String hash = PasswordUtils.gerarBCrypt(SENHA);

		assertTrue(encoder.matches(SENHA, hash));
	}
}
