package website.fernandoconde.messaging.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import website.fernandoconde.messaging.model.User;
import website.fernandoconde.messaging.model.UserRole;
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

    @PostMapping("/test/create-test-user")
    public String createUser() {
        User user = User.createLocalUser(
                "testuser",
                "test@example.com",
                passwordEncoder.encode("testpassword"),
                UserRole.ROLE_USER
        );
        userRepository.save(user);
        return "Test user created!";
    }

    @GetMapping("/protected")
    public String protectedData() {
        return "Protected data";
    }
}
