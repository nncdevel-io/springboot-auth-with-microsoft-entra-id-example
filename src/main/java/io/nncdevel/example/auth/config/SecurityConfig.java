package io.nncdevel.example.auth.config;

import com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadWebApplicationHttpSecurityConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for Microsoft Entra ID integration.
 *
 * This configuration class sets up Spring Security to work with Microsoft Entra ID
 * for OAuth2-based authentication and authorization.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.cloud.azure.active-directory.enabled:true}")
    private boolean aadEnabled;

    /**
     * Configures the security filter chain with Microsoft Entra ID authentication.
     *
     * This method defines:
     * - Public endpoints that don't require authentication (/, /css/**, /error)
     * - Protected endpoints that require authentication (/profile)
     * - OAuth2 login configuration
     * - Logout configuration
     * - CSRF protection
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Apply Microsoft Entra ID specific security configuration only if enabled
        if (aadEnabled) {
            http.apply(AadWebApplicationHttpSecurityConfigurer.aadWebApplication());
        }

        http
            .authorizeHttpRequests(authorize -> authorize
                // Public endpoints - accessible without authentication
                .requestMatchers("/", "/css/**", "/error").permitAll()
                // Protected endpoints - require authentication (only when AAD is enabled)
                .requestMatchers("/profile").authenticated()
                // All other requests
                .anyRequest().permitAll()
            );

        // Configure OAuth2 login only when AAD is enabled
        if (aadEnabled) {
            http.oauth2Login(oauth2 -> oauth2
                // Default login page will be used
                .defaultSuccessUrl("/", true)
            );
        }

        http.logout(logout -> logout
            // Configure logout
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies("JSESSIONID")
        );

        return http.build();
    }
}
