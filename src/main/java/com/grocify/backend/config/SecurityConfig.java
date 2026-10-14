package com.grocify.backend.config;

import com.grocify.backend.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http


                .csrf(csrf -> csrf.disable())


                .cors(cors -> {})


                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                .authorizeHttpRequests(auth -> auth



                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()


                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/login",
                                "/api/auth/signup",
                                "/api/auth/signup/send-otp",
                                "/api/auth/signup/verify-otp",
                                "/api/auth/forgot-password/send-otp",
                                "/api/auth/forgot-password/verify-otp",
                                "/api/auth/forgot-password/reset"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/auth/test"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/forgot-password/verify-otp"
                        ).permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/auth/forgot-password/reset"
                        ).permitAll()


                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/products"
                        ).permitAll()


                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/products"
                        ).hasRole("ADMIN")


                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/products/upload-image"
                        ).hasRole("ADMIN")


                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/products/**"
                        ).hasRole("ADMIN")


                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/products/**"
                        ).hasRole("ADMIN")




                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/cart"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/cart"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/cart/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/cart/**"
                        ).authenticated()


                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/wishlist"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/wishlist/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/wishlist/**"
                        ).authenticated()




                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/orders/admin"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/orders/*/status"
                        ).hasRole("ADMIN")

                        .requestMatchers("/", "/error").permitAll()

                        .requestMatchers("/api/ai").permitAll()

                        .anyRequest().authenticated()
                )


                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                Arrays.asList(
                        "http://localhost:5173",
                        "https://grocify-clone-three.vercel.app",
                        "https://grocify-clone-git-main-developers-8313.vercel.app"
                )
        );

        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                Arrays.asList(
                        "Authorization",
                        "Content-Type"
                )
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}