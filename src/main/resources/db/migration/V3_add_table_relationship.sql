ALTER TABLE projects
    ADD COLUMN owner_id UUID NOT NULL REFERENCES users (id);

ALTER TABLE tasks
    ADD COLUMN assignee_id UUID NOT NULL REFERENCES users (id);