package com.ponto.inteligente.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

	private PasswordUtils() {

	}
	private static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);

	/**
	 * Gera uma senha encriptada com BCrypt
	 * @param senha
	 * @return
	 */
	public static String gerarBCrypt(String senha) {
		if(senha == null) {
			return senha;
		}

		log.info("Gerando hash com o BCrypt");
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		return bcrypt.encode(senha);
	}
}
