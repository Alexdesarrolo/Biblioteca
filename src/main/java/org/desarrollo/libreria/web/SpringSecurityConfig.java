package org.desarrollo.libreria.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {
    
    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.security.core.userdetails.UserDetailsService UserDetailsService() throws Exception{
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admon").password(passwordEncoder().encode("Abc123")).roles("USER", "ADMIN").build());
		manager.createUser(User.withUsername("trespa").password(passwordEncoder().encode("Abc123")).roles("USER").build());
		manager.createUser(User.withUsername("alex").password(passwordEncoder().encode("123")).roles("USER", "ADMIN").build());
		
		return manager;
    }

    @Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
		.requestMatchers("/", "/css/**", "/js/**", "/img/**", "/libreria/usuarioslistar", "/libreria/libroslistar").permitAll()
		.requestMatchers("/libreria/usuarioconsultar/**", "/libreria/libroconsultar/**").hasAnyRole("USER")
		.requestMatchers("/libreria/usuarionuevo/**", "/libreria/usuarioeliminar/**", "/libreria/usuariomodificar/**").hasAnyRole("ADMIN")
		.requestMatchers("/libreria/libronuevo/**", "/libreria/libroeliminar/**", "/libreria/libromodificar/**").hasAnyRole("ADMIN")
		.requestMatchers("/libreria/prestamonuevo/**").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and()
        .formLogin().loginPage("/login").permitAll()
        .and()
        .logout().permitAll()
        .and()
        .exceptionHandling().accessDeniedPage("/error_403");
		
		
		return http.build();
	}

}
