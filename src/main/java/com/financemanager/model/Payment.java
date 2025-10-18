package com.financemanager.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Payment {
    private String paymentId;
    private double amount;
    private LocalDateTime paymentDate;
    private String paymentMethod;

    public Payment(double amount) {
        this.paymentId = "PAY_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.amount = amount;
        this.paymentDate = LocalDateTime.now();
        this.paymentMethod = "CASH";
    }

    public Payment(double amount, String paymentMethod) {
        this(amount);
        this.paymentMethod = paymentMethod;
    }

    // Getter methods
    public String getPaymentId() { return paymentId; }
    public double getAmount() { return amount; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return paymentDate.format(formatter);
    }

    @Override
    public String toString() {
        return String.format("Payment{id=%s, amount=%.2f, date=%s, method=%s}",
                paymentId, amount, getFormattedDate(), paymentMethod);
    }
}