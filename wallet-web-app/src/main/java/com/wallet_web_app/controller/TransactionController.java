package com.wallet_web_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wallet_web_app.dto.TransactionDTO;
import com.wallet_web_app.services.TransactionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity<TransactionDTO> addTransaction(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = transactionService.addTransaction(transactionDTO);
        return ResponseEntity.ok(createdTransaction);
    }

    @GetMapping("/get")
    public ResponseEntity<List<TransactionDTO>> getTransactions(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        List<TransactionDTO> transactions = transactionService.getTransactions(
                LocalDate.parse(startDate), LocalDate.parse(endDate));
        return ResponseEntity.ok(transactions);
    }
}
