package com.wallet_web_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet_web_app.dto.TransactionDTO;
import com.wallet_web_app.entity.Category;
import com.wallet_web_app.entity.Subcategory;
import com.wallet_web_app.entity.Transaction;
import com.wallet_web_app.repository.CategoryRepository;
import com.wallet_web_app.repository.SubcategoryRepository;
import com.wallet_web_app.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public TransactionDTO addTransaction(TransactionDTO transactionDTO) {
        Category category = categoryRepository.findById(Long.parseLong(transactionDTO.getCategory()))
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Subcategory subcategory = subcategoryRepository.findById(Long.parseLong(transactionDTO.getSubcategory()))
                .orElseThrow(() -> new IllegalArgumentException("Subcategory not found"));

        Transaction transaction = new Transaction(
                null,
                transactionDTO.getAmount(),
                transactionDTO.getType(),
                transactionDTO.getAccount(),
                category.getName(),
                subcategory.getName(),
                transactionDTO.getDate()
        );

        transaction = transactionRepository.save(transaction);

        return new TransactionDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getAccount(),
                transaction.getCategory(),
                transaction.getSubcategory(),
                transaction.getDate()
        );
    }


    public List<TransactionDTO> getTransactions(LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByDateBetween(startDate, endDate);
        return transactions.stream()
                .map(tx -> new TransactionDTO(
                        tx.getId(),
                        tx.getAmount(),
                        tx.getType(),
                        tx.getAccount(),
                        tx.getCategory(),
                        tx.getSubcategory(),
                        tx.getDate()))
                .collect(Collectors.toList());
    }
}
