package website.fernandoconde.messaging.repositories;

import org.springframework.data.repository.CrudRepository;
import website.fernandoconde.messaging.model.User;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
}
