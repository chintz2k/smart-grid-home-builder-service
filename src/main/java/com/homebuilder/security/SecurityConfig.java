package com.homebuilder.security;

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
						.requestMatchers("/api/consumers").permitAll()
						.requestMatchers("/api/producers").permitAll()
						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
