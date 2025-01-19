package com.wallet_web_app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wallet_web_app.entity.Transaction;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Transaction> findByTypeAndDateBetween(String type, LocalDate startDate, LocalDate endDate);

}
