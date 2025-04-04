package website.fernandoconde.messaging.security.components;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import website.fernandoconde.messaging.model.User;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        User user = (User) authentication.getPrincipal();

        // Generate response
        response.setContentType("application/json");
        response.getWriter().println(
                String.format(
                        "{\"id\": \"%s\", \"username\": \"%s\", \"email\": \"%s\", \"roles\": %s}",
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles()
                )
        );
        response.setStatus(HttpServletResponse.SC_OK);
    }
}