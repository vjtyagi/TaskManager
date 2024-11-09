-- Add NOT NULL constraints to required columns
ALTER TABLE task 
    ALTER COLUMN title SET NOT NULL,
    ALTER COLUMN status SET NOT NULL,
    ALTER COLUMN due_date SET NOT NULL;
-- Add Check constraint to ensure title is not empty
ALTER TABLE task 
    ADD CONSTRAINT check_title_not_empty CHECK (title <> '');
-- Add Check constraint on status to restrict values
ALTER TABLE task 
    ADD CONSTRAINT check_status_values CHECK (status IN ('PENDING', 'IN_PROGRESS', 'COMPLETED'));
-- Add created_at and updated_at timestamp columns
ALTER TABLE task 
    ADD COLUMN created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    ADD COLUMN updated_at TIMESTAMP DEFAULT NOW() NOT NULL;

--Define trigger function to update updated_at on row modification
CREATE OR REPLACE FUNCTION set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Add the trigger to call set_timestamp() before each update
CREATE TRIGGER update_timestamp
BEFORE UPDATE ON task 
FOR EACH ROW 
EXECUTE FUNCTION set_timestamp();