package com.financemanager.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Validator {

    // Validation patterns
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^[A-Za-z0-9]{5,20}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}0-9\\s]{2,50}$");
    private static final Pattern DESCRIPTION_PATTERN = Pattern.compile("^[\\p{L}0-9\\s.,!?-]{0,200}$");

    // Account validation
    public static ValidationResult validateAccount(String accountName, String accountType,
                                                   String accountNumber, double balance) {
        ValidationResult result = new ValidationResult();

        if (!isValidName(accountName)) {
            result.addError("Tên tài khoản phải từ 2-50 ký tự và chỉ chứa chữ cái, số, khoảng trắng");
        }

        if (!isValidAccountType(accountType)) {
            result.addError("Loại tài khoản không hợp lệ. Ví dụ: BANK, WALLET, SAVINGS");
        }

        if (!isValidAccountNumber(accountNumber)) {
            result.addError("Số tài khoản phải từ 5-20 ký tự và chỉ chứa chữ cái, số");
        }

        if (!isValidAmount(balance)) {
            result.addError("Số dư không thể âm");
        }

        return result;
    }

    // Transaction validation
    public static ValidationResult validateTransaction(String accountId, String type,
                                                       double amount, String description) {
        ValidationResult result = new ValidationResult();

        if (!isValidId(accountId)) {
            result.addError("ID tài khoản không hợp lệ");
        }

        if (!isValidTransactionType(type)) {
            result.addError("Loại giao dịch phải là INCOME hoặc EXPENSE");
        }

        if (!isValidPositiveAmount(amount)) {
            result.addError("Số tiền phải lớn hơn 0");
        }

        if (!isValidDescription(description)) {
            result.addError("Mô tả không được vượt quá 200 ký tự");
        }

        return result;
    }

    // Loan validation
    public static ValidationResult validateLoan(String lenderName, double amount,
                                                double interestRate, String description) {
        ValidationResult result = new ValidationResult();

        if (!isValidName(lenderName)) {
            result.addError("Tên người cho vay phải từ 2-50 ký tự");
        }

        if (!isValidPositiveAmount(amount)) {
            result.addError("Số tiền vay phải lớn hơn 0");
        }

        if (!isValidInterestRate(interestRate)) {
            result.addError("Lãi suất phải từ 0-100%");
        }

        if (!isValidDescription(description)) {
            result.addError("Mô tả không được vượt quá 200 ký tự");
        }

        return result;
    }

    // Individual validation methods
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches();
    }

    public static boolean isValidAccountType(String accountType) {
        return accountType != null &&
                (accountType.equalsIgnoreCase("BANK") ||
                        accountType.equalsIgnoreCase("WALLET") ||
                        accountType.equalsIgnoreCase("SAVINGS") ||
                        accountType.equalsIgnoreCase("CASH"));
    }

    public static boolean isValidTransactionType(String type) {
        return type != null &&
                (type.equalsIgnoreCase("INCOME") ||
                        type.equalsIgnoreCase("EXPENSE"));
    }

    public static boolean isValidAmount(double amount) {
        return amount >= 0;
    }

    public static boolean isValidPositiveAmount(double amount) {
        return amount > 0;
    }

    public static boolean isValidInterestRate(double interestRate) {
        return interestRate >= 0 && interestRate <= 100;
    }

    public static boolean isValidDescription(String description) {
        return description == null || DESCRIPTION_PATTERN.matcher(description).matches();
    }

    public static boolean isValidId(String id) {
        return id != null && !id.trim().isEmpty() && id.length() <= 50;
    }

    public static boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null && !startDate.isAfter(endDate);
    }

    public static boolean isFutureDate(LocalDateTime date) {
        return date != null && date.isAfter(LocalDateTime.now());
    }

    // Validation result class
    public static class ValidationResult {
        private List<String> errors;

        public ValidationResult() {
            this.errors = new ArrayList<>();
        }

        public void addError(String error) {
            errors.add(error);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public List<String> getErrors() {
            return new ArrayList<>(errors);
        }

        public void printErrors() {
            if (!isValid()) {
                System.out.println("LOI XAC THUC:");
                errors.forEach(error -> System.out.println("   • " + error));
            }
        }
    }
}