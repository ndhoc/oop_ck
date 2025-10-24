package com.financemanager.service;

import com.financemanager.model.*;
import com.financemanager.exception.*;
import com.financemanager.util.Validator;
import java.util.List;
import java.util.Optional;

public class FinanceManager {
    private AccountService accountService;
    private TransactionService transactionService;
    private LoanService loanService;
    private ReportService reportService;

    public FinanceManager() {
        this.accountService = new AccountService();
        this.transactionService = new TransactionService(accountService);
        this.loanService = new LoanService();
        this.reportService = new ReportService(accountService, transactionService, loanService);
    }

    // UPDATE method generateFinancialReport để dùng ReportService
    public void generateFinancialReport() {
        reportService.generateFinancialOverview();
    }

    // THÊM CÁC METHOD MỚI CHO BÁO CÁO
    public void generateIncomeExpenseReport() {
        reportService.generateIncomeExpenseReport(
                java.time.LocalDate.now().minusMonths(1),
                java.time.LocalDate.now()
        );
    }

    public void generateLoanReport() {
        reportService.generateLoanReport();
    }

    public void generateMonthlyReport() {
        int currentYear = java.time.LocalDate.now().getYear();
        int currentMonth = java.time.LocalDate.now().getMonthValue();
        reportService.generateMonthlyReport(currentYear, currentMonth);
    }

    public void exportToCSV() {
        reportService.exportToCSV();
    }

    // UPDATE method addAccount với validation
    public void addAccount(String name, String type, String number, double balance) {
        Validator.ValidationResult result = Validator.validateAccount(name, type, number, balance);
        if (!result.isValid()) {
            result.printErrors();
            return;
        }

        Account account = new Account(name, type, number, balance);
        accountService.addAccount(account);
    }

    // UPDATE method addTransaction với validation
    public void addTransaction(String accountId, String typeStr, double amount,
                               String description, String category) {
        Validator.ValidationResult result = Validator.validateTransaction(accountId, typeStr, amount, description);
        if (!result.isValid()) {
            result.printErrors();
            return;
        }

        TransactionType type;
        if (typeStr.equalsIgnoreCase("income")) {
            type = TransactionType.INCOME;
        } else if (typeStr.equalsIgnoreCase("expense")) {
            type = TransactionType.EXPENSE;
        } else {
            System.out.println(" Loai giao dich khong hop le!");
            return;
        }

        transactionService.addTransaction(accountId, type, amount, description, category);
    }

    // THÊM method tìm kiếm và lọc
    public void findAccountByName(String name) {
        accountService.getAllAccounts().stream()
                .filter(acc -> acc.getAccountName().toLowerCase().contains(name.toLowerCase()))
                .forEach(Account::displayInfo);
    }

    public void displayTransactionsByCategory(String category) {
        List<Transaction> transactions = transactionService.getTransactionsByCategory(category);
        if (transactions.isEmpty()) {
            System.out.println(" Khong co giao dich nao trong danh muc: " + category);
            return;
        }

        System.out.println("\n GIAO DICH DANH MUC: " + category);
        transactions.forEach(Transaction::displayInfo);
    }

    // THÊM getter cho ReportService
    public ReportService getReportService() {
        return reportService;
    }

    // Các method hiện có giữ nguyên...
    public void displayAllAccounts() {
        accountService.displayAllAccounts();
    }

    public void deleteAccount(String accountId) {
        accountService.deleteAccount(accountId);
    }

    public void transferBetweenAccounts(String fromId, String toId, double amount) {
        try {
            accountService.transferBetweenAccounts(fromId, toId, amount);
        } catch (AccountNotFoundException | InsufficientBalanceException e) {
            System.out.println(e.getMessage());
        }
    }

    public void displayAllTransactions() {
        transactionService.displayAllTransactions();
    }

    public void displayCurrentBalance() {
        transactionService.displayCurrentBalance();
    }

    // Loan management - SỬA LẠI METHOD addLoan
    public void addLoan(String lender, double amount, double interest,
                        int months, String description) {
        Validator.ValidationResult result = Validator.validateLoan(lender, amount, interest, description, months);
        if (!result.isValid()) {
            result.printErrors();
            return;
        }

        loanService.addLoan(lender, amount, interest, months, description);
    }

