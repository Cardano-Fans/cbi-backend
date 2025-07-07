-- Add performance indexes for transaction queries

-- Index for date range queries (already exists but adding for completeness)
CREATE INDEX IF NOT EXISTS idx_transaction_day_count_date ON transaction_day_count(date);

-- Index for transaction count queries (covering index)
CREATE INDEX IF NOT EXISTS idx_transaction_day_count_date_count ON transaction_day_count(date, transaction_count);

-- Index for epoch range queries (already exists but adding for completeness)  
CREATE INDEX IF NOT EXISTS idx_transaction_epoch_count_epoch ON transaction_epoch_count(epoch);

-- Index for epoch count queries (covering index)
CREATE INDEX IF NOT EXISTS idx_transaction_epoch_count_epoch_count ON transaction_epoch_count(epoch, transaction_count);

-- Index for summary table fast lookups (already exists but adding for completeness)
CREATE INDEX IF NOT EXISTS idx_transaction_summary_dates ON transaction_summary(first_transaction_date, last_transaction_date);

-- Index for timestamp-based queries
CREATE INDEX IF NOT EXISTS idx_transaction_day_count_last_updated ON transaction_day_count(last_updated);
CREATE INDEX IF NOT EXISTS idx_transaction_epoch_count_last_updated ON transaction_epoch_count(last_updated);

-- Partial index for non-zero counts only (more efficient)
CREATE INDEX IF NOT EXISTS idx_transaction_day_count_nonzero ON transaction_day_count(date) WHERE transaction_count > 0;
CREATE INDEX IF NOT EXISTS idx_transaction_epoch_count_nonzero ON transaction_epoch_count(epoch) WHERE transaction_count > 0;