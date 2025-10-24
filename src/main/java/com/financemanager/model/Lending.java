package com.financemanager.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Lending {
    private String lendingId;
    private String borrowerName;
    private double principalAmount;
    private double interestRate;
    private int lendingMonths;
    private double remainingAmount;
    private LocalDateTime startDate;
    private LocalDateTime dueDate;
    private String status;
    private String description;
    private List<Payment> repaymentHistory;

    public Lending(String borrowerName, double principalAmount, double interestRate,
                   int lendingMonths, String description) {

        if (principalAmount <= 0) throw new IllegalArgumentException("So tien cho vay phai lon hon 0");
        if (interestRate < 0) throw new IllegalArgumentException("Lai suat khong duoc am");
        if (lendingMonths <= 0) throw new IllegalArgumentException("So thang cho vay phai lon hon 0");
        if (borrowerName == null || borrowerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten nguoi vay khong duoc de trong");
        }

        this.lendingId = "LEND_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.borrowerName = borrowerName;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.lendingMonths = lendingMonths;
        this.remainingAmount = principalAmount;
        this.description = description;
        this.status = "ACTIVE";
        this.startDate = LocalDateTime.now();
        this.dueDate = startDate.plusMonths(lendingMonths);
        this.repaymentHistory = new ArrayList<>();
    }

    // Getter methods...
    public String getLendingId() { return lendingId; }
    public String getBorrowerName() { return borrowerName; }
    public double getPrincipalAmount() { return principalAmount; }
    public double getInterestRate() { return interestRate; }
    public int getLendingMonths() { return lendingMonths; }
    public double getRemainingAmount() { return remainingAmount; }
    public String getStatus() { return status; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDate() { return startDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public List<Payment> getRepaymentHistory() { return repaymentHistory; }

    // Business methods...
    public void addRepayment(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("So tien tra phai lon hon 0");
        if (amount > remainingAmount) {
            throw new IllegalArgumentException(
                    String.format("So tien tra (%.2f) vuot qua so tien con no (%.2f)", amount, remainingAmount));
        }

        remainingAmount -= amount;
        Payment payment = new Payment(amount);
        repaymentHistory.add(payment);
        updateStatus();

        System.out.printf("Da nhan tra no: %.2f VND. Con no: %.2f VND\n", amount, remainingAmount);
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
        return principalAmount * (interestRate / 100) * (lendingMonths / 12.0);
    }

    public double calculateTotalAmount() {
        return principalAmount + calculateTotalInterest();
    }

    public double calculateMonthlyCollection() {
        return calculateTotalAmount() / lendingMonths;
    }

    //Tính số tháng còn lại chính xác
    public long getRemainingMonths() {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(dueDate)) {
            return 0;
        }

        long totalMonths = ChronoUnit.MONTHS.between(startDate, dueDate);
        long passedMonths = ChronoUnit.MONTHS.between(startDate, now);
        long remaining = totalMonths - passedMonths;

        return Math.max(0, remaining);
    }

    //Kiểm tra sắp đến hạn
    public boolean isDueSoon() {
        long remainingMonths = getRemainingMonths();
        return remainingMonths <= 1 && remainingMonths >= 0 && remainingAmount > 0;
    }

    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(dueDate) && remainingAmount > 0;
    }

    public boolean isValid() {
        return borrowerName != null && !borrowerName.trim().isEmpty() &&
                principalAmount > 0 && interestRate >= 0 && lendingMonths > 0;
    }

    public void displayInfo() {
        System.out.println("┌──────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ %-64s │\n", "THONG TIN KHOAN CHO VAY");
        System.out.println("├──────────────────────────────────────────────────────────────────┤");
        System.out.printf("│ %-20s: %-42s │\n", "ID", lendingId);
        System.out.printf("│ %-20s: %-42s │\n", "Nguoi vay", shortenString(borrowerName, 42));
        System.out.printf("│ %-20s: %-42s │\n", "So tien cho vay", formatCurrency(principalAmount) + " VND");
        System.out.printf("│ %-20s: %-42s │\n", "Lai suat", String.format("%.2f%%/nam", interestRate));
        System.out.printf("│ %-20s: %-42s │\n", "Thoi han cho vay", lendingMonths + " thang");
        System.out.printf("│ %-20s: %-42s │\n", "So tien con lai", formatCurrency(remainingAmount) + " VND");
        System.out.printf("│ %-20s: %-42s │\n", "Tong se nhan", formatCurrency(calculateTotalAmount()) + " VND");
        System.out.printf("│ %-20s: %-42s │\n", "Hang thang", formatCurrency(calculateMonthlyCollection()) + " VND");
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
            System.out.printf("│ \u001B[31m%-64s\u001B[0m │\n", "⚠CANH BAO: KHOAN CHO VAY DA QUA HAN!");
        } else if (isDueSoon()) {
            System.out.printf("│ \u001B[33m%-64s\u001B[0m │\n",
                    "CANH BAO: KHOAN CHO VAY SAP DEN HAN! (" + remainingMonths + " thang con lai)");
        } else if (status.equals("PAID")) {
            System.out.printf("│ \u001B[32m%-64s\u001B[0m │\n", "KHOAN CHO VAY DA DUOC TRA HET");
        }

        System.out.println("└──────────────────────────────────────────────────────────────────┘");
    }

    //Chuyển trạng thái sang tiếng Việt
    private String getStatusVietnamese() {
        switch (status) {
            case "ACTIVE": return "Dang cho vay";
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
        return String.format("Lending{id=%s, borrower=%s, amount=%.2f, months=%d, remaining=%.2f, status=%s}",
                lendingId, borrowerName, principalAmount, lendingMonths, remainingAmount, status);
    }
}