    public void addLending(String borrower, double amount, double interest,
                           int months, String description) {
        Validator.ValidationResult result = Validator.validateLoan(borrower, amount, interest, description, months);
        if (!result.isValid()) {
            result.printErrors();
            return;
        }

        loanService.addLending(borrower, amount, interest, months, description);
    }

    public void generateAccountReport(String accountId) {
        transactionService.generateAccountReport(accountId);
    }

    public void displayAllAccountReports() {
        List<Account> accounts = accountService.getAllAccounts();

        if (accounts.isEmpty()) {
            System.out.println("Khong co tai khoan nao trong he thong!");
            return;
        }

        for (Account account : accounts) {
            account.generateAccountReport(transactionService.getAllTransactions());
            System.out.println();
        }
    }

    // THÊM: Method xem chi tiết khoản vay
    public void displayLoanDetails(String loanId) {
        loanService.displayLoanDetails(loanId);
    }

    // THÊM: Method xem chi tiết khoản cho vay
    public void displayLendingDetails(String lendingId) {
        loanService.displayLendingDetails(lendingId);
    }

    public void displayAccountComparisonReport() {
        List<Account> accounts = accountService.getAllAccounts();

        if (accounts.isEmpty()) {
            System.out.println("Không có tài khoản nào!");
            return;
        }

        String header = "╔══════════════════════════════════════════════════════════════╗";
        String separator = "╠══════════════════════════════════════════════════════════════╣";
        String footer = "╚══════════════════════════════════════════════════════════════╝";

        System.out.println(header);
        System.out.printf("║ \u001B[1;36m%-60s\u001B[0m ║\n", "SO SÁNH TẤT CẢ TÀI KHOẢN");
        System.out.println(separator);

        double totalBalance = 0;
        for (Account account : accounts) {
            List<Transaction> accountTransactions = transactionService.getTransactionsByAccount(account.getAccountId());
            double accountIncome = calculateAccountIncome(accountTransactions);
            double accountExpense = calculateAccountExpense(accountTransactions);

            System.out.printf("║ %-60s ║\n",account.getAccountName());
            System.out.printf("║ %-60s ║\n",
                    String.format("   Số dư: %,.2f %s | Thu: %,.2f | Chi: %,.2f",
                            account.getBalance(), account.getCurrency(), accountIncome, accountExpense));
            System.out.printf("║ %-60s ║\n", "  " + "-".repeat(56));

            totalBalance += account.getBalance();
        }

        System.out.println(separator);
        System.out.printf("║ \u001B[1;32m%-60s\u001B[0m ║\n",
                String.format("TỔNG SỐ DƯ TẤT CẢ TÀI KHOẢN: %,.2f VND", totalBalance));
        System.out.println(footer);
    }

    private double calculateAccountIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private double calculateAccountExpense(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public void displayAllLoans() {
        loanService.displayAllLoans();
    }

    public void displayAllLendings() {
        loanService.displayAllLendings();
    }

    // Getters for services
    public AccountService getAccountService() { return accountService; }
    public TransactionService getTransactionService() { return transactionService; }
    public LoanService getLoanService() { return loanService; }

    public void repayLoan(String loanId, double amount) {
        boolean success = loanService.repayLoan(loanId, amount);
        if (!success) {
            System.out.println("Tra no that bai!");
        }
    }

    public void collectLending(String lendingId, double amount) {
        boolean success = loanService.collectLending(lendingId, amount);
        if (!success) {
            System.out.println("Thu no that bai!");
        }
    }

    public void depositToAccount(String accountId, double amount) {
        // Validation sẽ được xử lý trong Main.java trước khi gọi phương thức này
        Optional<Account> accountOpt = accountService.findAccountById(accountId);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.deposit(amount);
            System.out.println("Them tien thanh cong!");
            System.out.printf("Tai khoan: %s\n", account.getAccountName());
            System.out.printf("So tien them: %,.2f %s\n", amount, account.getCurrency());
            System.out.printf("So du moi: %,.2f %s\n", account.getBalance(), account.getCurrency());
        } else {
            System.out.println("Khong tim thay tai khoan voi ID: " + accountId);
        }
    }

}