CREATE INDEX idx_message_recipient_delivered
ON message(recipient_id, is_delivered);

CREATE INDEX idx_message_timestamp
ON message(timestamp DESC);

CREATE TABLE users (
    id BINARY(16) PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    password VARCHAR(255),
    provider VARCHAR(50),
    provider_id VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_provider (provider, provider_id),
    INDEX idx_role (role)
);