-- User table
CREATE TABLE user_info
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username     VARCHAR(50)  NOT NULL UNIQUE,
    password     VARCHAR(100) NOT NULL DEFAULT '',
    email        VARCHAR(100),
    mobile       VARCHAR(15),
    display_name VARCHAR(100) NOT NULL,
    avatar       VARCHAR(255) NOT NULL DEFAULT '',
    gender       SMALLINT     NOT NULL,
    location     VARCHAR(255) NOT NULL DEFAULT '',
    enabled      BOOLEAN      NOT NULL DEFAULT TRUE,
    verified     BOOLEAN      NOT NULL DEFAULT FALSE,
    roles        JSON         NOT NULL,
    locale       VARCHAR(10)  NOT NULL,
    created_at   TIMESTAMPTZ(6) NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ(6) NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX user_info_idx_email ON user_info (email) WHERE email IS NOT NULL;
CREATE UNIQUE INDEX user_info_idx_mobile ON user_info (mobile) WHERE mobile IS NOT NULL;