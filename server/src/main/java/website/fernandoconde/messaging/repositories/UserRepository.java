package website.fernandoconde.messaging.repositories;

import jakarta.persistence.Id;
import org.springframework.data.repository.CrudRepository;
import website.fernandoconde.messaging.model.User;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    // For local auth
    User findByUsername(String username);

    // For OAuth2 auth
    User findByProviderAndProviderId(String provider, String providerId);

    // Common checks
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findById(Id id);
}