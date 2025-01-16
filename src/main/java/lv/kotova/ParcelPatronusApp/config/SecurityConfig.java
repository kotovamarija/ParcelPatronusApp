package lv.kotova.ParcelPatronusApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/static/**", "/landing_page.jpg",
                                        "/styles_home.css","/styles_auth.css", "/styles_general.css",
                                        "/registration", "/login", "/error", "/favicon.ico").permitAll()
                                .anyRequest().hasAnyRole("ADMIN", "USER")
                )
                .formLogin(form -> form
                .loginPage("/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error")
                )
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login")
                );

        return http.build();
    }


}
