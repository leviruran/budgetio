package com.wallet_web_app.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String type; // "income" or "expense"

    @Column(nullable = false)
    private String account; // e.g., "bank", "mobile", "cash"

    @Column(nullable = false)
    private String category;

    private String subcategory;

    @Column(nullable = false)
    private LocalDate date;
}
