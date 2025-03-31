CREATE TABLE user_info (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    mobile VARCHAR(15) UNIQUE,
    display_name VARCHAR(100),
    avatar VARCHAR(255),
    gender VARCHAR(20),
    location VARCHAR(100),
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    roles VARCHAR(255) NOT NULL,
    locale VARCHAR(10),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Social login connections
CREATE TABLE user_connection (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES user_info(id) ON DELETE CASCADE,
    provider_id VARCHAR(50) NOT NULL,
    provider_user_id VARCHAR(100) NOT NULL,
    display_name VARCHAR(100),
    profile_url VARCHAR(255),
    image_url VARCHAR(255),
    access_token VARCHAR(255) NOT NULL,
    refresh_token VARCHAR(255),
    expire_time BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    UNIQUE (provider_id, provider_user_id)
);

-- Indices for faster lookups
CREATE INDEX idx_user_connection_user_id ON user_connection(user_id);
CREATE INDEX idx_user_connection_provider_id ON user_connection(provider_id); 