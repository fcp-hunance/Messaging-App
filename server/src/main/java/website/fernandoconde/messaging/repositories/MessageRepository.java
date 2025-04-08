package website.fernandoconde.messaging.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import website.fernandoconde.messaging.model.Message;
import website.fernandoconde.messaging.model.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    // Find all undelivered messages for a recipient
    @Query("SELECT m FROM Message m WHERE m.recipient = :recipient AND m.isDelivered = false")
    List<Message> findByRecipientAndIsDeliveredFalse(@Param("recipient") User recipient);

    // Bulk mark messages as delivered
    @Modifying
    @Query("UPDATE Message m SET m.isDelivered = true WHERE m.id IN :messageIds")
    void markAsDelivered(List<Long> messageIds);

    // Count undelivered messages for a user
    long countByRecipientAndIsDeliveredFalse(User user);
}