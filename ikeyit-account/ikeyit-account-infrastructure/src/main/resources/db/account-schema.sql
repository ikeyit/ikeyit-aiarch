-- User table
CREATE TABLE user_info
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username     VARCHAR(50)    NOT NULL UNIQUE,
    password     VARCHAR(100)   NOT NULL DEFAULT '',
    email        VARCHAR(100),
    phone       VARCHAR(15),
    display_name VARCHAR(100)   NOT NULL DEFAULT '',
    avatar       VARCHAR(255)   NOT NULL DEFAULT '',
    gender       SMALLINT       NOT NULL,
    location     VARCHAR(255)   NOT NULL DEFAULT '',
    enabled      BOOLEAN        NOT NULL DEFAULT TRUE,
    verified     BOOLEAN        NOT NULL DEFAULT FALSE,
    roles        JSON           NOT NULL,
    locale       VARCHAR(10)    NOT NULL,
    created_at   TIMESTAMPTZ(6) NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ(6) NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX user_info_idx_email ON user_info (email) WHERE email IS NOT NULL;
CREATE UNIQUE INDEX user_info_idx_phone ON user_info (phone) WHERE phone IS NOT NULL;


CREATE TABLE user_connection
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    local_user_id      VARCHAR(64)    NOT NULL,
    provider           VARCHAR(100)   NOT NULL,
    sub                VARCHAR(100)   NOT NULL,
    name               VARCHAR(100)   NOT NULL DEFAULT '',
    preferred_username VARCHAR(100)   NOT NULL DEFAULT '',
    nickname           VARCHAR(100)   NOT NULL DEFAULT '',
    picture            VARCHAR(255)   NOT NULL DEFAULT '',
    email              VARCHAR(100)   NOT NULL DEFAULT '',
    phone_number       VARCHAR(100)   NOT NULL DEFAULT '',
    created_at         TIMESTAMPTZ(6) NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMPTZ(6) NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX user_connection_idx_local_user_provider ON user_connection (local_user_id, provider, sub);

CREATE TABLE domain_event
(
    event_id    UUID           NOT NULL,
    listener_id VARCHAR(255)   NOT NULL,
    event_type  VARCHAR(255)   NOT NULL,
    payload     JSON           NOT NULL,
    created_at  TIMESTAMPTZ(6) NOT NULL DEFAULT NOW(),
    headers     JSON           NOT NULL,
    PRIMARY KEY (event_id, listener_id)
);
CREATE INDEX domain_event_idx_created_at ON domain_event (created_at);

CREATE TABLE oauth2_authorization_consent
(
    registered_client_id varchar(100)  NOT NULL,
    principal_name       varchar(200)  NOT NULL,
    authorities          varchar(1000) NOT NULL,
    PRIMARY KEY (registered_client_id, principal_name)
);

/*
IMPORTANT:
    If using PostgreSQL, update ALL columns defined with 'text' to 'text',
    as PostgreSQL does not support the 'text' data type.
*/
CREATE TABLE oauth2_authorization
(
    id                            varchar(100) NOT NULL,
    registered_client_id          varchar(100) NOT NULL,
    principal_name                varchar(200) NOT NULL,
    authorization_grant_type      varchar(100) NOT NULL,
    authorized_scopes             varchar(1000) DEFAULT NULL,
    attributes                    text          DEFAULT NULL,
    state                         varchar(500)  DEFAULT NULL,
    authorization_code_value      text          DEFAULT NULL,
    authorization_code_issued_at  timestamp     DEFAULT NULL,
    authorization_code_expires_at timestamp     DEFAULT NULL,
    authorization_code_metadata   text          DEFAULT NULL,
    access_token_value            text          DEFAULT NULL,
    access_token_issued_at        timestamp     DEFAULT NULL,
    access_token_expires_at       timestamp     DEFAULT NULL,
    access_token_metadata         text          DEFAULT NULL,
    access_token_type             varchar(100)  DEFAULT NULL,
    access_token_scopes           varchar(1000) DEFAULT NULL,
    oidc_id_token_value           text          DEFAULT NULL,
    oidc_id_token_issued_at       timestamp     DEFAULT NULL,
    oidc_id_token_expires_at      timestamp     DEFAULT NULL,
    oidc_id_token_metadata        text          DEFAULT NULL,
    refresh_token_value           text          DEFAULT NULL,
    refresh_token_issued_at       timestamp     DEFAULT NULL,
    refresh_token_expires_at      timestamp     DEFAULT NULL,
    refresh_token_metadata        text          DEFAULT NULL,
    user_code_value               text          DEFAULT NULL,
    user_code_issued_at           timestamp     DEFAULT NULL,
    user_code_expires_at          timestamp     DEFAULT NULL,
    user_code_metadata            text          DEFAULT NULL,
    device_code_value             text          DEFAULT NULL,
    device_code_issued_at         timestamp     DEFAULT NULL,
    device_code_expires_at        timestamp     DEFAULT NULL,
    device_code_metadata          text          DEFAULT NULL,
    PRIMARY KEY (id)
);
