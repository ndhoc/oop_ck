package com.financemanager.model;

import java.util.UUID;

public class Account {
    private String accountId;
    private String accountName;
    private String accountType; // BANK, WALLET, etc.
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

    // Getter methods
    public String getAccountId() { return accountId; }
    public String getAccountName() { return accountName; }
    public String getAccountType() { return accountType; }
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getCurrency() { return currency; }

    // Setter methods
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public void setBalance(double balance) { this.balance = balance; }

    // Business methods
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public void updateBalance(double amount, TransactionType type) {
        if (type == TransactionType.INCOME) {
            this.balance += amount;
        } else if (type == TransactionType.EXPENSE) {
            this.balance -= amount;
        }
    }

    // Validation
    public boolean isValid() {
        return accountName != null && !accountName.trim().isEmpty() &&
                accountType != null && !accountType.trim().isEmpty() &&
                balance >= 0;
    }

    // Display
    public void displayInfo() {
        System.out.println("┌─────────────────────────────────────┐");
        System.out.printf("│ ID: %-34s │\n", accountId);
        System.out.printf("│ Ten: %-33s │\n", accountName);
        System.out.printf("│ Loai: %-32s │\n", accountType);
        System.out.printf("│ So TK: %-31s │\n", accountNumber);
        System.out.printf("│ So du: %-28.2f %s │\n", balance, currency);
        System.out.println("└─────────────────────────────────────┘");
    }

    @Override
    public String toString() {
        return String.format("Account{id=%s, name=%s, type=%s, balance=%.2f %s}",
                accountId, accountName, accountType, balance, currency);
    }
}