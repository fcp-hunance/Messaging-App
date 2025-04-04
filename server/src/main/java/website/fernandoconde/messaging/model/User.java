package website.fernandoconde.messaging.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // Changed from 'user_repository' (more conventional)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    private String password; // Encrypted (for local users)

    // OAuth2 fields (for GitHub users)
    private String provider;  // "github" or null
    private String providerId; // GitHub user ID

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Set<UserRole> roles;

    // Constructor for OAuth2 users (GitHub)
    public User(String provider, String providerId, String email, Set<UserRole> roles) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.roles = roles;
    }

    // Constructor for local users
    public User(String username, String email, String password, Set<UserRole> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
