CREATE TYPE sleep_quality AS ENUM ('BAD', 'OK', 'GOOD');

CREATE TABLE sleep (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    quality sleep_quality NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);