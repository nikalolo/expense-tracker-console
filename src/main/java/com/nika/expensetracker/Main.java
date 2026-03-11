package com.nika.expensetracker;

import com.nika.expensetracker.model.Expense;
import com.nika.expensetracker.repository.ExpenseRepository;
import com.nika.expensetracker.service.ExpenseService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName());
        ExpenseService service = new ExpenseService();
        Scanner scanner = new Scanner(System.in);

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/expense_tracker",
                    "root",
                    "NiKanika3006!?$");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Connected!");

        while (true) {
            System.out.println("\n=== Expense Tracker Menu ===");
            System.out.println("1. Add expense");
            System.out.println("2. Delete expense");
            System.out.println("3. List expenses");
            System.out.println("4. Filter by category");
            System.out.println("5. Show total");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                logger.info("Please enter a valid number.");
                continue;
            }

            try {
                switch (choice) {
                    case 1:
                        logger.info("Category: ");
                        String category = scanner.nextLine();

                        logger.info("Amount: ");
                        BigDecimal amount = new BigDecimal(scanner.nextLine());

                        logger.info("Date (YYYY-MM-DD): ");
                        LocalDate date = LocalDate.parse(scanner.nextLine());

                        service.addExpense(category, amount, date);
                        logger.info("Expense added.");
                        break;

                    case 2:
                        logger.info("Expense ID to delete: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        service.deleteExpense(id);
                        logger.info("Expense deleted.");
                        break;

                    case 3:
                        service.getAllExpenses().forEach(System.out::println);
                        break;

                    case 4:
                        logger.info("Category: ");
                        String cat = scanner.nextLine();
                        service.filterByCategory(cat).forEach(System.out::println);
                        break;

                    case 5:
                        logger.info("Total: " + service.calculateTotal());
                        break;

                    case 6:
                        logger.info("Bye!");
                        return;
                    case 7:
                        service.sortByDateDesc().forEach(System.out::println);
                        break;

                    case 8:
                        service.sortByAmountDesc().forEach(System.out::println);
                        break;

                    default:
                        logger.info("Invalid option");
                }
            } catch (Exception e) {
                logger.info("❌ Error: " + e.getMessage());
            }
        }
    }


}


