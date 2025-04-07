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

@RequiredArgsConstructor
public class JsonAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        if (!isLoginRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            LoginRequest loginRequest = objectMapper.readValue(
                    request.getInputStream(),
                    LoginRequest.class
            );
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            String token = jwtTokenProvider.generateToken(authentication);

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

    private boolean isLoginRequest(HttpServletRequest request) {
        return "/api/auth/login".equals(request.getServletPath())
                && "POST".equalsIgnoreCase(request.getMethod());
    }
}