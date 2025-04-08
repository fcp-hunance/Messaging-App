package website.fernandoconde.messaging.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import website.fernandoconde.messaging.model.User;
import website.fernandoconde.messaging.repositories.UserRepository;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public Map<String, Object> myself(@AuthenticationPrincipal User user) throws UsernameNotFoundException {
        // Fetch from DB to get complete user data
        User userFetched = userRepository.findByUsername(user.getUsername());

        return Map.of(
                "id", userFetched.getId(),
                "username", userFetched.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole()
        );
    }
}