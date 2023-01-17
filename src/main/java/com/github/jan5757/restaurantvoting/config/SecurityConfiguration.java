package com.github.jan5757.restaurantvoting.config;

import com.github.jan5757.restaurantvoting.model.User;
import com.github.jan5757.restaurantvoting.repository.UserRepository;
import com.github.jan5757.restaurantvoting.web.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.github.jan5757.restaurantvoting.model.Role;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
public class SecurityConfiguration {
    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PASSWORD_ENCODER;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            log.debug("Authenticating '{}'", email);
            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
            return new AuthUser(optionalUser.orElseThrow(
                    () -> new UsernameNotFoundException("User '" + email + "' was not found")));
        };
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/api/profile").anonymous()
                .requestMatchers("/api/profile").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .requestMatchers("/api/votes/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .requestMatchers(HttpMethod.GET, "/api/restaurants/**").hasAnyRole(Role.ADMIN.name(), Role.USER.name())
                .requestMatchers("/api/**").hasRole(Role.ADMIN.name())
                .anyRequest().denyAll()
                .and().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
        return http.build();
    }
}
