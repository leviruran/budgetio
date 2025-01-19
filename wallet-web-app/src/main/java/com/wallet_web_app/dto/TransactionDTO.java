package com.wallet_web_app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;          // Optional: Include for responses
    private Double amount;
    private String type;      // "income" or "expense"
    private String account;   // e.g., "bank", "mobile", "cash"
    private String category;
    private String subcategory;
    private LocalDate date;
}
