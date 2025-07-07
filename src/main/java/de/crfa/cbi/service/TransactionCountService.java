package de.crfa.cbi.service;

import de.crfa.cbi.entity.TransactionDayCount;
import de.crfa.cbi.entity.TransactionEpochCount;
import de.crfa.cbi.entity.TransactionSummary;
import de.crfa.cbi.repository.TransactionDayCountRepository;
import de.crfa.cbi.repository.TransactionEpochCountRepository;
import de.crfa.cbi.repository.TransactionSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionCountService {
    
    private final TransactionDayCountRepository transactionDayCountRepository;
    private final TransactionEpochCountRepository transactionEpochCountRepository;
    private final TransactionSummaryRepository transactionSummaryRepository;
    
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

    @Transactional
    public void resetDayCountsForDateRange(LocalDate startDate, LocalDate endDate) {
        List<TransactionDayCount> existingCounts = transactionDayCountRepository.findByDateBetween(startDate, endDate);
        for (TransactionDayCount count : existingCounts) {
            count.setTransactionCount(0L);
            count.setLastUpdated(System.currentTimeMillis());
            transactionDayCountRepository.save(count);
        }
    }

    @Transactional
    public void resetEpochCountsForRange(Integer startEpoch, Integer endEpoch) {
        List<TransactionEpochCount> existingCounts = transactionEpochCountRepository.findByEpochBetween(startEpoch, endEpoch);
        for (TransactionEpochCount count : existingCounts) {
            count.setTransactionCount(0L);
            count.setLastUpdated(System.currentTimeMillis());
            transactionEpochCountRepository.save(count);
        }
    }
    
    public List<Integer> getEpochsWithData() {
        return transactionEpochCountRepository.findEpochsWithNonZeroTransactions();
    }

    @CacheEvict(value = "transaction-counts", key = "'total-count'")
    public void evictTotalCountCache() {
        // Cache eviction method to be called when summary is updated
    }

    @Cacheable(value = "transaction-counts", key = "'total-count'")
    public Map<String, Object> getTotalTransactionCount() {
        Map<String, Object> result = new HashMap<>();
        TransactionSummary summary = transactionSummaryRepository.findSummary().orElse(null);
        
        if (summary != null) {
            result.put("totalCount", summary.getTotalTransactionCount());
            result.put("firstTransactionDate", summary.getFirstTransactionDate());
            result.put("lastTransactionDate", summary.getLastTransactionDate());
            result.put("lastUpdated", summary.getLastUpdated());
        } else {
            result.put("totalCount", 0L);
            result.put("firstTransactionDate", null);
            result.put("lastTransactionDate", null);
            result.put("lastUpdated", System.currentTimeMillis());
        }
        
        return result;
    }

    @Cacheable(value = "transaction-counts", key = "'date-range-' + #startDate + '-' + #endDate")
    public Map<String, Object> getTransactionCountSummary(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();
        
        // Get total count
        Map<String, Object> totalInfo = getTotalTransactionCount();
        result.put("totalCount", totalInfo.get("totalCount"));
        result.put("periodStart", startDate);
        result.put("periodEnd", endDate);
        
        // Get date range counts
        List<TransactionDayCount> counts = transactionDayCountRepository.findByDateBetween(startDate, endDate);
        Map<String, Long> dailyCounts = new HashMap<>();
        Long periodTotal = 0L;
        
        for (TransactionDayCount count : counts) {
            dailyCounts.put(count.getDate().toString(), count.getTransactionCount());
            periodTotal += count.getTransactionCount();
        }
        
        result.put("periodTotal", periodTotal);
        result.put("dailyCounts", dailyCounts);
        result.put("recordCount", counts.size());
        
        return result;
    }

}