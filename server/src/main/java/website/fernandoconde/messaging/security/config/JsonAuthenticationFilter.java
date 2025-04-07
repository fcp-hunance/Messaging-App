package website.fernandoconde.messaging.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;
import website.fernandoconde.messaging.dto.LoginRequest;
import website.fernandoconde.messaging.security.components.JwtTokenProvider;

import java.io.IOException;

public class JsonAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public JsonAuthenticationFilter(ObjectMapper objectMapper,
                                    AuthenticationManager authenticationManager,
                                    JwtTokenProvider jwtTokenProvider) {
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        if (!request.getServletPath().equals("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Parse JSON request
            LoginRequest loginRequest = objectMapper.readValue(
                    request.getInputStream(),
                    LoginRequest.class
            );

            // Authenticate
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            // Generate JWT token
            String token = jwtTokenProvider.generateToken(authentication);

            // Return token in response
            response.setContentType("application/json");
            response.getWriter().write(
                    String.format("{\"token\": \"%s\"}", token)
            );
        } catch (AuthenticationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    String.format("{\"error\": \"Authentication failed\", \"message\": \"%s\"}",
                            e.getMessage())
            );
        }
    }
}