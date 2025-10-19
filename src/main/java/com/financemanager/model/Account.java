package com.financemanager.model;

import java.util.UUID;

public class Account {
    private String accountId;
    private String accountName;
    private String accountType; // BANK, WALLEL, CASH
    private String accountNumber;
    private double balance;
    private String currency;

    public Account(String accountName, String accountType, String accountNumber, double initialBalance) {
        this.accountId = "ACC_" + UUID.randomUUID().toString().substring(0, 8);
        this.accountName = accountName;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.currency = "VND";
    }

    // Nhap thong tin tai khoan
    public String getAccountId() { return accountId; }
    public String getAccountName() { return accountName; }
    public String getAccountType() { return accountType; }
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getCurrency() { return currency; }

    // Phuong thuc tao lap
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Business methods
    // Nhận tiền
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    // Chuyển tiền
    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    //Cập Nhật
    public void updateBalance(double amount, TransactionType type) {
        if (type == TransactionType.INCOME) {
            this.balance += amount;
        } else if (type == TransactionType.EXPENSE) {
            this.balance -= amount;
        }
    }

    // Validation
    public boolean isValid() {
        return accountName != null && !accountName.trim().isEmpty() &&    // Tên không trống
                accountType != null && !accountType.trim().isEmpty() &&   // Loại khoản hơp lệ
                balance >= 0;   //Số dư không âm
    }

    // Display
    public void displayInfo() {
        // Tính độ rộng động dựa trên nội dung
        int maxWidth = calculateMaxWidth();
        String horizontalLine = "┌" + "─".repeat(maxWidth + 2) + "┐";
        String middleLine = "├" + "─".repeat(maxWidth + 2) + "┤";
        String bottomLine = "└" + "─".repeat(maxWidth + 2) + "┘";

        System.out.println(horizontalLine);
        System.out.printf("│ %-" + (maxWidth) + "s │\n", "THÔNG TIN TÀI KHOẢN");
        System.out.println(middleLine);
        System.out.printf("│ %-" + (maxWidth) + "s │\n", "ID: " + accountId);
        System.out.printf("│ %-" + (maxWidth) + "s │\n", "Tên: " + accountName);
        System.out.printf("│ %-" + (maxWidth) + "s │\n", "Loại: " + accountType);
        System.out.printf("│ %-" + (maxWidth) + "s │\n", "Số TK: " + accountNumber);
        System.out.printf("│ %-" + (maxWidth) + "s │\n",
                String.format("Số dư: %,.2f %s", balance, currency));
        System.out.println(bottomLine);
    }

    private int calculateMaxWidth() {
        int minWidth = 40;
        int calculatedWidth = Math.max(
                "💰 THÔNG TIN TÀI KHOẢN".length(),
                Math.max(
                        ("ID: " + accountId).length(),
                        Math.max(
                                ("Tên: " + accountName).length(),
                                Math.max(
                                        ("Loại: " + accountType).length(),
                                        Math.max(
                                                ("Số TK: " + accountNumber).length(),
                                                ("Số dư: " + String.format("%,.2f %s", balance, currency)).length()
                                        )
                                )
                        )
                )
        );
        return Math.max(minWidth, calculatedWidth + 2);
    }

    @Override
    public String toString() {
        return String.format("Account{id=%s, name=%s, type=%s, balance=%.2f %s}",
                accountId, accountName, accountType, balance, currency);
    }
}