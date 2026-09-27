package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
		.csrf(c -> c.disable())
		.authorizeHttpRequests((auth) -> 
				auth.requestMatchers("/user/**").permitAll()
				.anyRequest().authenticated()
				)
		.sessionManagement(ssn -> ssn.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.logout(logout -> logout.disable())
		.addFilterBefore(null, UsernamePasswordAuthenticationFilter.class);
		
		return httpSecurity.build();
	}
	
}
