CREATE TABLE agenda
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(60)  NOT NULL,
    description VARCHAR(255) NOT NULL,
    opened_date TIMESTAMP,
    limit_date  TIMESTAMP
);

CREATE TABLE vote
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    agenda_id    BIGINT      NOT NULL,
    associate_id VARCHAR(60) NOT NULL,
    vote_option  ENUM('YES', 'NO') NOT NULL,

    CONSTRAINT fk_vote_agenda
        FOREIGN KEY (agenda_id)
            REFERENCES agenda (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_vote_agenda
    ON vote (agenda_id);

ALTER TABLE vote
    ADD CONSTRAINT uk_vote_agenda_associate
        UNIQUE (agenda_id, associate_id);