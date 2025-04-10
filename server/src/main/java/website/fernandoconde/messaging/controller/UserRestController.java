package website.fernandoconde.messaging.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import website.fernandoconde.messaging.dto.MessageRequest;
import website.fernandoconde.messaging.repositories.UserRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserRepository userRepository;

    @PostMapping("/find")
    public ResponseEntity<Map<String, Object>> findByUsername(
            @RequestBody MessageRequest request
    ) { return (userRepository.existsByUsername(request.username())?
            ResponseEntity.status(HttpStatus.OK).body(
            Map.of("status", 200,
                    "message", "User exists in DB")
    ) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            Map.of("status", 400,
                    "message", "User not found")
            )
    );}
}