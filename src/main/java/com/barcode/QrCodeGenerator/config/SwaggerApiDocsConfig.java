package com.barcode.QrCodeGenerator.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerApiDocsConfig {
  private static final String OAUTH_SCHEME = "bearer";

  @Bean
  public OpenAPI customOpenAPI() {
    final Info info =
        new Info()
            .title("QrPay")
            .description("Payment service")
            .version("1.0.0")
            .license(new License().name("© Tonmoy Saha").url("https://github.com/Tonmoy-saha18"));
    final Components components = new Components();
    final SecurityRequirement securityRequirement = new SecurityRequirement().addList(OAUTH_SCHEME);
    components.addSecuritySchemes(OAUTH_SCHEME, createSecuritySchemeScheme());
    return new OpenAPI().info(info).components(components).addSecurityItem(securityRequirement);
  }

  private SecurityScheme createSecuritySchemeScheme() {
    return new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme(OAUTH_SCHEME);
  }
}
