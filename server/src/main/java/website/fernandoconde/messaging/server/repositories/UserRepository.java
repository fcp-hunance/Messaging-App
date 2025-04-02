package website.fernandoconde.messaging.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import website.fernandoconde.messaging.server.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
