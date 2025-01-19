package com.wallet_web_app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wallet_web_app.entity.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
