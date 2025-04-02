package website.fernandoconde.messaging.server.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import website.fernandoconde.messaging.server.models.Message;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @PostMapping
    public Message createMessage(@RequestBody Message message) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        message.setSender.(username);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }
}
