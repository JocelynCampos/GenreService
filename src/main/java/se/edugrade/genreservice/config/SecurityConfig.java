package se.edugrade.genreservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerJwtAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${edufy.security.client-id}")
    private String clientId;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(h-> h.frameOptions(f -> f.sameOrigin()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET,"/edufy/v1/genre/test").permitAll()
                        .requestMatchers(HttpMethod.GET, "/edufy/v1/genre/all").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/edufy/v1/genre/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/edufy/v1/genre/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/edufy/v1/genre/delete/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(auth2 -> auth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }



}
