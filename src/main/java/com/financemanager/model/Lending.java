package com.financemanager.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Lending {
    private String lendingId;
    private String borrowerName;
    private double principalAmount;
    private double interestRate;
    private double remainingAmount;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private String status; // ACTIVE, PAID, OVERDUE
    private String description;
    private List<Payment> repaymentHistory;

    public Lending(String borrowerName, double principalAmount, double interestRate, String description) {
        this.lendingId = "LEND_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.borrowerName = borrowerName;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.remainingAmount = principalAmount;
        this.description = description;
        this.status = "ACTIVE";
        this.startDate = LocalDateTime.now();
        this.dueDate = startDate.plusDays(90);
        this.repaymentHistory = new ArrayList<>();
    }

    // Getter methods
    public String getLendingId() { return lendingId; }
    public String getBorrowerName() { return borrowerName; }
    public double getPrincipalAmount() { return principalAmount; }
    public double getInterestRate() { return interestRate; }
    public double getRemainingAmount() { return remainingAmount; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public List<Payment> getRepaymentHistory() { return repaymentHistory; }

    // Business methods
    public void addRepayment(double amount) {
        if (amount > 0 && amount <= remainingAmount) {
            remainingAmount -= amount;
            Payment payment = new Payment(amount);
            repaymentHistory.add(payment);
            updateStatus();
        }
    }

    public void updateStatus() {
        if (remainingAmount <= 0) {
            status = "PAID";
        } else if (LocalDateTime.now().isAfter(dueDate)) {
            status = "OVERDUE";
        } else {
            status = "ACTIVE";
        }
    }

    public double calculateInterest() {
        return principalAmount * (interestRate / 100);
    }

    public double calculateTotalAmount() {
        return principalAmount + calculateInterest();
    }

    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(dueDate) && remainingAmount > 0;
    }

    public boolean isValid() {
        return borrowerName != null && !borrowerName.trim().isEmpty() &&
                principalAmount > 0 && interestRate >= 0;
    }

    // Display
    public void displayInfo() {
        System.out.println("┌─────────────────────────────────────┐");
        System.out.printf("│ ID: %-34s │\n", lendingId);
        System.out.printf("│ Nguoi vay: %-26s │\n", borrowerName);
        System.out.printf("│ So tien cho vay: %-20.2f │\n", principalAmount);
        System.out.printf("│ Lai suat: %-27.2f%% │\n", interestRate);
        System.out.printf("│ Con lai: %-28.2f │\n", remainingAmount);
        System.out.printf("│ Trang thai: %-26s │\n", status);
        System.out.printf("│ Ngay bat dau: %-24s │\n", formatDate(startDate));
        System.out.printf("│ Ngay den han: %-24s │\n", formatDate(dueDate));
        System.out.printf("│ Mo ta: %-30s │\n", description);
        System.out.println("└─────────────────────────────────────┘");
    }

    private String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    @Override
    public String toString() {
        return String.format("Lending{id=%s, borrower=%s, amount=%.2f, remaining=%.2f, status=%s}",
                lendingId, borrowerName, principalAmount, remainingAmount, status);
    }
}