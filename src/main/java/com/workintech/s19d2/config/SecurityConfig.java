package com.workintech.s19d2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(
            DataSource dataSource) {

        JdbcUserDetailsManager manager =
                new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery("""
                SELECT email, password, true
                FROM bank.member
                WHERE email = ?
                """);

        manager.setAuthoritiesByUsernameQuery("""
                SELECT m.email, r.authority
                FROM bank.member m
                JOIN bank.member_role mr
                    ON m.id = mr.member_id
                JOIN bank.role r
                    ON r.id = mr.role_id
                WHERE m.email = ?
                """);

        return manager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                HttpMethod.POST,
                                "/auth/register"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/account",
                                "/account/**",
                                "/accounts",
                                "/accounts/**"
                        ).hasAnyAuthority("USER", "ADMIN")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/account",
                                "/accounts"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/account/**",
                                "/accounts/**"
                        ).hasAuthority("ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/account/**",
                                "/accounts/**"
                        ).hasAuthority("ADMIN")

                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}