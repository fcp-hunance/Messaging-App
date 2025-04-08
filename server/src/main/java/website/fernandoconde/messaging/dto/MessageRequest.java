package website.fernandoconde.messaging.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageRequest(
        @NotNull
        String username,

        @NotBlank(message = "Message content cannot be empty")
        String content
) {}