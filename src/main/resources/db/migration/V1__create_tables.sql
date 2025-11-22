-- TYPE OF ENUMS
CREATE TYPE role_enum AS ENUM ('ADMIN', 'USER');
CREATE TYPE status_enum AS ENUM ('TODO', 'DOING', 'DONE');
CREATE TYPE priority_enum AS ENUM ('LOW', 'MEDIUM', 'HIGH');

-- TABLES DEFINITIONS
CREATE TABLE users
(
    id         UUID         NOT NULL DEFAULT gen_random_uuid(),
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    enabled    BOOLEAN      NOT NULL DEFAULT TRUE,
    role       role_enum    NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE projects
(
    id          UUID         NOT NULL DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    start_date  DATE         NOT NULL,
    end_date    DATE,
    owner_id    UUID         NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT pk_projects PRIMARY KEY (id),
    CONSTRAINT fk_projects_on_owner FOREIGN KEY (owner_id) REFERENCES users (id)
);

CREATE TABLE tasks
(
    id          UUID         NOT NULL DEFAULT gen_random_uuid(),
    title       VARCHAR(150) NOT NULL,
    description VARCHAR(255),
    status      status_enum,
    priority    priority_enum,
    due_date    DATE,
    assignee_id UUID         NOT NULL,
    project_id  UUID         NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT pk_tasks PRIMARY KEY (id),
    CONSTRAINT fk_tasks_on_assignee FOREIGN KEY (assignee_id) REFERENCES users (id),
    CONSTRAINT fk_tasks_on_project FOREIGN KEY (project_id) REFERENCES projects (id)
);

CREATE TABLE user_projects
(
    project_id UUID NOT NULL,
    user_id    UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    updated_at TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT pk_user_projects PRIMARY KEY (project_id, user_id),
    CONSTRAINT fk_user_projects_on_project FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_projects_on_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);