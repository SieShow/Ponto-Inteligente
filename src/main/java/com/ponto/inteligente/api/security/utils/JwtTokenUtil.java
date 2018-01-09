package com.ponto.inteligente.api.security.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	private static final String CLAIM_KEY_USERNAME = "sub"; // Refente ao email do usuário
	private static final String CLAIM_KEY_ROLE = "role"; // Referente ao perfil
	private static final String CLAIM_KEY_CREATED = "created"; // Definição de quando o usuário foi criado

	@Value("{$jwt.secret}")
	private String secret;

	@Value("{$jwt.expiration}")
	private Long expiration;

	public JwtTokenUtil() {
		// Não implementado
	}

	/**
	 * Obtém o username(email) contidos no token JWT
	 *
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken(String token) {
		try {
			Claims claims = this.getClaimsFromToken(token);
			return claims.getSubject();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Obtém a data de expiração de um token JWT
	 *
	 * @param token
	 * @return
	 */
	public Date getExpirationDateFromToken(String token) {
		try {
			Claims claims = this.getClaimsFromToken(token);
			return claims.getExpiration();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Cria um novo token(refresh)
	 *
	 * @param token
	 * @return
	 */
	public String refreshToken(String token) {
		try {
			Claims claims = this.getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			return this.gerarToken(claims);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Verifica e retorna se um token JWT é válido
	 *
	 * @param token
	 * @return
	 */
	public boolean tokenValido(String token) {
		return !this.tokenExpirado(token);
	}

	/**
	 * Retorna um novo token JWT com base nos dados do usuário
	 *
	 * @param userdetails
	 * @return
	 */
	public String obterToken(UserDetails userdetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, userdetails.getUsername());
		userdetails.getAuthorities().forEach(authority -> claims.put(CLAIM_KEY_ROLE, authority.getAuthority()));
		claims.put(CLAIM_KEY_CREATED, new Date());

		return this.gerarToken(claims);
	}

	/**
	 * Retorna a data de expiração com base na data atual
	 *
	 * @return
	 */
	private Date gerarDataExpiracao() {
		return new Date(System.currentTimeMillis() + expiration * 1000);
	}

	/**
	 * Verifica se um token JWT está expirado
	 *
	 * @param token
	 * @return
	 */
	private boolean tokenExpirado(String token) {
		Date data = this.getExpirationDateFromToken(token);
		if (data == null) {
			return false;
		} else {
			return data.before(new Date());
		}
	}

	/**
	 * Gera um novo token contendo os dados (claims) fornecidos
	 * @param claims
	 * @return
	 */
	private String gerarToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(this.gerarDataExpiracao())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * Realiza o parse do Token JWT para extrair as informações contidas no corpo
	 * dele.
	 *
	 * @param token
	 * @return
	 */
	public Claims getClaimsFromToken(String token) {
		try {
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
	}
}
