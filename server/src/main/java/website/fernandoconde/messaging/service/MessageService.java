package website.fernandoconde.messaging.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import website.fernandoconde.messaging.model.Message;
import website.fernandoconde.messaging.model.User;
import website.fernandoconde.messaging.repositories.MessageRepository;
import website.fernandoconde.messaging.repositories.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepo;
    private final UserRepository userRepo;

    public Message sendMessage(UUID senderId, UUID recipientId, String content) {
        User sender = userRepo.findById(senderId).orElseThrow();
        User recipient = userRepo.findById(recipientId).orElseThrow();

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(content);

        return messageRepo.save(message);
    }

    @Transactional
    public List<Message> getAndMarkUndeliveredMessages(UUID recipientId) {
        // 1. Fetch undelivered messages
        User recipient = userRepo.findById(recipientId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Message> undelivered = messageRepo.findByRecipientAndIsDeliveredFalse(recipient);

        // 2. Mark messages as delivered
        undelivered.forEach(message -> message.setDelivered(true));

        // 3. Save changes (automatically flushed by @Transactional)
        return messageRepo.saveAll(undelivered);
    }
}