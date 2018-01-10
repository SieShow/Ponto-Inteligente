package com.ponto.inteligente.api.security.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ponto.inteligente.api.response.Response;
import com.ponto.inteligente.api.security.dto.JwtAuthenticationDto;
import com.ponto.inteligente.api.security.dto.TokenDto;
import com.ponto.inteligente.api.security.utils.JwtTokenUtil;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
	private static final String TOKEN_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtToken;
	
	@Autowired
	private UserDetailsService userDetailService;

	/**
	 * Gera um token de acesso
	 * @param authenticationdto
	 * @param result
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Response<TokenDto>> gerarTokenJwt(@Valid @RequestBody JwtAuthenticationDto authenticationdto,
			BindingResult result){
		Response<TokenDto> response = new Response<>();
		
		if(result.hasErrors()) {
			log.error("Erro no lancamento do token." + result.getAllErrors());
			result.getAllErrors().forEach(erro -> response.getErrors().add(erro.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		else {
			log.info("Gerando token para o email: " + authenticationdto.getEmail());
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationdto.getEmail(), authenticationdto.getSenha()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			UserDetails userDetails = userDetailService.loadUserByUsername(authenticationdto.getEmail());
			String token = jwtToken.obterToken(userDetails);
			response.setData(new TokenDto(token));
			
			return ResponseEntity.ok(response);
		}	
	}
	
	/**
	 * Gera um novo token com uma nova data de expiração
	 * @param request
	 * @return
	 */
	@PostMapping("/refresh")
	public ResponseEntity<Response<TokenDto>> gerarRefreshTokenJwt(HttpServletRequest request){
		log.info("Gerando refresh token");
		Response<TokenDto> response = new  Response<>();
		Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));
		
		if(token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
			token = Optional.of(token.get().substring(7));
		}
		
		if(!token.isPresent()) {
			response.getErrors().add("Token não informado");
		}
		else if(!jwtToken.tokenValido(token.get())) {
			response.getErrors().add("Token inválido ou expirado.");
		}
		
		if(!response.getErrors().isEmpty()) {
			return ResponseEntity.badRequest().body(response);
		}
		
		String refreshedToken = jwtToken.refreshToken(token.get());
		response.setData(new TokenDto(refreshedToken));
		
		return ResponseEntity.ok(response);
	}
}
