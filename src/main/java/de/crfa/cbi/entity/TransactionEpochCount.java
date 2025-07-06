package de.crfa.cbi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction_epoch_count")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEpochCount {
    
    @Id
    @Column(name = "epoch")
    private Integer epoch;
    
    @Column(name = "transaction_count", nullable = false)
    private Long transactionCount;
    
    @Column(name = "last_updated")
    private Long lastUpdated;
    
    public TransactionEpochCount(Integer epoch, Long transactionCount) {
        this.epoch = epoch;
        this.transactionCount = transactionCount;
        this.lastUpdated = System.currentTimeMillis();
    }

    public Integer getEpoch() {
        return epoch;
    }

    public void setEpoch(Integer epoch) {
        this.epoch = epoch;
    }

    public Long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}