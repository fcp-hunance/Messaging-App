package website.fernandoconde.messaging.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import website.fernandoconde.messaging.dto.MessageRequest;
import website.fernandoconde.messaging.model.CustomOAuth2User;
import website.fernandoconde.messaging.model.Message;
import website.fernandoconde.messaging.model.User;
import website.fernandoconde.messaging.repositories.UserRepository;
import website.fernandoconde.messaging.service.MessageService;

import java.util.*;

@RestController
@RequestMapping ("/message")
@RequiredArgsConstructor
public class MessageController {

    private final UserRepository userRepo;
    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendMessage(
            Authentication authentication,
            @RequestBody MessageRequest request
    ) {
        try {
            String username = getUsername(authentication);

            User sender = userRepo.findByUsername(username);
            User recipient = userRepo.findByUsername(request.username());

            if (recipient.getEmail().isEmpty()) {
                System.out.println("Recipient not found!");
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Recipient not found!"
                );
            }

            // Prevent self-messaging
            if (sender.getUsername().equals(recipient.getUsername())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Cannot send message to yourself"
                );
            }

            Message message = messageService.sendMessage(
                    sender.getId(),
                    recipient.getId(),
                    request.content()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "messageId", message.getId(),
                            "content", message.getContent(),
                            "recipientId", recipient.getId(),
                            "timestamp", message.getTimestamp()
                    )
            );

        } catch (ResponseStatusException ex) {
            throw ex; // Re-throw explicitly caught exceptions
        } catch (Exception ex) {
            System.err.println("ERROR IN SEND MESSAGE:"); // Debug log
            ex.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to send message",
                    ex
            );
        }
    }

    @GetMapping("/undelivered")
    public ResponseEntity<List<Map<String, Object>>> getUndeliveredMessages(
            Authentication authentication
    ) {
        String username = getUsername(authentication);
        User recipient = userRepo.findByUsername(username);
        List<Message> undelivered = messageService.getUndeliveredMessages(recipient.getId());

        List<Map<String, Object>> response = undelivered.stream()
                .map(msg -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", msg.getId());
                    map.put("content", msg.getContent());
                    map.put("recipientId", msg.getRecipient().getId());
                    map.put("timestamp", msg.getTimestamp());
                    return map;
                })
                .toList();
        return ResponseEntity.ok(response);
    }


    private static String getUsername(Authentication authentication) {
        String username;

        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            username = principal.getUsername();
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails principal = (UserDetails) authentication.getPrincipal();
            username = principal.getUsername();
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown authentication type");
        }
        return username;
    }

}
