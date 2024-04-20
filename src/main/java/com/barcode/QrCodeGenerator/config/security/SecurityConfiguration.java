package com.barcode.QrCodeGenerator.config.security;

import com.barcode.QrCodeGenerator.service.auth.LogOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogOutService logOutService;
  private final String[] allowedUrl = {
    "/",
    "/api/auth/**",
    "/swagger-ui/**",
    "/javainuse-openapi/**",
    "/v3/api-docs",
    "/v3/api-docs/**",
    "/v2/api-docs",
    "/swagger-resources",
    "/swagger-resources/**",
    "configuration/ui",
    "/configuration/security",
    "/webjars/**",
    "/swagger-ui.html"
  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                    .requestMatchers(allowedUrl)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(
            httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(
            httpSecurityLogoutConfigurer ->
                httpSecurityLogoutConfigurer
                    .logoutUrl("/api/auth/logout")
                    .addLogoutHandler(logOutService)
                    .logoutSuccessHandler(
                        ((request, response, authentication) ->
                            SecurityContextHolder.clearContext())));
    return httpSecurity.build();
  }
}
