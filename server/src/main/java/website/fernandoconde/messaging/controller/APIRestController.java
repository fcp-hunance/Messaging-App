package website.fernandoconde.messaging.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class APIRestController {

    @GetMapping("/test")
    public String testEndpoint() {
        return "Server is running";
    }

    @GetMapping("/protected")
    public String protectedData(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        // Verify token with GitHub's API if needed
        return "Protected data";
    }
}
