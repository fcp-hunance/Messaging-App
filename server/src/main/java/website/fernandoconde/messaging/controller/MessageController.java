package website.fernandoconde.messaging.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import website.fernandoconde.messaging.dto.MessageRequest;
import website.fernandoconde.messaging.model.Message;
import website.fernandoconde.messaging.model.User;
import website.fernandoconde.messaging.repositories.UserRepository;
import website.fernandoconde.messaging.service.MessageService;

import java.util.Map;

@RestController
@RequestMapping ("/message")
@RequiredArgsConstructor
public class MessageController {

    private final UserRepository userRepo;
    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(
            @AuthenticationPrincipal User sender,
            @RequestBody MessageRequest request
    ) {
        try {
            User recipient = userRepo.findById(request.recipientId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Recipient not found"
                    ));

            // Prevent self-messaging
            if (sender.getId().equals(recipient.getId())) {
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
                            "timestamp", message.getTimestamp()
                    )
            );

        } catch (ResponseStatusException ex) {
            throw ex; // Re-throw explicitly caught exceptions
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to send message",
                    ex
            );
        }
    }
}
