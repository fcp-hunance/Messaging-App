package website.fernandoconde.messaging.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class APIRestController {

    public String testEndpoint() {
        return "Server is running";
    }
}
