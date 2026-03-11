package com.nika.expensetracker.exception;

public class InvalidExpenseException extends RuntimeException {
    public InvalidExpenseException(String message) {
        super(message);
    }

    // Optional: default constructor
    public InvalidExpenseException() {
        super("Invalid expense data provided");
    }

}
