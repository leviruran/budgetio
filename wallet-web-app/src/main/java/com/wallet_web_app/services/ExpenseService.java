package com.wallet_web_app.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet_web_app.entity.Transaction;
import com.wallet_web_app.repository.TransactionRepository;

@Service
public class ExpenseService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getExpenses(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByTypeAndDateBetween("expense", startDate, endDate);
    }
    
    public Double getTotalExpenses(LocalDate startDate, LocalDate endDate) {
        return transactionRepository.findByTypeAndDateBetween("expense", startDate, endDate)
                                    .stream()
                                    .mapToDouble(Transaction::getAmount)
                                    .sum();
    }
}
