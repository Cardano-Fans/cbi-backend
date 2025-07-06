package de.crfa.cbi.repository;

import de.crfa.cbi.entity.TransactionEpochCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionEpochCountRepository extends JpaRepository<TransactionEpochCount, Integer> {
    
    List<TransactionEpochCount> findByEpochBetween(Integer startEpoch, Integer endEpoch);
}