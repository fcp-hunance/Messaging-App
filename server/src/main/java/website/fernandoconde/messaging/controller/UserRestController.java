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

//    @GetMapping("/me")
//    public Map<String, Object> myself(@AuthenticationPrincipal OAuth2User oauth2User) throws UsernameNotFoundException {
//        // Fetch from DB to get complete user data
//        User user = userRepository.findByProviderAndProviderId("github", oauth2User.getName()).orElseThrow(() -> new UsernameNotFoundException(oauth2User.getName()));
//
//        return Map.of(
//                "id", user.getId(),
//                "username", user.getUsername() != null ? user.getUsername() : Objects.requireNonNull(oauth2User.getAttribute("login")),
//                "email", user.getEmail(),
//                "role", user.getRole()
//        );
//    }
}