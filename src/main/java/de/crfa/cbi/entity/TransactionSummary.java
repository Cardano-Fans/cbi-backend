package de.crfa.cbi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "transaction_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummary {
    
    @Id
    @Column(name = "id")
    private Integer id = 1;
    
    @Column(name = "total_transaction_count", nullable = false)
    private Long totalTransactionCount = 0L;
    
    @Column(name = "first_transaction_date")
    private LocalDate firstTransactionDate;
    
    @Column(name = "last_transaction_date")
    private LocalDate lastTransactionDate;
    
    @Column(name = "last_updated", nullable = false)
    private Long lastUpdated;
}