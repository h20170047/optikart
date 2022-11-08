package com.svj;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "OPTIKART SERVICE", version = "v3.0", description = "Optikart API services"))
public class OptiKartApplication {

	public static void main(String[] args) {
		SpringApplication.run(OptiKartApplication.class, args);
	}

}
