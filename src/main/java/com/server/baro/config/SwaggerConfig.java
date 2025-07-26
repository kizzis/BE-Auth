package com.server.baro.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI(ServletContext servletContext) {
		String contextPath = servletContext.getContextPath();

		Server server = new Server().url("http://localhost:8080" + contextPath)
			.description("로컬 개발 서버");

		// Server deployedServer = new Server().url("" + contextPath)
		// 	.description("배포 서버");

		return new OpenAPI()
			.servers(List.of(server))
			.components(authSetting())
			.info(swaggerInfo())
			.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
	}

	private Info swaggerInfo() {
		License license = new License();
		license.setUrl("http://www.apache.org/licenses/LICENSE-2.0.html");
		license.setName("Apache 2.0");

		return new Info()
			.version("v1.0.0")
			.title("백엔드 기술과제 (Java)")
			.description("[김은수] 바로인턴 15기 백엔드 기술과제 API 문서입니다.")
			.contact(new Contact()
				.url("https://github.com/kizzis/BE-Auth"))
			.license(license);
	}

	private Components authSetting() {
		return new Components()
			.addSecuritySchemes(
				"bearerAuth",
				new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT")
					.in(SecurityScheme.In.HEADER)
					.name("Authorization"));
	}

}
