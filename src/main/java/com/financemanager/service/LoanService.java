package com.financemanager.service;

import com.financemanager.model.Loan;
import com.financemanager.model.Lending;
import com.financemanager.model.Payment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LoanService {
    private List<Loan> loans;
    private List<Lending> lendings;

    public LoanService() {
        this.loans = new ArrayList<>();
        this.lendings = new ArrayList<>();
    }

    // THÊM: Method tạo loan với số tháng
    public void addLoan(String lender, double amount, double interest,
                        int months, String description) {
        try {
            Loan loan = new Loan(lender, amount, interest, months, description);
            if (loan.isValid()) {
                loans.add(loan);
                System.out.println("Them khoan vay thanh cong!");
                loan.displayInfo();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }

    // THÊM: Method tạo lending với số tháng
    public void addLending(String borrower, double amount, double interest,
                           int months, String description) {
        try {
            Lending lending = new Lending(borrower, amount, interest, months, description);
            if (lending.isValid()) {
                lendings.add(lending);
                System.out.println("Them khoan cho vay thanh cong!");
                lending.displayInfo();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Loi: " + e.getMessage());
        }
    }

    // THÊM: Hiển thị chi tiết khoản vay
    public void displayLoanDetails(String loanId) {
        Optional<Loan> loanOpt = findLoanById(loanId);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            System.out.println("\nCHI TIET KHOAN VAY");
            System.out.println("=========================================");
            loan.displayInfo();

            // Hiển thị lịch sử thanh toán
            if (!loan.getPaymentHistory().isEmpty()) {
                System.out.println("\nLICH SU THANH TOAN:");
                for (Payment payment : loan.getPaymentHistory()) {
                    System.out.printf(" - %.2f VND vao %s\n",
                            payment.getAmount(), payment.getFormattedDate());
                }
            } else {
                System.out.println("\nChua co thanh toan nao.");
            }
        } else {
            System.out.println("Khong tim thay khoan vay!");
        }
    }

    // THÊM: Hiển thị chi tiết khoản cho vay
    public void displayLendingDetails(String lendingId) {
        Optional<Lending> lendingOpt = findLendingById(lendingId);
        if (lendingOpt.isPresent()) {
            Lending lending = lendingOpt.get();
            System.out.println("\nCHI TIET KHOAN CHO VAY");
            System.out.println("=========================================");
            lending.displayInfo();

            if (!lending.getRepaymentHistory().isEmpty()) {
                System.out.println("\nLICH SU TRA NO:");
                for (Payment payment : lending.getRepaymentHistory()) {
                    System.out.printf(" - %.2f VND vao %s\n",
                            payment.getAmount(), payment.getFormattedDate());
                }
            } else {
                System.out.println("\nChua co tra no nao.");
            }
        } else {
            System.out.println("Khong tim thay khoan cho vay!");
        }
    }

    // THÊM: Lấy danh sách khoản vay sắp đến hạn
    public List<Loan> getDueSoonLoans() {
        return loans.stream()
                .filter(Loan::isDueSoon)
                .collect(Collectors.toList());
    }

    // THÊM: Lấy danh sách khoản vay quá hạn
    public List<Loan> getOverdueLoans() {
        return loans.stream()
                .filter(Loan::isOverdue)
                .collect(Collectors.toList());
    }

    // THÊM: Lấy danh sách cho vay sắp đến hạn
    public List<Lending> getDueSoonLendings() {
        return lendings.stream()
                .filter(Lending::isDueSoon)
                .collect(Collectors.toList());
    }

    // THÊM: Lấy danh sách cho vay quá hạn
    public List<Lending> getOverdueLendings() {
        return lendings.stream()
                .filter(Lending::isOverdue)
                .collect(Collectors.toList());
    }

    // THÊM: Tổng quan khoản vay
    private void displayLoanSummary() {
        double totalBorrowed = loans.stream().mapToDouble(Loan::getPrincipalAmount).sum();
        double totalRemaining = loans.stream().mapToDouble(Loan::getRemainingAmount).sum();
        double totalPaid = totalBorrowed - totalRemaining;
        long overdueLoans = loans.stream().filter(Loan::isOverdue).count();
        long dueSoonLoans = loans.stream().filter(Loan::isDueSoon).count();

        System.out.println("\nTONG QUAN KHOAN VAY:");
        System.out.printf(" - Tong da vay: %.2f VND\n", totalBorrowed);
        System.out.printf(" - Da tra: %.2f VND\n", totalPaid);
        System.out.printf(" - Con no: %.2f VND\n", totalRemaining);
        System.out.printf(" - Qua han: %d khoan\n", overdueLoans);
        System.out.printf(" - Sap den han: %d khoan\n", dueSoonLoans);
    }

    // THÊM: Tổng quan khoản cho vay
    private void displayLendingSummary() {
        double totalLent = lendings.stream().mapToDouble(Lending::getPrincipalAmount).sum();
        double totalRemaining = lendings.stream().mapToDouble(Lending::getRemainingAmount).sum();
        double totalCollected = totalLent - totalRemaining;
        long overdueLendings = lendings.stream().filter(Lending::isOverdue).count();
        long dueSoonLendings = lendings.stream().filter(Lending::isDueSoon).count();

        System.out.println("\nTONG QUAN CHO VAY:");
        System.out.printf(" - Tong da cho vay: %.2f VND\n", totalLent);
        System.out.printf(" - Da thu: %.2f VND\n", totalCollected);
        System.out.printf(" - Con phai thu: %.2f VND\n", totalRemaining);
        System.out.printf(" - Qua han: %d khoan\n", overdueLendings);
        System.out.printf(" - Sap den han: %d khoan\n", dueSoonLendings);
    }

    // Các method hiện có giữ nguyên...
    public void displayAllLoans() {
        if (loans.isEmpty()) {
            System.out.println("Khong co khoan vay nao!");
            return;
        }

        System.out.println("\nDANH SACH KHOAN VAY (" + loans.size() + " khoan)");
        System.out.println("=========================================");

        for (Loan loan : loans) {
            loan.displayInfo();
            System.out.println();
        }

        // THÊM: Hiển thị tổng quan
        displayLoanSummary();
    }

    public void displayAllLendings() {
        if (lendings.isEmpty()) {
            System.out.println("Khong co khoan cho vay nao!");
            return;
        }

        System.out.println("\nDANH SACH CHO VAY (" + lendings.size() + " khoan)");
        System.out.println("=========================================");

        for (Lending lending : lendings) {
            lending.displayInfo();
            System.out.println();
        }

        // THÊM: Hiển thị tổng quan
        displayLendingSummary();
    }

    public Optional<Loan> findLoanById(String loanId) {
        return loans.stream()
                .filter(loan -> loan.getLoanId().equals(loanId))
                .findFirst();
    }

    public Optional<Lending> findLendingById(String lendingId) {
        return lendings.stream()
                .filter(lending -> lending.getLendingId().equals(lendingId))
                .findFirst();
    }

    public boolean addPaymentToLoan(String loanId, double amount) {
        try {
            Optional<Loan> loanOpt = findLoanById(loanId);
            if (loanOpt.isPresent()) {
                Loan loan = loanOpt.get();
                loan.addPayment(amount);
                return true;
            } else {
                System.out.println("Khong tim thay khoan vay!");
                return false;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Loi: " + e.getMessage());
            return false;
        }
    }

    public boolean addRepaymentToLending(String lendingId, double amount) {
        try {
            Optional<Lending> lendingOpt = findLendingById(lendingId);
            if (lendingOpt.isPresent()) {
                Lending lending = lendingOpt.get();
                lending.addRepayment(amount);
                return true;
            } else {
                System.out.println("Khong tim thay khoan cho vay!");
                return false;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Loi: " + e.getMessage());
            return false;
        }
    }

    // Get totals
    public double getTotalLoanAmount() {
        return loans.stream()
                .mapToDouble(Loan::getRemainingAmount)
                .sum();
    }

    public double getTotalLendingAmount() {
        return lendings.stream()
                .mapToDouble(Lending::getRemainingAmount)
                .sum();
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public List<Lending> getAllLendings() {
        return new ArrayList<>(lendings);
    }

    // THÊM vào LoanService.java (cuối class)
    public boolean repayLoan(String loanId, double amount) {
        Optional<Loan> loanOpt = findLoanById(loanId);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();

            if (amount <= 0) {
                System.out.println("So tien tra no phai lon hon 0!");
                return false;
            }

            if (amount > loan.getRemainingAmount()) {
                System.out.println("So tien tra no vuot qua so tien con lai!");
                return false;
            }

            if (loan.getStatus().equals("PAID")) {
                System.out.println("Khoan vay da duoc tra het!");
                return false;
            }

            loan.addPayment(amount);
            System.out.printf("Tra no thanh cong! So tien con lai: %.2f VND\n", loan.getRemainingAmount());
            return true;
        } else {
            System.out.println("Khong tim thay khoan vay!");
            return false;
        }
    }

    public boolean collectLending(String lendingId, double amount) {
        Optional<Lending> lendingOpt = findLendingById(lendingId);
        if (lendingOpt.isPresent()) {
            Lending lending = lendingOpt.get();

            if (amount <= 0) {
                System.out.println("So tien thu no phai lon hon 0!");
                return false;
            }

            if (amount > lending.getRemainingAmount()) {
                System.out.println("So tien thu no vuot qua so tien con lai!");
                return false;
            }

            if (lending.getStatus().equals("PAID")) {
                System.out.println("Khoan cho vay da duoc thu het!");
                return false;
            }

            lending.addRepayment(amount);
            System.out.printf("Thu no thanh cong! So tien con lai: %.2f VND\n", lending.getRemainingAmount());
            return true;
        } else {
            System.out.println("Khong tim thay khoan cho vay!");
            return false;
        }
    }

    // THÊM: Hiển thị khoản sắp đến hạn theo số ngày
    public void displayUpcomingDueItems(int days) {
        System.out.println("\nCAC KHOAN SAP DEN HAN (trong " + days + " ngay toi)");
        System.out.println("=========================================");

        List<Loan> dueLoans = getDueSoonLoans().stream()
                .filter(loan -> loan.getRemainingMonths() <= 1) // Tương đương ~30 ngày
                .collect(Collectors.toList());

        List<Lending> dueLendings = getDueSoonLendings().stream()
                .filter(lending -> lending.getRemainingMonths() <= 1)
                .collect(Collectors.toList());

        if (dueLoans.isEmpty() && dueLendings.isEmpty()) {
            System.out.println("Khong co khoan nao sap den han!");
            return;
        }

        if (!dueLoans.isEmpty()) {
            System.out.println("KHOAN VAY SAP DEN HAN:");
            for (Loan loan : dueLoans) {
                System.out.printf(" - %s: %.2f VND (Con %d thang)\n",
                        loan.getLenderName(), loan.getRemainingAmount(), loan.getRemainingMonths());
            }
        }

        if (!dueLendings.isEmpty()) {
            System.out.println("\nKHOAN CHO VAY SAP DEN HAN:");
            for (Lending lending : dueLendings) {
                System.out.printf(" - %s: %.2f VND (Con %d thang)\n",
                        lending.getBorrowerName(), lending.getRemainingAmount(), lending.getRemainingMonths());
            }
        }
    }
}