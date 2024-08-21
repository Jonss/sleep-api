CREATE TABLE users (
       id SERIAL PRIMARY KEY,
       external_id varchar(36) NOT NULL
);

CREATE INDEX IF NOT EXISTS users_external_id_idx ON users(external_id);