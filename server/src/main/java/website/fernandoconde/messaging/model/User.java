package website.fernandoconde.messaging.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users") // Changed from 'user_repository' (more conventional)
public class User {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(columnDefinition = "BINARY(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = true) // Allow null for OAuth users
    private String password; // Encrypted (for local users)

    // OAuth2 fields (for GitHub users)
    private String provider;  // "GitHub" or null
    private String providerId; // GitHub user ID


    // Constructor for OAuth2 users (GitHub)
    public static User createOAuth2User(String provider, String providerId, String email, UserRole role, String password) {
        User user = new User();
        user.setProvider(provider);
        user.setProviderId(providerId);
        user.setRole(role);
        user.setEmail(email);
        user.setUsername(providerId);
        user.setPassword(password);
        return user;
    }

    // Constructor for local users
    public static User createLocalUser(String username, String email, String password, UserRole role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", password='" + password + '\'' +
                ", provider='" + provider + '\'' +
                ", providerId='" + providerId + '\'' +
                '}';
    }
}
