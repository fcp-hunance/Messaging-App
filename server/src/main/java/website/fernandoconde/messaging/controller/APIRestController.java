package website.fernandoconde.messaging.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import website.fernandoconde.messaging.repositories.UserRepository;

@RestController
@RequestMapping("/api")
public class APIRestController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public APIRestController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Server is running";
    }

    @GetMapping("/protected")
    public String protectedData() {
        return "Protected data";
    }

    @GetMapping("/debug-auth")
    public String debugAuth(Authentication authentication) {
        if (authentication == null) {
            return "NOT AUTHENTICATED";
        }
        return "Authenticated as: " + authentication.getPrincipal().toString();
    }


}
