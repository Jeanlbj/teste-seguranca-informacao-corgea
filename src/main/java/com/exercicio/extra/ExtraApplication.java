package com.exercicio.extra;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(
		info = @Info(title = "Sistema de Apostas API", version = "1.0", description = "Documentação da API do Sistema de Apostas Esportivas"),
		security = {
				@SecurityRequirement(name = "bearerAuth")
		}
)
@SecurityScheme(
		name = "bearerAuth",
		description = "Autenticação JWT",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		in = SecuritySchemeIn.HEADER
)

@SpringBootApplication
public class ExtraApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExtraApplication.class, args);
	}

}