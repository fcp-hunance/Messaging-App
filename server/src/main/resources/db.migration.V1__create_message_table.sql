CREATE INDEX idx_message_recipient_delivered
ON message(recipient_id, is_delivered);

CREATE INDEX idx_message_timestamp
ON message(timestamp DESC);

CREATE TABLE users (
    id BINARY(16) PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    provider VARCHAR(50),
    provider_id VARCHAR(255),
    INDEX idx_provider (provider, provider_id)
);

CREATE TABLE users_roles (
    user_id BINARY(16),
    roles VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(id)
);