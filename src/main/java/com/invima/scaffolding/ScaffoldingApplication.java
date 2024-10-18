package com.invima.scaffolding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ScaffoldingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScaffoldingApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")  // Aplica a todas las rutas
						.allowedOriginPatterns("*")  // Permitir todos los orígenes con credenciales
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos permitidos
						.allowedHeaders("*")  // Permitir todos los headers
						.allowCredentials(false)  // Permitir envío de credenciales (cookies, headers)
						.maxAge(3600);
			}
		};
	}

}
