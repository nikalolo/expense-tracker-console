package com.nika.expensetracker.repository;

import com.nika.expensetracker.model.Expense;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseRepository {

    private static final String FILE_PATH = "expenses.txt"; // file in project root


    // Step 3: Save expenses to file
    public void saveToFile(List<Expense> expenses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Expense expense : expenses) {
                writer.write(expense.getId() + ","
                        + expense.getCategory() + ","
                        + expense.getAmount() + ","
                        + expense.getDate());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Step 2: Safe stub for loading
    public List<Expense> loadFromFile() {
        List<Expense> expenses = new ArrayList<>();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return expenses; // return empty list if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",");

                    int id = Integer.parseInt(parts[0]);
                    String category = parts[1];
                    BigDecimal amount = new BigDecimal(parts[2]);
                    LocalDate date = LocalDate.parse(parts[3]);

                    Expense expense = new Expense(id, category, amount, date);
                    expenses.add(expense);

                } catch (Exception e) {
                    // corrupted line -> skip it, don't crash app
                    System.err.println("Skipping corrupted line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return expenses;
    }
    public void saveExpense(Expense expense) {
        String sql = "INSERT INTO expenses (id, category, amount, date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, expense.getId());
            stmt.setString(2, expense.getCategory());
            stmt.setBigDecimal(3, expense.getAmount());
            stmt.setDate(4, Date.valueOf(expense.getDate()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Expense> getAllExpenses() {

        List<Expense> expenses = new ArrayList<>();

        String sql = "SELECT id, category, amount, date FROM expenses";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("id");
                String category = rs.getString("category");
                BigDecimal amount = rs.getBigDecimal("amount");
                LocalDate date = rs.getDate("date").toLocalDate();

                Expense expense = new Expense(id, category, amount, date);

                expenses.add(expense);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }
}