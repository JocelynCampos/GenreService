package se.edugrade.genreservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(h-> h.frameOptions(f -> f.sameOrigin()))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET,"/edufy/v1/genre/test").permitAll()
                        .requestMatchers(HttpMethod.GET, "/edufy/v1/genre/all").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/edufy/v1/genre/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/edufy/v1/genre/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/edufy/v1/genre/delete/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user1 = User.withUsername("Erik")
                .password("{noop}Edman")
                .roles("USER")
                .build();

        UserDetails user2 = User.withUsername("Jocelyn")
                .password("{noop}CarrilloCampos")
                .roles("USER")
                .build();

        UserDetails user3 = User.withUsername("Mohamed")
                .password("{noop}Sharshar")
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("Hugo")
                .password("{noop}Ransvi")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1, user2, user3, admin);
    }
}
