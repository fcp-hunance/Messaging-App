package website.fernandoconde.messaging.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "user_repository")
public class User {

    @Id
    private UUID id;
    private String userName;
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Set<UserRole> roles;

    protected User() {

    }

    public User(UUID id, String userName, String email, String password, Set<UserRole> roles) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }
}
