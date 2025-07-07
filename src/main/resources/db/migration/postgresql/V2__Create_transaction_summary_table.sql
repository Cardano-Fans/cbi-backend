-- Create transaction summary table for fast total counts
CREATE TABLE transaction_summary (
    id INTEGER PRIMARY KEY DEFAULT 1,
    total_transaction_count BIGINT NOT NULL DEFAULT 0,
    first_transaction_date DATE,
    last_transaction_date DATE,
    last_updated BIGINT NOT NULL,
    
    CONSTRAINT single_summary_row CHECK (id = 1)
);

-- Create index for fast lookups
CREATE INDEX idx_transaction_summary_dates ON transaction_summary(first_transaction_date, last_transaction_date);

-- Insert initial row
INSERT INTO transaction_summary (id, total_transaction_count, last_updated) 
VALUES (1, 0, EXTRACT(EPOCH FROM NOW()) * 1000);