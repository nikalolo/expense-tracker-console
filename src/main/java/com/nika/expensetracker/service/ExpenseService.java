package com.nika.expensetracker.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.nika.expensetracker.exception.InvalidExpenseException;
import com.nika.expensetracker.model.Expense;
import com.nika.expensetracker.repository.ExpenseRepository;


public class ExpenseService {

    private final ExpenseRepository repository = new ExpenseRepository();
    private List<Expense> expenses;
    private int nextId;

    public ExpenseService() {
        this.expenses = repository.loadFromFile();

        // Ensure IDs continue correctly after restart
        this.nextId = expenses.stream()
                .mapToInt(Expense::getId)
                .max()
                .orElse(0) + 1;
    }

    public Expense addExpense(String category, BigDecimal amount, LocalDate date) {
        if (category == null || category.isEmpty()) {
            throw new InvalidExpenseException("Category cannot be empty");
        }
        // ✅ Normalize category input
        category = category.trim().toLowerCase();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidExpenseException("Amount must be greater than zero");
        }

        if (date == null) {
            throw new InvalidExpenseException("Date cannot be null");
        }

        Expense expense = new Expense(nextId, category, amount, date);
        nextId++;
        expenses.add(expense);

        repository.saveToFile(expenses); // ✅ save AFTER adding

        return expense;
    }

    public void deleteExpense(int id) {
        Expense toDelete = null;

        for (Expense e : expenses) {
            if (e.getId() == id) {
                toDelete = e;
                break;
            }
        }

        if (toDelete == null) {
            throw new InvalidExpenseException("Expense with ID " + id + " not found");
        }

        expenses.remove(toDelete);           // ✅ remove first
        repository.saveToFile(expenses);     // ✅ save AFTER deletion
    }

    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses);
    }

    public List<Expense> filterByCategory(String category) {
        if (category == null || category.isEmpty()) {
            throw new InvalidExpenseException("Category cannot be empty");
        }

        List<Expense> result = new ArrayList<>();

        for (Expense e : expenses) {
            if (e.getCategory().equalsIgnoreCase(category)) {
                result.add(e);
            }
        }

        return result;
    }

    public BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (Expense e : expenses) {
            total = total.add(e.getAmount());
        }

        return total;
    }

    public List<Expense> sortByDateDesc() {
        List<Expense> sorted = new ArrayList<>(expenses);
        sorted.sort((e1, e2) -> e2.getDate().compareTo(e1.getDate()));
        return sorted;
    }

    public List<Expense> sortByAmountDesc() {
        List<Expense> sorted = new ArrayList<>(expenses);
        sorted.sort((e1, e2) -> e2.getAmount().compareTo(e1.getAmount()));
        return sorted;
    }
}