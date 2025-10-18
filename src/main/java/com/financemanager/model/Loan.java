package com.financemanager.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Loan {
    private String loanId;
    private String lenderName;
    private double principalAmount;
    private double interestRate;
    private double remainingAmount;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private String status; // ACTIVE, PAID, OVERDUE
    private String description;
    private List<Payment> paymentHistory;

    public Loan(String lenderName, double principalAmount, double interestRate, String description) {
        this.loanId = "LOAN_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.lenderName = lenderName;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.remainingAmount = principalAmount;
        this.description = description;
        this.status = "ACTIVE";
        this.startDate = LocalDateTime.now();
        this.dueDate = startDate.plusDays(90); // 90 days from now
        this.paymentHistory = new ArrayList<>();
    }

    // Getter methods
    public String getLoanId() { return loanId; }
    public String getLenderName() { return lenderName; }
    public double getPrincipalAmount() { return principalAmount; }
    public double getInterestRate() { return interestRate; }
    public double getRemainingAmount() { return remainingAmount; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public List<Payment> getPaymentHistory() { return paymentHistory; }

    // Business methods
    public void addPayment(double amount) {
        if (amount > 0 && amount <= remainingAmount) {
            remainingAmount -= amount;
            Payment payment = new Payment(amount);
            paymentHistory.add(payment);
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
        return lenderName != null && !lenderName.trim().isEmpty() &&
                principalAmount > 0 && interestRate >= 0;
    }

    // Display
    public void displayInfo() {
        System.out.println("┌─────────────────────────────────────┐");
        System.out.printf("│ ID: %-34s │\n", loanId);
        System.out.printf("│ Nguoi cho vay: %-23s │\n", lenderName);
        System.out.printf("│ So tien vay: %-25.2f │\n", principalAmount);
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
        return String.format("Loan{id=%s, lender=%s, amount=%.2f, remaining=%.2f, status=%s}",
                loanId, lenderName, principalAmount, remainingAmount, status);
    }
}