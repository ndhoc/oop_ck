package com.financemanager.util;

public class Constants {

    // Application constants
    public static final String APP_NAME = "Finance Manager";
    public static final String APP_VERSION = "1.0.0";
    public static final String DEFAULT_CURRENCY = "VND";

    // File constants
    public static final String DATA_DIRECTORY = "data/";
    public static final String ACCOUNTS_FILE = "accounts.dat";
    public static final String TRANSACTIONS_FILE = "transactions.dat";
    public static final String LOANS_FILE = "loans.dat";
    public static final String CATEGORIES_FILE = "categories.dat";

    // Account types
    public static final String ACCOUNT_TYPE_BANK = "BANK";
    public static final String ACCOUNT_TYPE_WALLET = "E-WALLET";
    public static final String ACCOUNT_TYPE_SAVINGS = "SAVINGS";
    public static final String ACCOUNT_TYPE_CASH = "CASH";
    public static final String ACCOUNT_TYPE_CREDIT = "CREDIT";

    // Transaction categories
    public static class IncomeCategories {
        public static final String SALARY = "Luong";
        public static final String BONUS = "Thuong";
        public static final String INVESTMENT = "Dau tu";
        public static final String FREELANCE = "Freelance";
        public static final String OTHER_INCOME = "Thu nhap khac";
    }

    public static class ExpenseCategories {
        public static final String FOOD = "An uong";
        public static final String TRANSPORT = "Di chuyen";
        public static final String ENTERTAINMENT = "Giai tri";
        public static final String SHOPPING = "Mua sam";
        public static final String BILLS = "Hoa don";
        public static final String HEALTHCARE = "Suc khoe";
        public static final String EDUCATION = "Giao duc";
        public static final String OTHER_EXPENSE = "Chi tieu khac";
    }

    // Loan status
    public static final String LOAN_STATUS_ACTIVE = "ACTIVE";
    public static final String LOAN_STATUS_PAID = "PAID";
    public static final String LOAN_STATUS_OVERDUE = "OVERDUE";
    public static final String LOAN_STATUS_CANCELLED = "CANCELLED";

    // Payment methods
    public static final String PAYMENT_CASH = "CASH";
    public static final String PAYMENT_BANK_TRANSFER = "BANK_TRANSFER";
    public static final String PAYMENT_EWALLET = "E_WALLET";
    public static final String PAYMENT_CREDIT_CARD = "CREDIT_CARD";

    // Date formats
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String MONTH_YEAR_FORMAT = "MM/yyyy";

    // Numeric constants
    public static final double MAX_TRANSACTION_AMOUNT = 1_000_000_000.0; // 1 tỷ
    public static final double MIN_TRANSACTION_AMOUNT = 1000.0; // 1,000 VND
    public static final int MAX_ACCOUNT_NAME_LENGTH = 50;
    public static final int MAX_DESCRIPTION_LENGTH = 200;

    // Interest calculation
    public static final int DAYS_IN_YEAR = 365;
    public static final int MONTHS_IN_YEAR = 12;

    // Budget categories (6 jars method)
    public static class BudgetJars {
        public static final String NECESSITIES = "Nhu yeu pham";        // 55%
        public static final String FINANCIAL_FREEDOM = "Tu do tai chinh"; // 10%
        public static final String EDUCATION = "Giao duc";             // 10%
        public static final String LONG_TERM_SAVINGS = "Tiet kiem dai han"; // 10%
        public static final String PLAY = "Giai tri";                  // 10%
        public static final String GIVING = "Cho di";                  // 5%
    }

    // Color codes for console (ANSI)
    public static class Colors {
        public static final String RESET = "\u001B[0m";
        public static final String RED = "\u001B[31m";
        public static final String GREEN = "\u001B[32m";
        public static final String YELLOW = "\u001B[33m";
        public static final String BLUE = "\u001B[34m";
        public static final String PURPLE = "\u001B[35m";
        public static final String CYAN = "\u001B[36m";

        public static String colorize(String text, String color) {
            return color + text + RESET;
        }
    }

    // Validation messages
    public static class Messages {
        public static final String INVALID_AMOUNT = "So tien khong hop le";
        public static final String INVALID_ACCOUNT = "Tai khoan khong hop le";
        public static final String INSUFFICIENT_BALANCE = "So du tai khoan khong du";
        public static final String TRANSACTION_SUCCESS = "Giao dich thanh cong";
        public static final String ACCOUNT_CREATED = "Tao tai khoan thanh cong";
        public static final String LOAN_ADDED = "Them khoan vay thanh cong";
    }
}