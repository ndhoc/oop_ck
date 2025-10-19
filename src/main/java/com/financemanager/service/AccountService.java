package com.financemanager.service;

import com.financemanager.model.Account;
import com.financemanager.exception.AccountNotFoundException;
import com.financemanager.exception.InsufficientBalanceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountService {
    private List<Account> accounts;

    public AccountService() {
        this.accounts = new ArrayList<>();
    }

    // Them tai khoan
    public void addAccount(Account account) {
        if (account.isValid()) {
            accounts.add(account);
            System.out.println("Them tai khoan thanh cong: " + account.getAccountName());
        } else {
            System.out.println("Tai khoan khong hop le!");
        }
    }

    // Tim kiem tai khoan qua ID
    public Optional<Account> findAccountById(String accountId) {
        return accounts.stream()
                .filter(account -> account.getAccountId().equals(accountId))
                .findFirst();
    }

    //Xem danh sach tai khoan
    public void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("Khong co tai khoan nao!");
            return;
        }

        System.out.println("\nDANH SACH TAI KHOAN (" + accounts.size() + " tai khoan)");
        System.out.println("=========================================");
        for (Account account : accounts) {
            account.displayInfo();
            System.out.println();
        }

        // Display total balance
        double totalBalance = getTotalBalance();
        System.out.printf("TONG SO DU TAT CA TAI KHOAN: %.2f VND\n", totalBalance);
    }

    // Xoa tai khoan
    public boolean deleteAccount(String accountId) {
        Optional<Account> accountOpt = findAccountById(accountId);
        if (accountOpt.isPresent()) {
            accounts.remove(accountOpt.get());
            System.out.println("Xoa tai khoan thanh cong!");
            return true;
        } else {
            System.out.println("Khong tim thay tai khoan voi ID: " + accountId);
            return false;
        }
    }

    //Chuyen khoan noi bo
    public boolean transferBetweenAccounts(String fromAccountId, String toAccountId, double amount)
            throws AccountNotFoundException, InsufficientBalanceException {

        Optional<Account> fromAccountOpt = findAccountById(fromAccountId);
        Optional<Account> toAccountOpt = findAccountById(toAccountId);

        if (fromAccountOpt.isEmpty()) {
            throw new AccountNotFoundException("Khong tim thay tai khoan nguon: " + fromAccountId);
        }

        if (toAccountOpt.isEmpty()) {
            throw new AccountNotFoundException("Khong tim thay tai khoan dich: " + toAccountId);
        }

        Account fromAccount = fromAccountOpt.get();
        Account toAccount = toAccountOpt.get();

        if (fromAccount.getBalance() < amount) {
            throw new InsufficientBalanceException(
                    "So du khong du! So du hien tai: " + fromAccount.getBalance());
        }

        // Perform transfer
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        System.out.println("Chuyen khoan thanh cong!");
        System.out.printf("Tu: %s -> Den: %s\n", fromAccount.getAccountName(), toAccount.getAccountName());
        System.out.printf("So tien: %.2f VND\n", amount);

        return true;
    }

    public double getTotalBalance() {
        return accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    public int getAccountCount() {
        return accounts.size();
    }
}