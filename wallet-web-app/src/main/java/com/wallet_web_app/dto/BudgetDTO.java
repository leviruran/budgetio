package com.wallet_web_app.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetDTO {
    private Long id;
    private Double amount;
    private Boolean notifyIfExceeded;
    private String startDate;
    private String endDate;
}
