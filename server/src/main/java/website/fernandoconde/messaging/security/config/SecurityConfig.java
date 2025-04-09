package website.fernandoconde.messaging.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import website.fernandoconde.messaging.model.CustomOAuth2User;
import website.fernandoconde.messaging.repositories.UserRepository;
import website.fernandoconde.messaging.security.components.JwtTokenProvider;
import website.fernandoconde.messaging.security.services.CustomOAuth2UserService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(UserRepository userRepository, ObjectMapper objectMapper, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions(withDefaults()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/error**",
                                "/api/test/**",
                                "/login/**",
                                "/oauth2/**",
                                "/h2-console/**",
                                "/api/auth/login"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(jsonAuthenticationFilter(
                        authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))),
                        UsernamePasswordAuthenticationFilter.class
                )
                .oauth2Login(oauth2 -> oauth2
                                .userInfoEndpoint(userInfo -> userInfo
                                        .userService(oauth2UserService())
                                )
                                .successHandler(successHandler())
                                .failureHandler(authenticationFailureHandler())
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public JsonAuthenticationFilter jsonAuthenticationFilter(
            AuthenticationManager authenticationManager
    ) {
        return new JsonAuthenticationFilter(
                objectMapper,
                authenticationManager,
                jwtTokenProvider
        );
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    String.format("{\"error\": \"Authentication failed\", \"message\": \"%s\"}",
                            exception.getMessage())
            );
        };
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new CustomOAuth2UserService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            // 1. Extract your saved user
            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

            // 2. Generate JWT
            String jwt = jwtTokenProvider.generateToken(authentication);

            // 3. Return JSON response
            response.setContentType("application/json");
            response.getWriter().write(
                    String.format("{\"token\": \"%s\", \"userId\": \"%s\"}",
                            jwt,
                            oauthUser.getUsername())
            );
        };
    }
}
