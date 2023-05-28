package com.thelobsterpot.userauthorization.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/api/user/login",
//                                "/api/user/register",
//                                "/login",
//                                "/register"
//                        ).permitAll());
//
//
//
//
//
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests((requests) -> {
            ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.anyRequest()).permitAll();
        });
        http.formLogin(Customizer.withDefaults());


//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/api/user/register", "/api/user/login").permitAll()
//                        .anyRequest().authenticated())
//                .formLogin(loginConfigurer -> loginConfigurer
//                        .loginProcessingUrl("/login") // change the login processing url
//                        .defaultSuccessUrl("/me", true)) // and also define a default success url
//                .logout(logoutConfigurer -> logoutConfigurer
//                        .logoutSuccessUrl("/login"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
