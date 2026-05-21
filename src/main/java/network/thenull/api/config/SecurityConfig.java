package network.thenull.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	// https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/config/annotation/web/builders/HttpSecurity.html
	@Bean
 	public SecurityFilterChain securityFilterChain(HttpSecurity http)  {
		http
			// cross-site request forgery security disabled - will likely want to remove or modify this line before deployment
			.csrf(csrf -> csrf.disable())
			
			.authorizeHttpRequests(authorize -> 
				authorize
					.anyRequest().permitAll()
			)
		;
		
		return http.build();
	}
}
