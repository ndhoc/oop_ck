package com.financemanager.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private String transactionId;
    private String accountId;
    private TransactionType type;
    private double amount;
    private LocalDateTime date;
    private String description;
    private Category category;

    public Transaction(String accountId, TransactionType type, double amount,
                       String description, Category category) {
        this.transactionId = "TRX_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.date = LocalDateTime.now();
    }

    // Getter methods
    public String getTransactionId() { return transactionId; }
    public String getAccountId() { return accountId; }
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getDate() { return date; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }

    // Setter methods
    public void setType(TransactionType type) { this.type = type; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(Category category) { this.category = category; }

    // Business methods
    public boolean isValid() {
        return accountId != null && !accountId.trim().isEmpty() &&
                type != null && amount > 0 && category != null;
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return date.format(formatter);
    }

    // Display
    public void displayInfo() {
        System.out.println("┌─────────────────────────────────────┐");
        System.out.printf("│ ID: %-34s │\n", transactionId);
        System.out.printf("│ Tai khoan: %-26s │\n", accountId);
        System.out.printf("│ Loai: %-32s │\n", type.getVietnameseName());
        System.out.printf("│ So tien: %-28.2f │\n", amount);
        System.out.printf("│ Danh muc: %-27s │\n", category.getName());
        System.out.printf("│ Mo ta: %-30s │\n", description);
        System.out.printf("│ Thoi gian: %-26s │\n", getFormattedDate());
        System.out.println("└─────────────────────────────────────┘");
    }

    @Override
    public String toString() {
        return String.format("Transaction{id=%s, account=%s, type=%s, amount=%.2f, category=%s}",
                transactionId, accountId, type.getVietnameseName(), amount, category.getName());
    }
}