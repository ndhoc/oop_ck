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
            result.addError("Loại tài khoản không hợp lệ. Chỉ được sử dụng: BANK, E-WALLET, SAVINGS, CASH, CREDIT");
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
    public static ValidationResult validateLoan(String name, double amount, double interest,
                                                String description, int months) {
        ValidationResult result = new ValidationResult();

        if (name == null || name.trim().isEmpty()) {
            result.addError("Ten khong duoc de trong");
        }
        if (amount <= 0) {
            result.addError("So tien phai lon hon 0");
        }
        if (interest < 0) {
            result.addError("Lai suat khong duoc am");
        }
        if (months <= 0) {
            result.addError("So thang phai lon hon 0");
        }
        if (description == null || description.trim().isEmpty()) {
            result.addError("Mo ta khong duoc de trong");
        }

        return result;
    }

    public static ValidationResult validateDeposit(String accountId, double amount, String amountStr) {
        ValidationResult result = new ValidationResult();

        if (accountId == null || accountId.trim().isEmpty()) {
            result.addError("ID tai khoan khong duoc de trong");
        }

        if (!isValidAmountFormat(amountStr)) {
            result.addError("So tien khong hop le. Khong duoc co so 0 o dau va chi chua so");
        }

        if (amount <= 0) {
            result.addError("So tien them phai lon hon 0");
        }

        if (amount > 1000000000) {
            result.addError("So tien them vuot qua gioi han cho phep (1 ty)");
        }

        return result;
    }

    public static boolean isValidAmountFormat(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) {
            return false;
        }

        // Kiểm tra không có số 0 ở đầu (trừ số 0 đơn lẻ)
        if (amountStr.length() > 1 && amountStr.startsWith("0")) {
            return false;
        }

        // Kiểm tra chỉ chứa số và tối đa 2 chữ số thập phân
        return amountStr.matches("^\\d+(\\.\\d{1,2})?$");
    }

    public static ValidationResult validateInitialBalance(String balanceStr) {
        ValidationResult result = new ValidationResult();

        if (balanceStr == null || balanceStr.trim().isEmpty()) {
            result.addError("So du ban dau khong duoc de trong");
            return result;
        }

        if (!isValidAmountFormat(balanceStr)) {
            result.addError("So du ban dau khong hop le. Khong duoc co so 0 o dau va chi chua so");
        }

        try {
            double balance = Double.parseDouble(balanceStr);
            if (balance < 0) {
                result.addError("So du ban dau khong duoc am");
            }
        } catch (NumberFormatException e) {
            result.addError("So du ban dau khong hop le");
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
                        accountType.equalsIgnoreCase("CASH")) ||
                        accountType.equalsIgnoreCase("CREDIT");
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