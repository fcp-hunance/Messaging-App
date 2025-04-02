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
        HashSet<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ADMIN);
        roles.add(UserRole.USER);
        User user = repository.save(new User(UUID.randomUUID(), "fcp.cello", "ferhu90@gmail.com", "Cimdata2025", roles));
        assertNotNull(user);
        assertEquals(1L, repository.count());
    }

}