package com.nika.expensetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Expense {

    private int id;
    private String category;
    private BigDecimal amount;
    private LocalDate date;



    public Expense() {
    }

    public Expense(int id, String category, BigDecimal amount, LocalDate date) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "model.Expense{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
