CREATE TABLE projects
(
    id          UUID         NOT NULL PRIMARY KEY ,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    start_date  DATE         NOT NULL,
    end_date    DATE,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE tasks
(
    id          UUID         NOT NULL PRIMARY KEY,
    title       VARCHAR(150) NOT NULL,
    description VARCHAR(255),
    status      VARCHAR(255) CHECK (status IN ('TODO', 'DOING', 'DONE')),
    priority    VARCHAR(255) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    due_date    DATE,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    updated_at  TIMESTAMP WITHOUT TIME ZONE,
    project_id  UUID         NOT NULL,
    CONSTRAINT fk_tasks_on_project FOREIGN KEY (project_id) REFERENCES projects (id)
);