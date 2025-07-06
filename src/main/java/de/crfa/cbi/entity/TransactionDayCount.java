package de.crfa.cbi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "transaction_day_count")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionDayCount {
    
    @Id
    @Column(name = "date")
    private LocalDate date;
    
    @Column(name = "transaction_count", nullable = false)
    private Long transactionCount;
    
    @Column(name = "last_updated")
    private Long lastUpdated;
    
    public TransactionDayCount(LocalDate date,
                               Long transactionCount) {
        this.date = date;
        this.transactionCount = transactionCount;
        this.lastUpdated = System.currentTimeMillis();
    }

}
