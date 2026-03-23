ALTER TABLE vow
ADD CONSTRAINT unique_vote_per_member
UNIQUE (agenda_id, associated_id);

CREATE INDEX idx_vow_agenda ON vow (agenda_id);