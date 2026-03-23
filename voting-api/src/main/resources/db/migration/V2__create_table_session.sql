CREATE TABLE session (
    id BIGSERIAL PRIMARY KEY,
    agenda_id BIGINT NOT NULL,
    opening_date TIMESTAMP NOT NULL,
    closing_date TIMESTAMP NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,

    FOREIGN KEY (agenda_id) REFERENCES agenda(id)
);