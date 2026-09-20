ALTER TABLE associado ADD COLUMN cpf VARCHAR(11) NOT NULL;

ALTER TABLE associado ADD CONSTRAINT uk_associado_cpf UNIQUE (cpf);
