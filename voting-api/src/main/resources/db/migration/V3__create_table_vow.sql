CREATE TABLE vow (
    id BIGSERIAL PRIMARY KEY,
    associated_id VARCHAR(50) NOT NULL,
    vote VARCHAR(10) NOT NULL,
    agenda_id BIGINT NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,

    FOREIGN KEY (agenda_id) REFERENCES agenda(id)
);