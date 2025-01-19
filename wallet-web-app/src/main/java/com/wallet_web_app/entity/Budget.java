package com.wallet_web_app.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Boolean notifyIfExceeded;

    @Column(nullable = false, columnDefinition = "DATE DEFAULT '2025-01-01'")
    private LocalDate startDate;

    @Column(nullable = false, columnDefinition = "DATE DEFAULT '2025-01-31'")
    private LocalDate endDate;

}
