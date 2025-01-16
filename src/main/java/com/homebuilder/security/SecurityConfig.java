package com.homebuilder.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author André Heinen
 */
@Configuration
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/rooms/admin/**").hasRole("ADMIN")
						.requestMatchers("/api/consumers/admin/**").hasRole("ADMIN")
						.requestMatchers("/api/producers/admin/**").hasRole("ADMIN")
						.requestMatchers("/api/storages/admin/**").hasRole("ADMIN")
						.requestMatchers("/api/devices/admin/**").hasRole("ADMIN")
						.requestMatchers("/api/devices/system/**").hasAnyRole("ADMIN", "SYSTEM")
						.requestMatchers("/api/rooms").permitAll()
						.requestMatchers("/api/consumers").permitAll()
						.requestMatchers("/api/producers").permitAll()
						.requestMatchers("/api/storages").permitAll()
						.requestMatchers("/api/devices").permitAll()
						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(exceptionHandling ->
						exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
							response.setContentType("application/json");
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							response.getWriter().write("{\"error\": \"Authentication is required to access this resource\"}");
						})
				)
				.exceptionHandling(exceptionHandling ->
						exceptionHandling.accessDeniedHandler((request, response, accessDeniedException) -> {
							response.setContentType("application/json");
							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
							response.getWriter().write("{\"error\": \"You do not have permission to access this resource\"}");
						})
				)
		;
		return http.build();
	}
}
