package com.financemanager.service;

import com.financemanager.model.*;
import com.financemanager.exception.AccountNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionService {
    private List<Transaction> transactions;
    private List<Category> categories;
    private AccountService accountService;

    public TransactionService(AccountService accountService) {
        this.transactions = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.accountService = accountService;
        initializeDefaultCategories();
    }

    private void initializeDefaultCategories() {
        // Income categories
        categories.add(new Category("Luong", "Thu nhap tu luong", TransactionType.INCOME));
        categories.add(new Category("Thuong", "Thuong", TransactionType.INCOME));
        categories.add(new Category("Dau tu", "Thu nhap tu dau tu", TransactionType.INCOME));

        // Expense categories
        categories.add(new Category("An uong", "Chi phi an uong", TransactionType.EXPENSE));
        categories.add(new Category("Di chuyen", "Chi phi di lai", TransactionType.EXPENSE));
        categories.add(new Category("Giai tri", "Chi phi giai tri", TransactionType.EXPENSE));
        categories.add(new Category("Mua sam", "Chi phi mua sam", TransactionType.EXPENSE));
    }

    public void addTransaction(String accountId, TransactionType type, double amount,
                               String description, String categoryName) {

        Optional<Account> accountOpt = accountService.findAccountById(accountId);
        if (accountOpt.isEmpty()) {
            System.out.println("Khong tim thay tai khoan!");
            return;
        }

        Optional<Category> categoryOpt = categories.stream()
                .filter(cat -> cat.getName().equalsIgnoreCase(categoryName))
                .findFirst();

        Category category;
        if (categoryOpt.isPresent()) {
            category = categoryOpt.get();
        } else {
            // Create new category
            category = new Category(categoryName, "Mo ta", type);
            categories.add(category);
        }

        Transaction transaction = new Transaction(accountId, type, amount, description, category);

        if (transaction.isValid()) {
            transactions.add(transaction);

            // Update account balance
            Account account = accountOpt.get();
            if (type == TransactionType.EXPENSE) {
                if (account.getBalance() < amount) {
                    System.out.println(" So du khong du de thuc hien giao dich!");
                    System.out.printf(" So du hien tai: %.2f VND\n", account.getBalance());
                    System.out.printf(" So tien can chi: %.2f VND\n", amount);
                    System.out.printf(" Con thieu: %.2f VND\n", amount - account.getBalance());
                    return; // Dừng lại, không thực hiện giao dịch
                }
            }
            account.updateBalance(amount, type);

            System.out.println("Them giao dich thanh cong!");
            transaction.displayInfo();
        } else {
            System.out.println("Giao dich khong hop le!");
        }
    }

    public void displayAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("Khong co giao dich nao!");
            return;
        }

        System.out.println("\nLICH SU GIAO DICH (" + transactions.size() + " giao dich)");
        System.out.println("=========================================");

        for (Transaction transaction : transactions) {
            transaction.displayInfo();
            System.out.println();
        }
    }

    public void displayTransactionsByAccount(String accountId) {
        List<Transaction> accountTransactions = transactions.stream()
                .filter(transaction -> transaction.getAccountId().equals(accountId))
                .collect(Collectors.toList());

        if (accountTransactions.isEmpty()) {
            System.out.println("Khong co giao dich nao cho tai khoan nay!");
            return;
        }

        System.out.println("\nGIAO DICH TAI KHOAN " + accountId);
        System.out.println("=========================================");

        for (Transaction transaction : accountTransactions) {
            transaction.displayInfo();
            System.out.println();
        }
    }

    public void displayCurrentBalance() {
        accountService.displayAllAccounts();
    }

    public List<Transaction> getTransactionsByCategory(String categoryName) {
        return transactions.stream()
                .filter(transaction -> transaction.getCategory().getName().equalsIgnoreCase(categoryName))
                .collect(Collectors.toList());
    }

    public double getTotalIncome() {
        return transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpense() {
        return transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    public void addCategory(Category category) {
        categories.add(category);
    }
}