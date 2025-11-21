CREATE TABLE users
(
    id         UUID         NOT NULL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(255) CHECK (role IN ('ADMIN', 'USER')),
    enabled    BOOLEAN      NOT NULL DEFAULT true,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
);