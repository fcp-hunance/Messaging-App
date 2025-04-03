package website.fernandoconde.messaging.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @GetMapping("/me")
    public Map<String, Object> myself(@AuthenticationPrincipal OAuth2User principal) {
        return Map.of(
                "name", principal.getAttribute("login"), // GitHub username
                "attributes", principal.getAttributes() // All GitHub user info
        );
    }
}