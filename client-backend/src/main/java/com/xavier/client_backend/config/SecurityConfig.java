package com.xavier.client_backend.config;

import com.xavier.client_backend.domain.entities.ClientEntity;
import com.xavier.client_backend.repositories.ClientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ClientRepository clientRepository;

    public SecurityConfig(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {
                CorsConfigurationSource source = request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("https://mango-plant-0dc82e003.5.azurestaticapps.net"));
                    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(List.of("*"));
                    configuration.setAllowCredentials(true);
                    return configuration;
                };
                cors.configurationSource(source);
            })
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/api/products/**").permitAll();
                auth.anyRequest().authenticated();
            })
            .oauth2Login(oauth2 -> oauth2.successHandler(successHandler()))
            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            );
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");

            ClientEntity client = clientRepository.findByEmail(email);
            if (client == null) {
                client = new ClientEntity();
                client.setName(oAuth2User.getAttribute("name"));
                client.setEmail(email);
                clientRepository.save(client);
            }

            SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
            handler.setDefaultTargetUrl("https://mango-plant-0dc82e003.5.azurestaticapps.net/");
            handler.onAuthenticationSuccess(request, response, authentication);
        };
    }
}
