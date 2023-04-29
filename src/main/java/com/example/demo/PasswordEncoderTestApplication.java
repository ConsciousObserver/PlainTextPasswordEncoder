package com.example.demo;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class PasswordEncoderTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(PasswordEncoderTestApplication.class, args);
    }

}

@RestController
@RequestMapping("/")
class TestController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello World! " + LocalDateTime.now();
    }
}

@Configuration
@EnableWebSecurity
class SecurityConfig {
    @Bean
    PasswordEncoder plainTextPasswordEncoder() {
        return new PasswordEncoder() {

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return Objects.equals(rawPassword, encodedPassword);
            }

            /**
             * Returns password as it is without encoding.
             */
            @Override
            public String encode(CharSequence rawPassword) {
                String encodedPassword = null;

                if (rawPassword != null) {
                    encodedPassword = rawPassword.toString();
                }

                return encodedPassword;
            }
        };
    }
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
            .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .httpBasic();

        return http.build();
    }
}