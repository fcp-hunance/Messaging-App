package website.fernandoconde.messaging.controller;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    @GetMapping("/me")
    public Map<String, Object> myself(@AuthenticationPrincipal Jwt jwt) {
        return Map.of("subject", jwt.getSubject(), "claims", jwt.getClaims()); //TODO Configure Keycloak or another OAuth2 Server and test with Postman
    }
}
