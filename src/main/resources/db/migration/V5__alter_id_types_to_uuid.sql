CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- alterar associado
ALTER TABLE associado 
    ADD COLUMN new_id UUID DEFAULT uuid_generate_v4();
    
-- copiar dados existentes
UPDATE associado SET new_id = uuid_generate_v4();

-- remover constraints antigas
ALTER TABLE voto DROP CONSTRAINT IF EXISTS voto_associado_id_fkey;

-- alterar tipo da coluna associado_id em voto
ALTER TABLE voto 
    ALTER COLUMN associado_id TYPE UUID USING uuid_generate_v4();

-- dropar coluna antiga
ALTER TABLE associado DROP COLUMN id;

-- tornar new_id not null
ALTER TABLE associado ALTER COLUMN new_id SET NOT NULL;

-- renomear coluna
ALTER TABLE associado RENAME COLUMN new_id TO id;

-- adicionar constraint primary key
ALTER TABLE associado ADD PRIMARY KEY (id);