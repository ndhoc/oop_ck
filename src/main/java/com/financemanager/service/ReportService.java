package com.financemanager.service;

import com.financemanager.model.*;
import com.financemanager.util.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ReportService {
    private AccountService accountService;
    private TransactionService transactionService;
    private LoanService loanService;

    public ReportService(AccountService accountService, TransactionService transactionService,
                         LoanService loanService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.loanService = loanService;
    }

    // Báo cáo tổng quan tài chính
    public void generateFinancialOverview() {
        System.out.println("\nBAO CAO TONG QUAN TAI CHINH");
        System.out.println("=========================================");

        double totalBalance = accountService.getTotalBalance();
        double totalIncome = transactionService.getTotalIncome();
        double totalExpense = transactionService.getTotalExpense();
        double netCashFlow = totalIncome - totalExpense;
        double totalLoans = loanService.getTotalLoanAmount();
        double totalLendings = loanService.getTotalLendingAmount();
        double netWorth = totalBalance + totalLendings - totalLoans;

        System.out.printf("TONG TAI SAN: %,.2f VND\n", totalBalance);
        System.out.printf("TONG THU NHAP: %,.2f VND\n", totalIncome);
        System.out.printf("TONG CHI TIEU: %,.2f VND\n", totalExpense);
        System.out.printf("LUONG TIEN MAT RONG: %,.2f VND\n", netCashFlow);
        System.out.printf("TONG NO PHAI TRA: %,.2f VND\n", totalLoans);
        System.out.printf("TONG NO PHAI THU: %,.2f VND\n", totalLendings);
        System.out.printf("GIA TRI TAI SAN RONG: %,.2f VND\n", netWorth);

        // Phân tích tỷ lệ
        if (totalIncome > 0) {
            double expenseRatio = (totalExpense / totalIncome) * 100;
            double savingsRatio = 100 - expenseRatio;
            System.out.printf("TY LE CHI TIEU/THU NHAP: %.1f%%\n", expenseRatio);
            System.out.printf("TY LE TIET KIEM: %.1f%%\n", savingsRatio);
        }
    }

    // Báo cáo thu nhập và chi tiêu theo khoảng thời gian
    public void generateIncomeExpenseReport(LocalDate startDate, LocalDate endDate) {
        System.out.println("\nBAO CAO THU CHI (" + startDate + " - " + endDate + ")");
        System.out.println("=========================================");

        List<Transaction> transactions = transactionService.getAllTransactions().stream()
                .filter(tx -> !tx.getDate().toLocalDate().isBefore(startDate) &&
                        !tx.getDate().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());

        double periodIncome = transactions.stream()
                .filter(tx -> tx.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double periodExpense = transactions.stream()
                .filter(tx -> tx.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        System.out.printf("THU NHAP TRONG KY: %,.2f VND\n", periodIncome);
        System.out.printf("CHI TIEU TRONG KY: %,.2f VND\n", periodExpense);
        System.out.printf("CHENH LECH: %,.2f VND\n", periodIncome - periodExpense);

        // Phân tích theo danh mục
        System.out.println("\nPHAN TICH THEO DANH MUC:");
        Map<String, Double> expenseByCategory = transactions.stream()
                .filter(tx -> tx.getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        tx -> tx.getCategory().getName(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        expenseByCategory.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> {
                    double percentage = (entry.getValue() / periodExpense) * 100;
                    System.out.printf("  ▸ %-15s: %,.2f VND (%.1f%%)\n",
                            entry.getKey(), entry.getValue(), percentage);
                });
    }

    // Báo cáo theo tài khoản
    public void generateAccountReport(String accountId) {
        Optional<Account> accountOpt = accountService.findAccountById(accountId);
        if (accountOpt.isEmpty()) {
            System.out.println("Khong tim thay tai khoan!");
            return;
        }

        Account account = accountOpt.get();
        List<Transaction> accountTransactions = transactionService.getAllTransactions().stream()
                .filter(tx -> tx.getAccountId().equals(accountId))
                .collect(Collectors.toList());

        System.out.println("\nBAO CAO TAI KHOAN: " + account.getAccountName());
        System.out.println("=========================================");

        account.displayInfo();

        double accountIncome = accountTransactions.stream()
                .filter(tx -> tx.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double accountExpense = accountTransactions.stream()
                .filter(tx -> tx.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();

        System.out.printf("\nTONG THU: %,.2f VND\n", accountIncome);
        System.out.printf("TONG CHI: %,.2f VND\n", accountExpense);
        System.out.printf("SO GIAO DICH: %d\n", accountTransactions.size());

        // Top 5 giao dịch lớn nhất
        System.out.println("\nTOP 5 GIAO DICH LON NHAT:");
        accountTransactions.stream()
                .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
                .limit(5)
                .forEach(tx -> {
                    String symbol = tx.getType() == TransactionType.INCOME ? "↑" : "↓";
                    System.out.printf("  %s %-12s: %,.2f VND - %s\n",
                            symbol, tx.getCategory().getName(),
                            tx.getAmount(), tx.getDescription());
                });
    }

    // Báo cáo khoản vay và cho vay
    public void generateLoanReport() {
        System.out.println("\nBAO CAO VAY & CHO VAY");
        System.out.println("=========================================");

        // Khoản vay
        List<Loan> loans = loanService.getAllLoans();
        System.out.println("KHOAN VAY (" + loans.size() + " khoan):");
        loans.forEach(loan -> {
            String statusIcon = loan.isOverdue() ? "🔴" : "🟢";
            System.out.printf("  %s %-20s: %,.2f / %,.2f VND (Con no: %,.2f)\n",
                    statusIcon, loan.getLenderName(),
                    loan.getPrincipalAmount() - loan.getRemainingAmount(),
                    loan.getPrincipalAmount(), loan.getRemainingAmount());
        });

        // Khoản cho vay
        List<Lending> lendings = loanService.getAllLendings();
        System.out.println("\nKHOAN CHO VAY (" + lendings.size() + " khoan):");
        lendings.forEach(lending -> {
            String statusIcon = lending.isOverdue() ? "🔴" : "🟢";
            System.out.printf("  %s %-20s: %,.2f / %,.2f VND (Con thu: %,.2f)\n",
                    statusIcon, lending.getBorrowerName(),
                    lending.getPrincipalAmount() - lending.getRemainingAmount(),
                    lending.getPrincipalAmount(), lending.getRemainingAmount());
        });

        // Tổng hợp
        double totalDebt = loanService.getTotalLoanAmount();
        double totalReceivable = loanService.getTotalLendingAmount();
        System.out.printf("\nTONG NO PHAI TRA: %,.2f VND\n", totalDebt);
        System.out.printf("TONG NO PHAI THU: %,.2f VND\n", totalReceivable);
        System.out.printf("CHENH LECH: %,.2f VND\n", totalReceivable - totalDebt);
    }

    // Báo cáo hàng tháng
    public void generateMonthlyReport(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        System.out.println("\nBAO CAO THANG " + month + "/" + year);
        System.out.println("=========================================");

        generateIncomeExpenseReport(startDate, endDate);

        // Thống kê hàng ngày
        System.out.println("\nBIEN DONG HANG NGAY:");
        Map<LocalDate, Double> dailyBalance = new TreeMap<>();

        // Tính toán số dư hàng ngày (đơn giản)
        double runningBalance = accountService.getTotalBalance();
        for (int day = startDate.lengthOfMonth(); day >= 1; day--) {
            LocalDate currentDate = LocalDate.of(year, month, day);
            dailyBalance.put(currentDate, runningBalance);
            // Giả lập biến động (trong thực tế cần tính toán chính xác)
            runningBalance -= 100000; // Giả định mỗi ngày giảm 100k
        }

        dailyBalance.entrySet().stream()
                .limit(10) // Hiển thị 10 ngày đầu
                .forEach(entry -> {
                    System.out.printf("  %s: %,.2f VND\n", entry.getKey(), entry.getValue());
                });
    }

    // Xuất dữ liệu đơn giản (có thể mở rộng để xuất file)
    public void exportToCSV() {
        System.out.println("\nXUAT DU LIEU (CSV Format):");
        System.out.println("=========================================");

        // Xuất danh sách tài khoản
        System.out.println("TAI_KHOAN_ID,TEN,LOAI,SO_DU");
        accountService.getAllAccounts().forEach(acc -> {
            System.out.printf("%s,%s,%s,%.2f\n",
                    acc.getAccountId(), acc.getAccountName(),
                    acc.getAccountType(), acc.getBalance());
        });

        // Xuất danh sách giao dịch
        System.out.println("\nGIAO_DICH_ID,TAI_KHOAN,LOAI,SOTIEN,DANHMUC,NGAY");
        transactionService.getAllTransactions().forEach(tx -> {
            System.out.printf("%s,%s,%s,%.2f,%s,%s\n",
                    tx.getTransactionId(), tx.getAccountId(),
                    tx.getType().name(), tx.getAmount(),
                    tx.getCategory().getName(), tx.getFormattedDate());
        });
    }
}