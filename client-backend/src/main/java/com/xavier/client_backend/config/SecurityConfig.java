package com.xavier.client_backend.config;

import com.xavier.client_backend.domain.entities.ClientEntity;
import com.xavier.client_backend.filter.CorsFilter;
import com.xavier.client_backend.repositories.ClientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

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
                .addFilterBefore(new CorsFilter(), org.springframework.web.filter.CorsFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/logout", "/api/orders/**", "/api/clients/me"))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/products/**", "/", "/index.html", "/static/**",
                            "/*.ico", "/*.json", "/*.png", "/api/clients/me").permitAll();
                    auth.requestMatchers("/api/orders/**").authenticated();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> oauth2.successHandler(successHandler()))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("https://mango-plant-0dc82e003.5.azurestaticapps.net"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
