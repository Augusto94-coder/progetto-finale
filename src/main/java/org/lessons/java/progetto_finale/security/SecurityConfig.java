package org.lessons.java.progetto_finale.security;

/* import java.util.List; */

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/* import org.springframework.http.HttpMethod; */
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final DatabaseUserDetailService databaseUserDetailService;

    public SecurityConfig(DatabaseUserDetailService databaseUserDetailService) {
        this.databaseUserDetailService = databaseUserDetailService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(databaseUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/api/public/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/organizer/**").hasAnyRole("ADMIN", "ORGANIZER")
                        /*
                         * .requestMatchers("/api/public/**").permitAll()
                         * .requestMatchers(HttpMethod.POST, "/api/events/**").hasAnyRole("ADMIN",
                         * "ORGANIZER")
                         * .requestMatchers(HttpMethod.PUT, "/api/events/**").hasAnyRole("ADMIN",
                         * "ORGANIZER")
                         * .requestMatchers(HttpMethod.DELETE, "/api/events/**").hasAnyRole("ADMIN",
                         * "ORGANIZER")
                         */
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }

}