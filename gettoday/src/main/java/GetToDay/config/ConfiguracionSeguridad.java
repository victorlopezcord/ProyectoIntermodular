/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GetToDay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author kiwitox
 */
@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactivar CSRF para que la App pueda enviar POST
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // PERMITIR registro y login sin contraseña
                .anyRequest().authenticated() // Todo lo demás sí pide login
            )
            .formLogin(form -> form.disable()) // Quitar esa pantalla blanca que viste
            .httpBasic(basic -> basic.disable());
            
        return http.build();
    }
}