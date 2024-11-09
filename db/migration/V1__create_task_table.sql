CREATE TABLE IF NOT EXISTS Task (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50),
    due_date DATE
)