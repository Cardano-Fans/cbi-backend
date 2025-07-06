package de.crfa.cbi.service;

import de.crfa.cbi.entity.TransactionDayCount;
import de.crfa.cbi.entity.TransactionEpochCount;
import de.crfa.cbi.repository.TransactionDayCountRepository;
import de.crfa.cbi.repository.TransactionEpochCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionCountService {
    
    @Autowired
    private TransactionDayCountRepository transactionDayCountRepository;
    
    @Autowired
    private TransactionEpochCountRepository transactionEpochCountRepository;
    
    public Map<String, Long> getTransactionCountsByDate(LocalDate date) {
        Map<String, Long> result = new HashMap<>();
        TransactionDayCount dayCount = transactionDayCountRepository.findById(date).orElse(null);
        if (dayCount != null) {
            result.put(date.toString(), dayCount.getTransactionCount());
        }
        return result;
    }
    
    public Map<String, Long> getTransactionCountsByDateRange(LocalDate startDate, LocalDate endDate) {
        Map<String, Long> result = new HashMap<>();
        List<TransactionDayCount> counts = transactionDayCountRepository.findByDateBetween(startDate, endDate);
        for (TransactionDayCount count : counts) {
            result.put(count.getDate().toString(), count.getTransactionCount());
        }
        return result;
    }
    
    public Map<String, Long> getTransactionCountsByEpoch(Integer epoch) {
        Map<String, Long> result = new HashMap<>();
        TransactionEpochCount epochCount = transactionEpochCountRepository.findById(epoch).orElse(null);
        if (epochCount != null) {
            result.put(epoch.toString(), epochCount.getTransactionCount());
        }
        return result;
    }
    
    public Map<String, Long> getTransactionCountsByEpochRange(Integer startEpoch, Integer endEpoch) {
        Map<String, Long> result = new HashMap<>();
        List<TransactionEpochCount> counts = transactionEpochCountRepository.findByEpochBetween(startEpoch, endEpoch);
        for (TransactionEpochCount count : counts) {
            result.put(count.getEpoch().toString(), count.getTransactionCount());
        }
        return result;
    }
    
    public void resetDayCountsForDateRange(LocalDate startDate, LocalDate endDate) {
        List<TransactionDayCount> existingCounts = transactionDayCountRepository.findByDateBetween(startDate, endDate);
        for (TransactionDayCount count : existingCounts) {
            count.setTransactionCount(0L);
            count.setLastUpdated(System.currentTimeMillis());
            transactionDayCountRepository.save(count);
        }
    }
    
    public void resetEpochCountsForRange(Integer startEpoch, Integer endEpoch) {
        List<TransactionEpochCount> existingCounts = transactionEpochCountRepository.findByEpochBetween(startEpoch, endEpoch);
        for (TransactionEpochCount count : existingCounts) {
            count.setTransactionCount(0L);
            count.setLastUpdated(System.currentTimeMillis());
            transactionEpochCountRepository.save(count);
        }
    }
}