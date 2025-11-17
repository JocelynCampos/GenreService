package se.edugrade.genreservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import se.edugrade.genreservice.converters.JwtAuthConverter;

@Configuration
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    @Autowired
    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                //.headers(h-> h.frameOptions(f -> f.sameOrigin()))
                //.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET,"/edufy/v1/genre/test").permitAll()
                        .requestMatchers(HttpMethod.GET, "/edufy/v1/genre/all").hasAnyRole("admin", "user")
                        .requestMatchers(HttpMethod.GET, "/edufy/v1/genre/by-name/**").hasAnyRole("admin", "user")
                        .requestMatchers(HttpMethod.GET, "/edufy/v1/genre/by-id/**").hasRole("admin")
                        .requestMatchers(HttpMethod.POST, "/edufy/v1/genre/add").hasRole("admin")
                        .requestMatchers(HttpMethod.PUT, "/edufy/v1/genre/update/**").hasRole("admin")
                        .requestMatchers(HttpMethod.DELETE, "/edufy/v1/genre/delete/**").hasRole("admin")

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(auth2 -> auth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));
        return http.build();
    }



}
