-- Create transaction day count table
CREATE TABLE transaction_day_count (
    date DATE PRIMARY KEY,
    transaction_count BIGINT NOT NULL,
    last_updated BIGINT
);

-- Create transaction epoch count table
CREATE TABLE transaction_epoch_count (
    epoch INTEGER PRIMARY KEY,
    transaction_count BIGINT NOT NULL,
    last_updated BIGINT
);

-- Create indexes for better performance
CREATE INDEX idx_transaction_day_count_date ON transaction_day_count(date);
CREATE INDEX idx_transaction_epoch_count_epoch ON transaction_epoch_count(epoch);