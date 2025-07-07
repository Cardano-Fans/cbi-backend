package de.crfa.cbi.repository;

import de.crfa.cbi.entity.TransactionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TransactionSummaryRepository extends JpaRepository<TransactionSummary, Integer> {
    
    @Query("SELECT ts FROM TransactionSummary ts WHERE ts.id = 1")
    Optional<TransactionSummary> findSummary();
    
    default TransactionSummary findOrCreateSummary() {
        return findSummary().orElseGet(() -> {
            TransactionSummary summary = new TransactionSummary();
            summary.setId(1);
            summary.setTotalTransactionCount(0L);
            summary.setLastUpdated(System.currentTimeMillis());

            return save(summary);
        });
    }

}