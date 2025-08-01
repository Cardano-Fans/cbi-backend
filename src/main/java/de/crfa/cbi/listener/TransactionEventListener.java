package de.crfa.cbi.listener;

import com.bloxbean.cardano.yaci.store.events.TransactionEvent;
import de.crfa.cbi.entity.TransactionDayCount;
import de.crfa.cbi.entity.TransactionEpochCount;
import de.crfa.cbi.repository.TransactionDayCountRepository;
import de.crfa.cbi.repository.TransactionEpochCountRepository;
import de.crfa.cbi.repository.TransactionSummaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
public class TransactionEventListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionEventListener.class);

    @Autowired
    private TransactionDayCountRepository transactionDayCountRepository;
    
    @Autowired
    private TransactionEpochCountRepository transactionEpochCountRepository;
    
    @Autowired
    private TransactionSummaryRepository transactionSummaryRepository;

    @EventListener
    @Transactional
    public void handleTransactionEvent(TransactionEvent event) {
        try {
            int transactionCount = event.getTransactions() != null ? event.getTransactions().size() : 0;
            if (transactionCount == 0) {
                return; // No transactions to process
            }

            log.info("Processing transaction event with {} transactions in block {}",
                     transactionCount, event.getMetadata().getBlockHash());
            
            // Update daily count
            updateDayCount(event, transactionCount);
            // Update epoch count
            updateEpochCount(event, transactionCount);
            // Update summary count
            updateSummaryCount(event, transactionCount);
        } catch (Exception e) {
            log.error("Error processing transaction event for block: {}", 
                     event.getMetadata().getBlockHash(), e);
        }
    }

    private void updateDayCount(TransactionEvent event, int transactionCount) {
        LocalDate date = Instant.ofEpochSecond(event.getMetadata().getBlockTime())
            .atZone(ZoneOffset.UTC)
            .toLocalDate();
        
        TransactionDayCount dayCount = transactionDayCountRepository.findById(date)
            .orElse(new TransactionDayCount(date, 0L));
        
        dayCount.setTransactionCount(dayCount.getTransactionCount() + transactionCount);
        dayCount.setLastUpdated(System.currentTimeMillis());
        
        transactionDayCountRepository.save(dayCount);

        log.debug("Updated day count for {}: {} (+{})", date, dayCount.getTransactionCount(), transactionCount);
    }

    private void updateEpochCount(TransactionEvent event, int transactionCount) {
        Integer epoch = event.getMetadata().getEpochNumber();
        
        TransactionEpochCount epochCount = transactionEpochCountRepository.findById(epoch)
            .orElse(new TransactionEpochCount(epoch, 0L));
        
        epochCount.setTransactionCount(epochCount.getTransactionCount() + transactionCount);
        epochCount.setLastUpdated(System.currentTimeMillis());
        
        transactionEpochCountRepository.save(epochCount);
        log.trace("Updated epoch count for {}: {} (+{})", epoch, epochCount.getTransactionCount(), transactionCount);
    }

    private void updateSummaryCount(TransactionEvent event, int transactionCount) {
        LocalDate date = Instant.ofEpochSecond(event.getMetadata().getBlockTime())
            .atZone(ZoneOffset.UTC)
            .toLocalDate();
        
        long timestamp = System.currentTimeMillis();
        
        try {
            // Fetch existing summary or create new one
            var summary = transactionSummaryRepository.findOrCreateSummary();
            
            // Update total count
            summary.setTotalTransactionCount(summary.getTotalTransactionCount() + transactionCount);
            
            // Update first date if needed
            if (summary.getFirstTransactionDate() == null || date.isBefore(summary.getFirstTransactionDate())) {
                summary.setFirstTransactionDate(date);
            }
            
            // Update last date if needed  
            if (summary.getLastTransactionDate() == null || date.isAfter(summary.getLastTransactionDate())) {
                summary.setLastTransactionDate(date);
            }
            
            // Update timestamp
            summary.setLastUpdated(timestamp);
            
            // Save the updated entity
            transactionSummaryRepository.save(summary);
            
            log.info("Updated transaction summary: +{} transactions for {} (total: {})",
                     transactionCount, date, summary.getTotalTransactionCount());
                     
        } catch (Exception e) {
            log.error("Failed to update transaction summary for {} transactions on {}", transactionCount, date, e);
        }
    }
}