package website.fernandoconde.messaging.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageRequest(
        @NotNull(message = "Recipient ID cannot be null")
        UUID recipientId,

        @NotBlank(message = "Message content cannot be empty")
        String content
) {}