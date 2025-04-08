package website.fernandoconde.messaging.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import website.fernandoconde.messaging.model.User;
import website.fernandoconde.messaging.model.UserRole;
import website.fernandoconde.messaging.repositories.MessageRepository;
import website.fernandoconde.messaging.repositories.UserRepository;

import java.util.List;

@Service
public class TestDataInitializer {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public TestDataInitializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        // Create users
        User user1 = User.createLocalUser("user1", "user1@example.com", passwordEncoder.encode("testpassword1"), UserRole.ROLE_USER);
        User user2 = User.createLocalUser("user2", "user2@example.com", passwordEncoder.encode("testpassword2"), UserRole.ROLE_USER);

        userRepository.saveAll(List.of(user1, user2));

//        // Create messages
//        Message msg1 = new Message(user1, user2, "Hello User2!", LocalDateTime.now());
//        Message msg2 = new Message(user2, user1, "Hi User1, how are you?", LocalDateTime.now().plusMinutes(5));
//        Message msg3 = new Message(user1, user2, "I'm doing great, thanks!", LocalDateTime.now().plusMinutes(10));
//
//        messageRepository.saveAll(List.of(msg1, msg2, msg3));
    }
}