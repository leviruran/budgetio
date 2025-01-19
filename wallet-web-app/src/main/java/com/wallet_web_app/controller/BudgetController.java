package com.wallet_web_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wallet_web_app.dto.BudgetDTO;
import com.wallet_web_app.services.BudgetService;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/add")
    public ResponseEntity<BudgetDTO> setBudget(@RequestBody BudgetDTO budgetDTO) {
        BudgetDTO updatedBudget = budgetService.setBudget(budgetDTO);
        return ResponseEntity.ok(updatedBudget);
    }

    @GetMapping("/get")
    public ResponseEntity<BudgetDTO> getBudget() {
        BudgetDTO budget = budgetService.getBudget();
        return ResponseEntity.ok(budget);
    }
}
