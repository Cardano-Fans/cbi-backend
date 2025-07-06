package de.crfa.cbi.controller;

import de.crfa.cbi.service.TransactionCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TransactionCountController {

    private final TransactionCountService transactionCountService;
    
    @GetMapping("/count/date/{date}")
    public ResponseEntity<Map<String, Long>> getTransactionCountByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Map<String, Long> result = transactionCountService.getTransactionCountsByDate(date);

        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/count/date-range")
    public ResponseEntity<Map<String, Long>> getTransactionCountByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Long> result = transactionCountService.getTransactionCountsByDateRange(startDate, endDate);

        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/count/epoch/{epoch}")
    public ResponseEntity<Map<String, Long>> getTransactionCountByEpoch(
            @PathVariable Integer epoch) {
        Map<String, Long> result = transactionCountService.getTransactionCountsByEpoch(epoch);

        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/count/epoch-range")
    public ResponseEntity<Map<String, Long>> getTransactionCountByEpochRange(
            @RequestParam Integer startEpoch,
            @RequestParam Integer endEpoch) {
        Map<String, Long> result = transactionCountService.getTransactionCountsByEpochRange(startEpoch, endEpoch);

        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/reset/date-range")
    public ResponseEntity<String> resetDayCountsForDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        transactionCountService.resetDayCountsForDateRange(startDate, endDate);

        return ResponseEntity.ok("Day counts reset successfully");
    }
    
    @PostMapping("/reset/epoch-range")
    public ResponseEntity<String> resetEpochCountsForRange(
            @RequestParam Integer startEpoch,
            @RequestParam Integer endEpoch) {
        transactionCountService.resetEpochCountsForRange(startEpoch, endEpoch);

        return ResponseEntity.ok("Epoch counts reset successfully");
    }

}