package de.crfa.cbi.repository;

import de.crfa.cbi.entity.TransactionDayCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionDayCountRepository extends JpaRepository<TransactionDayCount, LocalDate> {
    
    List<TransactionDayCount> findByDateBetween(LocalDate startDate, LocalDate endDate);
}