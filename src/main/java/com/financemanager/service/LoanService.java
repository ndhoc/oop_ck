package com.financemanager.service;

import com.financemanager.model.Loan;
import com.financemanager.model.Lending;
import com.financemanager.model.Payment;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanService {
    private List<Loan> loans;
    private List<Lending> lendings;

    public LoanService() {
        this.loans = new ArrayList<>();
        this.lendings = new ArrayList<>();
    }

    // Loan methods
    public void addLoan(Loan loan) {
        if (loan.isValid()) {
            loans.add(loan);
            System.out.println("Them khoan vay thanh cong!");
            loan.displayInfo();
        } else {
            System.out.println("Khoan vay khong hop le!");
        }
    }

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
    }

    public Optional<Loan> findLoanById(String loanId) {
        return loans.stream()
                .filter(loan -> loan.getLoanId().equals(loanId))
                .findFirst();
    }

    public boolean addPaymentToLoan(String loanId, double amount) {
        Optional<Loan> loanOpt = findLoanById(loanId);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            loan.addPayment(amount);
            System.out.println("Them thanh toan thanh cong!");
            return true;
        } else {
            System.out.println("Khong tim thay khoan vay!");
            return false;
        }
    }

    // Lending methods
    public void addLending(Lending lending) {
        if (lending.isValid()) {
            lendings.add(lending);
            System.out.println("Them khoan cho vay thanh cong!");
            lending.displayInfo();
        } else {
            System.out.println("Khoan cho vay khong hop le!");
        }
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
    }

    public Optional<Lending> findLendingById(String lendingId) {
        return lendings.stream()
                .filter(lending -> lending.getLendingId().equals(lendingId))
                .findFirst();
    }

    public boolean addRepaymentToLending(String lendingId, double amount) {
        Optional<Lending> lendingOpt = findLendingById(lendingId);
        if (lendingOpt.isPresent()) {
            Lending lending = lendingOpt.get();
            lending.addRepayment(amount);
            System.out.println("Them tra no thanh cong!");
            return true;
        } else {
            System.out.println("Khong tim thay khoan cho vay!");
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
}