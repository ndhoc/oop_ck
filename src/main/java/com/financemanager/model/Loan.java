package com.financemanager.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Loan {
    private String loanId;
    private String lenderName;
    private double principalAmount;
    private double interestRate;
    private int loanMonths;
    private double remainingAmount;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private String status;
    private String description;
    private List<Payment> paymentHistory;

    public Loan(String lenderName, double principalAmount, double interestRate,
                int loanMonths, String description) {

        if (principalAmount <= 0) {
            throw new IllegalArgumentException("So tien vay phai lon hon 0");
        }
        if (interestRate < 0) {
            throw new IllegalArgumentException("Lai suat khong duoc am");
        }
        if (loanMonths <= 0) {
            throw new IllegalArgumentException("So thang vay phai lon hon 0");
        }

        this.loanId = "LOAN_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.lenderName = lenderName;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.loanMonths = loanMonths;
        this.remainingAmount = principalAmount;
        this.description = description;
        this.status = "ACTIVE";
        this.startDate = LocalDateTime.now();
        this.dueDate = startDate.plusMonths(loanMonths);
        this.paymentHistory = new ArrayList<>();
    }

    // Getter methods...
    public String getLoanId() { return loanId; }
    public String getLenderName() { return lenderName; }
    public double getPrincipalAmount() { return principalAmount; }
    public double getInterestRate() { return interestRate; }
    public int getLoanMonths() { return loanMonths; }
    public double getRemainingAmount() { return remainingAmount; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public List<Payment> getPaymentHistory() { return paymentHistory; }

    // Business methods...
    public void addPayment(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("So tien thanh toan phai lon hon 0");
        }
        if (amount > remainingAmount) {
            throw new IllegalArgumentException(
                    String.format("So tien thanh toan (%.2f) vuot qua so tien con no (%.2f)",
                            amount, remainingAmount));
        }

        remainingAmount -= amount;
        Payment payment = new Payment(amount);
        paymentHistory.add(payment);
        updateStatus();

        System.out.printf("Da thanh toan: %.2f VND. Con no: %.2f VND\n", amount, remainingAmount);
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

    public double calculateTotalInterest() {
        return principalAmount * (interestRate / 100) * (loanMonths / 12.0);
    }

    public double calculateTotalAmount() {
        return principalAmount + calculateTotalInterest();
    }

    public double calculateMonthlyPayment() {
        double totalAmount = calculateTotalAmount();
        return totalAmount / loanMonths;
    }

    //Tính số tháng còn lại chính xác
    public long getRemainingMonths() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(dueDate)) {
            return 0;
        }

        // Tính số tháng chính xác giữa hai ngày
        long totalMonths = ChronoUnit.MONTHS.between(startDate, dueDate);
        long passedMonths = ChronoUnit.MONTHS.between(startDate, now);
        long remaining = totalMonths - passedMonths;

        return Math.max(0, remaining);
    }

    //Kiểm tra sắp đến hạn (còn 1 tháng hoặc ít hơn)
    public boolean isDueSoon() {
        long remainingMonths = getRemainingMonths();
        return remainingMonths <= 1 && remainingMonths >= 0 && remainingAmount > 0;
    }

    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(dueDate) && remainingAmount > 0;
    }

    public boolean isValid() {
        return lenderName != null && !lenderName.trim().isEmpty() &&
                principalAmount > 0 && interestRate >= 0 && loanMonths > 0;
    }

    //Display với định dạng cố định đẹp hơn
    public void displayInfo() {
        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-64s │\n", "THONG TIN KHOAN VAY");
        System.out.println("├──────────────────────────────────────────────────────────────────┤");
        System.out.printf("│ %-20s: %-42s │\n", "ID", loanId);
        System.out.printf("│ %-20s: %-42s │\n", "Nguoi cho vay", shortenString(lenderName, 42));
        System.out.printf("│ %-20s: %-42s │\n", "So tien vay", formatCurrency(principalAmount) + " VND");
        System.out.printf("│ %-20s: %-42s │\n", "Lai suat", String.format("%.2f%%/nam", interestRate));
        System.out.printf("│ %-20s: %-42s │\n", "Thoi han vay", loanMonths + " thang");
        System.out.printf("│ %-20s: %-42s │\n", "So tien con lai", formatCurrency(remainingAmount) + " VND");
        System.out.printf("│ %-20s: %-42s │\n", "Tong phai tra", formatCurrency(calculateTotalAmount()) + " VND");
        System.out.printf("│ %-20s: %-42s │\n", "Hang thang", formatCurrency(calculateMonthlyPayment()) + " VND");
        System.out.printf("│ %-20s: %-42s │\n", "Trang thai", getStatusVietnamese());
        System.out.printf("│ %-20s: %-42s │\n", "Ngay bat dau", formatDate(startDate));
        System.out.printf("│ %-20s: %-42s │\n", "Ngay den han", formatDate(dueDate));
        System.out.printf("│ %-20s: %-42s │\n", "Mo ta", shortenString(description, 42));

        // Hiển thị thông tin bổ sung
        System.out.println("├──────────────────────────────────────────────────────────────────┤");

        // Hiển thị số tháng còn lại
        long remainingMonths = getRemainingMonths();
        System.out.printf("│ %-20s: %-42s │\n", "Thang con lai", remainingMonths + " thang");

        // Hiển thị cảnh báo
        if (isOverdue()) {
            System.out.printf("│ \u001B[31m%-64s\u001B[0m │\n", "CANH BAO: KHOAN VAY DA QUA HAN!");
        } else if (isDueSoon()) {
            System.out.printf("│ \u001B[33m%-64s\u001B[0m │\n",
                    "CANH BAO: KHOAN VAY SAP DEN HAN! (" + remainingMonths + " thang con lai)");
        } else if (status.equals("PAID")) {
            System.out.printf("│ \u001B[32m%-64s\u001B[0m │\n", "KHOAN VAY DA DUOC TRA HET");
        }

        System.out.println("└──────────────────────────────────────────────────────────────────┘");
    }

    //Chuyển trạng thái sang tiếng Việt
    private String getStatusVietnamese() {
        switch (status) {
            case "ACTIVE": return "Dang vay";
            case "PAID": return "Da tra het";
            case "OVERDUE": return "Qua han";
            default: return status;
        }
    }

    //Hàm định dạng tiền tệ
    private String formatCurrency(double amount) {
        if (amount >= 1_000_000_000) {
            return String.format("%,.2f ty", amount / 1_000_000_000);
        } else if (amount >= 1_000_000) {
            return String.format("%,.2f trieu", amount / 1_000_000);
        } else if (amount >= 1_000) {
            return String.format("%,.2f ngan", amount / 1_000);
        } else {
            return String.format("%,.2f", amount);
        }
    }

    //Hàm rút gọn chuỗi
    private String shortenString(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }

    private String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    @Override
    public String toString() {
        return String.format("Loan{id=%s, lender=%s, amount=%.2f, months=%d, remaining=%.2f, status=%s}",
                loanId, lenderName, principalAmount, loanMonths, remainingAmount, status);
    }
}