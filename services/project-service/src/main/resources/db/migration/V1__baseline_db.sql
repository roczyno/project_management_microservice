CREATE TABLE project
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(255),
    category    VARCHAR(255),
    user_id     INTEGER,
    created_at  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_project PRIMARY KEY (id)
);
