CREATE TABLE agenda (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);