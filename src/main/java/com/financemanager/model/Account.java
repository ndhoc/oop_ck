package com.financemanager.model;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;


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
            double oldBalance = this.balance;
            this.balance += amount;
            System.out.println("Da them " + String.format("%,.0f", amount) + " " + currency + " vao tai khoan");
            System.out.println("So du cu: " + String.format("%,.0f", oldBalance) + " " + currency);
            System.out.println("So du moi: " + String.format("%,.0f", this.balance) + " " + currency);
        } else {
            System.out.println("So tien them phai lon hon 0!");
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

    public void generateAccountReport(List<Transaction> allTransactions) {
        List<Transaction> accountTransactions = filterTransactionsByAccount(allTransactions);

        // HIỂN THỊ HEADER VỚI ID TÀI KHOẢN
        String accountHeader = "=".repeat(80);
        System.out.println(accountHeader);
        System.out.println("TAI KHOAN ID: " + this.accountId + " - " + this.accountName);
        System.out.println(accountHeader);

        if (accountTransactions.isEmpty()) {
            System.out.println("Khong co giao dich nao cho tai khoan nay!");
            System.out.println(accountHeader);
            return;
        }

        displayDetailedAccountReport(accountTransactions);

        // FOOTER SAU KHI HIỂN THỊ BÁO CÁO
        System.out.println(accountHeader);
        System.out.println("KET THUC BAO CAO TAI KHOAN: " + this.accountId);
        System.out.println(accountHeader);
    }

    private List<Transaction> filterTransactionsByAccount(List<Transaction> allTransactions) {
        return allTransactions.stream()
                .filter(transaction -> transaction.getAccountId().equals(this.accountId))
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                .collect(Collectors.toList());
    }

    private void displayDetailedAccountReport(List<Transaction> transactions) {
        String header = "==============================================================";
        String separator = "--------------------------------------------------------------";

        System.out.println(header);
        System.out.printf("| %-60s |\n", "BAO CAO TAI KHOAN: " + this.accountName);
        System.out.println(separator);

        // Thong ke tong quan
        double totalIncome = calculateTotalIncome(transactions);
        double totalExpense = calculateTotalExpense(transactions);
        double netFlow = totalIncome - totalExpense;

        System.out.printf("| %-60s |\n", "TONG QUAN TAI KHOAN");
        System.out.printf("| %-60s |\n", "+----------------------------------------------------+");
        System.out.printf("| %-60s |\n", String.format("| So du hien tai: %,.2f %s", this.balance, this.currency));
        System.out.printf("| %-60s |\n", String.format("| Tong thu nhap:  %,.2f %s", totalIncome, this.currency));
        System.out.printf("| %-60s |\n", String.format("| Tong chi tieu:  %,.2f %s", totalExpense, this.currency));
        System.out.printf("| %-60s |\n", String.format("| Luong tien rong: %,.2f %s", netFlow, this.currency));
        System.out.printf("| %-60s |\n", "+----------------------------------------------------+");
        System.out.println(separator);

        // Phan loai theo danh muc
        displayCategoryBreakdown(transactions);
        System.out.println(separator);

        // Giao dich gan day
        displayRecentTransactions(transactions);

        System.out.println(header);
    }

    private double calculateTotalIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private double calculateTotalExpense(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private void displayCategoryBreakdown(List<Transaction> transactions) {
        Map<String, Double> incomeByCategory = new HashMap<>();
        Map<String, Double> expenseByCategory = new HashMap<>();

        for (Transaction transaction : transactions) {
            String categoryName = transaction.getCategory().getName();
            double amount = transaction.getAmount();

            if (transaction.getType() == TransactionType.INCOME) {
                incomeByCategory.merge(categoryName, amount, Double::sum);
            } else {
                expenseByCategory.merge(categoryName, amount, Double::sum);
            }
        }

        System.out.printf("║ %-60s ║\n", " PHÂN LOẠI THEO DANH MỤC");

        if (!incomeByCategory.isEmpty()) {
            System.out.printf("| %-60s |\n", "THU NHAP:");
            incomeByCategory.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .forEach(entry -> {
                        String line = String.format("  - %-15s: %,.2f %s",
                                entry.getKey(), entry.getValue(), this.currency);
                        System.out.printf("| %-60s |\n", line);
                    });
        }

        if (!expenseByCategory.isEmpty()) {
            System.out.printf("| %-60s |\n", "CHI TIEU:");
            expenseByCategory.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .forEach(entry -> {
                        String line = String.format("  - %-15s: %,.2f %s",
                                entry.getKey(), entry.getValue(), this.currency);
                        System.out.printf("| %-60s |\n", line);
                    });
        }
    }

    private void displayRecentTransactions(List<Transaction> transactions) {
        System.out.printf("| %-60s |\n", "GIAO DICH GAN DAY (5 giao dich moi nhat)");

        List<Transaction> recentTransactions = transactions.stream()
                .limit(5)
                .collect(Collectors.toList());

        for (Transaction transaction : recentTransactions) {
            String typeSymbol = transaction.getType() == TransactionType.INCOME ? "[+]" : "[-]";

            String line = String.format("%s %s %-15s: %,.2f %s",
                    typeSymbol,
                    transaction.getFormattedDate().substring(0, 10),
                    transaction.getCategory().getName(),
                    transaction.getAmount(),
                    transaction.getDescription());

            // Cat bot neu qua dai
            if (line.length() > 58) {
                line = line.substring(0, 55) + "...";
            }

            System.out.printf("| %-60s |\n", line);
        }
    }


    @Override
    public String toString() {
        return String.format("Account{id=%s, name=%s, type=%s, balance=%.2f %s}",
                accountId, accountName, accountType, balance, currency);
    }
}