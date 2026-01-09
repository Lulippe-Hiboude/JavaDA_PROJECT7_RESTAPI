package com.nnk.springboot.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Central security configuration for the application.
 * <p>
 * This class configures Spring Security using a {@link SecurityFilterChain}
 * and defines:
 * <ul>
 *     <li>Access rules based on user roles</li>
 *     <li>Custom login and logout behavior</li>
 *     <li>Exception handling (access denied)</li>
 *     <li>Session management</li>
 *     <li>Authentication and password encoding</li>
 * </ul>
 *
 * <p>
 * The application supports two roles:
 * <ul>
 *     <li>{@code ROLE_ADMIN} – full access to administrative endpoints</li>
 *     <li>{@code ROLE_USER} – limited access to business endpoints</li>
 * </ul>
 *
 * <p>
 * Authentication is performed via a custom login page and a custom
 * {@link AuthenticationSuccessHandler} is used to redirect users
 * after successful login based on their role.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    /**
     * Custom {@link UserDetailsService} used to load users from
     * the application's persistence layer.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Defines the main Spring Security filter chain.
     * <p>
     * This configuration:
     * <ul>
     *     <li>Disables CSRF protection</li>
     *     <li>Allows public access to static resources and login pages</li>
     *     <li>Restricts access to endpoints based on user roles</li>
     *     <li>Configures form-based authentication</li>
     *     <li>Configures logout behavior</li>
     *     <li>Manages HTTP sessions</li>
     * </ul>
     *
     * @param http the {@link HttpSecurity} object used to configure security
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if a security configuration error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/css/**",
                                "/favicon.ico",
                                "/",
                                "/auth/login",
                                "/auth/403"
                        ).permitAll()
                        .requestMatchers(
                                "/home",
                                "/bid/**",
                                "/trade/**"
                        ).hasAnyRole("ADMIN", "USER")
                        .requestMatchers(
                                "/admin/home/**",
                                "/curvePoint/**",
                                "/ruleName/**",
                                "/rating/**",
                                "/user/**"
                        ).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/auth/403")
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .successHandler(authenticationSuccessHandler())
                        .failureUrl("/auth/login?error=true")
                )
                .logout(logout -> logout
                        .logoutUrl("/app-logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .userDetailsService(userDetailsService)
                .build();
    }

    /**
     * Provides the password encoder used by Spring Security
     * to hash and verify user passwords.
     * <p>
     * Uses {@link BCryptPasswordEncoder}, which is currently
     * the recommended password hashing algorithm.
     *
     * @return a {@link PasswordEncoder} instance
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines a custom {@link AuthenticationSuccessHandler}.
     * <p>
     * This handler is invoked after a successful authentication
     * and is responsible for redirecting the user to the appropriate
     * page based on their role.
     *
     * @return a custom {@link AuthenticationSuccessHandler} instance
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}
