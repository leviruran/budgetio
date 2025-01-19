package com.wallet_web_app.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet_web_app.dto.BudgetDTO;
import com.wallet_web_app.entity.Budget;
import com.wallet_web_app.repository.BudgetRepository;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public BudgetDTO setBudget(BudgetDTO budgetDTO) {
        Budget budget = new Budget(
            null,
            budgetDTO.getAmount(),
            budgetDTO.getNotifyIfExceeded(),
            LocalDate.parse(budgetDTO.getStartDate()), // Convert String to LocalDate
            LocalDate.parse(budgetDTO.getEndDate())
        );
        budget = budgetRepository.save(budget);
        return new BudgetDTO(
            budget.getId(),
            budget.getAmount(),
            budget.getNotifyIfExceeded(),
            budget.getStartDate().toString(), // Convert LocalDate to String
            budget.getEndDate().toString()
        );
    }

    public BudgetDTO getBudget() {
        Budget budget = budgetRepository.findAll().stream().findFirst().orElse(null);
        if (budget == null) {
            return null;
        }
        return new BudgetDTO(
            budget.getId(),
            budget.getAmount(),
            budget.getNotifyIfExceeded(),
            budget.getStartDate().toString(),
            budget.getEndDate().toString()
        );
    }
}
