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
        int maxWidth = calculateMaxWidth();
        String horizontalLine = "┌" + "─".repeat(maxWidth + 2) + "┐";
        String middleLine = "├" + "─".repeat(maxWidth + 2) + "┤";
        String bottomLine = "└" + "─".repeat(maxWidth + 2) + "┘";

        System.out.println(horizontalLine);
        System.out.printf("│ %-" + (maxWidth) + "s │\n", "THÔNG TIN GIAO DỊCH");
        System.out.println(middleLine);
        System.out.printf("│ %-" + (maxWidth) + "s │\n", "ID: " + transactionId);
        System.out.printf("│ %-" + (maxWidth) + "s │\n", "Tài khoản: " + accountId);
        System.out.printf("│ %-" + (maxWidth) + "s │\n", "Loại: " + type.getVietnameseName());
        System.out.printf("│ %-" + (maxWidth) + "s │\n",
                String.format("Số tiền: %,.2f", amount));
        System.out.printf("│ %-" + (maxWidth) + "s │\n", "Danh mục: " + category.getName());
        System.out.printf("│ %-" + (maxWidth) + "s │\n", "Mô tả: " + description);
        System.out.printf("│ %-" + (maxWidth) + "s │\n", "Thời gian: " + getFormattedDate());
        System.out.println(bottomLine);
    }

    private int calculateMaxWidth() {
        int maxWidth = "THÔNG TIN GIAO DỊCH".length();

        // Tính độ dài của từng dòng content
        String[] contents = {
                "ID: " + transactionId,
                "Tài khoản: " + accountId,
                "Loại: " + type.getVietnameseName(),
                String.format("Số tiền: %,.2f", amount),
                "Danh mục: " + category.getName(),
                "Mô tả: " + description,
                "Thời gian: " + getFormattedDate()
        };

        for (String content : contents) {
            if (content.length() > maxWidth) {
                maxWidth = content.length();
            }
        }

        return maxWidth;
    }

    @Override
    public String toString() {
        return String.format("Transaction{id=%s, account=%s, type=%s, amount=%.2f, category=%s}",
                transactionId, accountId, type.getVietnameseName(), amount, category.getName());
    }
}