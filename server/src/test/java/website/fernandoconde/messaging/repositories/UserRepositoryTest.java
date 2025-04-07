package website.fernandoconde.messaging.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import website.fernandoconde.messaging.model.User;
import website.fernandoconde.messaging.model.UserRole;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void testStoreUser() {
        User user = User.createLocalUser("fcp.cello", "ferhu90@gmail.com", "test", UserRole.ROLE_USER);
        repository.save(user);
        assertNotNull(user);
        assertEquals(1L, repository.count());
    }

}