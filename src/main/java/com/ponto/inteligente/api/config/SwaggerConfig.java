package com.ponto.inteligente.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import com.ponto.inteligente.api.security.utils.JwtTokenUtil;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ActiveProfiles("dev")
public class SwaggerConfig {

	@Autowired
	private JwtTokenUtil tokenUtil;

	@Autowired
	private UserDetailsService userService;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.ponto.inteligente.api.controllers"))
				.paths(PathSelectors.any()).build()
				.apiInfo(this.apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Pont inteligênte API")
				.description("Documentação da API de acesso aos endpoints do Ponto inteligênte").version("1.0")
				.build();
	}

	
	@Bean
	public SecurityConfiguration security() {
		String token;
		try {
			UserDetails userDetails = this.userService.loadUserByUsername("admin@calangus.com");
			token = this.tokenUtil.obterToken(userDetails);
		}
		catch(Exception e) {
			token = "";

		}
		return new SecurityConfiguration(null, null, null, null, "Bearer " + token,
				ApiKeyVehicle.HEADER, "Authorization", ",");
	}
	
}
