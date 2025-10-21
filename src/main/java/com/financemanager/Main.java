package com.financemanager;

import com.financemanager.service.FinanceManager;
import com.financemanager.util.Validator;

import java.util.Scanner;

public class Main {
    private static FinanceManager financeManager = new FinanceManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayWelcomeMessage();

        int choice;
        do {
            displayMainMenu();
            choice = getIntInput("Chon chuc nang: ");

            switch (choice) {
                case 1:
                    manageAccounts();
                    break;
                case 2:
                    manageTransactions();
                    break;
                case 3:
                    manageLoans();
                    break;
                case 4:
                    manageReports();
                    break;
                case 5:
                    System.out.println("Cam on ban da su dung ung dung! Tam biet!");
                    break;
                default:
                    System.out.println("Lua chon khong hop le! Vui long chon lai.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void displayWelcomeMessage() {
        System.out.println("=========================================");
        System.out.println("   CHAO MUNG DEN VOI QUAN LY TAI CHINH   ");
        System.out.println("=========================================");
    }

    private static void displayMainMenu() {
        System.out.println("\n=== QUAN LY TAI CHINH CA NHAN ===");
        System.out.println("1. Quan ly tai khoan");
        System.out.println("2. Quan ly giao dich");
        System.out.println("3. Quan ly khoan vay/cho vay");
        System.out.println("4. Bao cao & Thong ke");
        System.out.println("5. Thoat");
    }

    private static void manageAccounts() {
        int choice;
        do {
            System.out.println("\n=== QUAN LY TAI KHOAN ===");
            System.out.println("1. Them tai khoan moi");
            System.out.println("2. Xem danh sach tai khoan");
            System.out.println("3. Xoa tai khoan");
            System.out.println("4. Chuyen khoan noi bo");
            System.out.println("5. Tim kiem tai khoan theo ten");
            System.out.println("6. Them tien vao tai khoan"); // THÊM CHỨC NĂNG MỚI
            System.out.println("7. Quay lai");
            choice = getIntInput("Chon chuc nang: ");

            switch (choice) {
                case 1:
                    addNewAccount();
                    break;
                case 2:
                    financeManager.displayAllAccounts();
                    break;
                case 3:
                    deleteAccount();
                    break;
                case 4:
                    transferBetweenAccounts();
                    break;
                case 5:
                    searchAccountByName();
                    break;
                case 6: // THÊM CHỨC NĂNG MỚI
                    depositToAccount();
                    break;
                case 7:
                    System.out.println("Quay lai menu chinh...");
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 7);
    }

    private static void depositToAccount() {
        System.out.println("\n--- THEM TIEN VAO TAI KHOAN ---");

        financeManager.displayAllAccounts();

        System.out.print("Nhap ID tai khoan can them tien: ");
        String accountId = scanner.nextLine();

        // SỬA: Validate số tiền với input string
        double amount;
        while (true) {
            System.out.print("Nhap so tien can them: ");
            String amountStr = scanner.nextLine();

            try {
                amount = Double.parseDouble(amountStr);
                Validator.ValidationResult result = Validator.validateDeposit(accountId, amount, amountStr);
                if (result.isValid()) {
                    break;
                } else {
                    result.printErrors();
                    System.out.println("Vui long nhap lai.");
                }
            } catch (NumberFormatException e) {
                System.out.println("So tien khong hop le! Vui long nhap lai.");
            }
        }

        financeManager.depositToAccount(accountId, amount);
    }

    private static void searchAccountByName() {
        System.out.print("Nhap ten tai khoan can tim: ");
        String name = scanner.nextLine();
        financeManager.findAccountByName(name);
    }

    private static void manageTransactions() {
        int choice;
        do {
            System.out.println("\n=== QUAN LY GIAO DICH ===");
            System.out.println("1. Them giao dich moi");
            System.out.println("2. Xem lich su giao dich");
            System.out.println("3. Xem so du hien tai");
            System.out.println("4. Xem giao dich theo danh muc");
            System.out.println("5. Quay lai");
            choice = getIntInput("Chon chuc nang: ");

            switch (choice) {
                case 1:
                    addNewTransaction();
                    break;
                case 2:
                    financeManager.displayAllTransactions();
                    break;
                case 3:
                    financeManager.displayCurrentBalance();
                    break;
                case 4:
                    displayTransactionsByCategory();
                    break;
                case 5:
                    System.out.println("Quay lai menu chinh...");
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 5);
    }

    private static void displayTransactionsByCategory() {
        System.out.print("Nhap danh muc can xem: ");
        String category = scanner.nextLine();
        financeManager.displayTransactionsByCategory(category);
    }

    private static void manageReports() {
        int choice;
        do {
            System.out.println("\n=== BAO CAO & THONG KE ===");
            System.out.println("1. Bao cao tai khoan");
            System.out.println("2. Bao cao tong quan tai chinh");
            System.out.println("3. Bao cao thu chi");
            System.out.println("4. Bao cao vay & cho vay");
            System.out.println("5. Bao cao hang thang");
            System.out.println("6. Xuat du lieu CSV");
            System.out.println("7. Quay lai");
            choice = getIntInput("Chon chuc nang: ");

            switch (choice) {
                case 1: // BAO CÁO TÀI KHOẢN - VỊ TRÍ MỚI
                    manageAccountReports();
                    break;
                case 2: // CÁC CHỨC NĂNG KHÁC DỜI XUỐNG
                    financeManager.generateFinancialReport();
                    break;
                case 3:
                    financeManager.generateIncomeExpenseReport();
                    break;
                case 4:
                    financeManager.generateLoanReport();
                    break;
                case 5:
                    financeManager.generateMonthlyReport();
                    break;
                case 6:
                    financeManager.exportToCSV();
                    break;
                case 7:
                    System.out.println("Quay lai menu chinh...");
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 7);
    }

    // update: bao cao tai chinh cho tung tai khoan
    private static void manageAccountReports() {
        int choice;
        do {
            System.out.println("\n=== BAO CAO TAI KHOAN ===");
            System.out.println("1. Bao cao cho tung tai khoan");
            System.out.println("2. Xem tat ca bao cao tai khoan");
            System.out.println("3. So sanh cac tai khoan");
            System.out.println("4. Quay lai");
            choice = getIntInput("Chon chuc nang: ");

            switch (choice) {
                case 1:
                    generateAccountReport();
                    break;
                case 2:
                    financeManager.displayAllAccountReports();
                    break;
                case 3:
                    financeManager.displayAccountComparisonReport();
                    break;
                case 4:
                    System.out.println("Quay lai menu bao cao...");
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 4);
    }

    // THÊM MỚI - TẠO BÁO CÁO CHO TỪNG TÀI KHOẢN
    private static void generateAccountReport() {
        System.out.print("Nhap ID tai khoan can xem bao cao: ");
        String accountId = scanner.nextLine();
        financeManager.generateAccountReport(accountId);
    }

    private static void addNewAccount() {
        System.out.println("\n--- THEM TAI KHOAN MOI ---");

        System.out.print("Nhap ten tai khoan: ");
        String name = scanner.nextLine();

        String[] validTypes = {"BANK", "WALLET", "CASH", "SAVINGS", "CREDIT"};

        String type;
        while (true) {
            System.out.print("Nhap loai tai khoan (BANK/WALLET/CASH/SAVINGS/CREDIT): ");
            type = scanner.nextLine().toUpperCase();

            boolean isValid = false;
            for (String validType : validTypes) {
                if (type.equals(validType)) {
                    isValid = true;
                    break;
                }
            }

            if (isValid) {
                break;
            } else {
                System.out.println("Loai tai khoan khong hop le! Vui long nhap: " + String.join("/", validTypes));
            }
        }

        System.out.print("Nhap so tai khoan: ");
        String number = scanner.nextLine();

        // SỬA: Validate số dư ban đầu với input string
        double balance;
        while (true) {
            System.out.print("Nhap so du ban dau: ");
            String balanceStr = scanner.nextLine().trim();

            Validator.ValidationResult result = Validator.validateInitialBalance(balanceStr);
            if (result.isValid()) {
                try {
                    balance = Double.parseDouble(balanceStr);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("So du khong hop le! Vui long nhap lai.");
                }
            } else {
                result.printErrors();
                System.out.println("Vui long nhap lai.");
            }
        }

        financeManager.addAccount(name, type, number, balance);
    }

    private static void deleteAccount() {
        System.out.print("Nhap ID tai khoan can xoa: ");
        String accountId = scanner.nextLine();
        financeManager.deleteAccount(accountId);
    }

    private static void transferBetweenAccounts() {
        System.out.println("\n--- CHUYEN KHOAN NOI BO ---");

        System.out.print("Nhap ID tai khoan nguon: ");
        String fromId = scanner.nextLine();

        System.out.print("Nhap ID tai khoan dich: ");
        String toId = scanner.nextLine();

        double amount = getDoubleInput("Nhap so tien chuyen: ");

        financeManager.transferBetweenAccounts(fromId, toId, amount);
    }

    private static void addNewTransaction() {
        System.out.println("\n--- THEM GIAO DICH MOI ---");

        System.out.print("Nhap ID tai khoan: ");
        String accountId = scanner.nextLine();

        System.out.print("Nhap loai giao dich (income/expense): ");
        String type = scanner.nextLine();

        double amount = getDoubleInput("Nhap so tien: ");

        System.out.print("Nhap mo ta: ");
        String description = scanner.nextLine();

        System.out.print("Nhap danh muc: ");
        String category = scanner.nextLine();

        financeManager.addTransaction(accountId, type, amount, description, category);
    }

    private static void manageLoans() {
        int choice;
        do {
            System.out.println("\n=== QUAN LY VAY & CHO VAY ===");
            System.out.println("1. Them khoan vay moi");
            System.out.println("2. Them khoan cho vay moi");
            System.out.println("3. Xem danh sach khoan vay");
            System.out.println("4. Xem danh sach khoan cho vay");
            System.out.println("5. Tra no khoan vay");
            System.out.println("6. Thu no khoan cho vay");
            System.out.println("7. Xem cac khoan sap den han");
            System.out.println("8. Quay lai");

            choice = getIntInput("Chon chuc nang: ");

            switch (choice) {
                case 1:
                    addNewLoan();
                    break;
                case 2:
                    addNewLending();
                    break;
                case 3:
                    financeManager.displayAllLoans();
                    break;
                case 4:
                    financeManager.displayAllLendings();
                    break;
                case 5:
                    repayLoan();
                    break;
                case 6:
                    collectLending();
                    break;
                case 7:
                    displayUpcomingDueItems();
                    break;
                case 8:
                    System.out.println("Quay lai menu chinh...");
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 8);
    }

    private static void addNewLoan() {
        System.out.println("\n--- THEM KHOAN VAY MOI ---");

        System.out.print("Nhap ten nguoi cho vay: ");
        String lender = scanner.nextLine();

        double amount = getDoubleInput("Nhap so tien vay: ");

        double interestRate = getDoubleInput("Nhap lai suat (%): ");

        // THÊM: Nhập số tháng vay
        int months = getIntInput("Nhap so thang vay: ");

        System.out.print("Nhap mo ta: ");
        String description = scanner.nextLine();

        // SỬA: Gọi method với số tháng
        financeManager.addLoan(lender, amount, interestRate, months, description);
    }

    private static void addNewLending() {
        System.out.println("\n--- THEM KHOAN CHO VAY MOI ---");

        System.out.print("Nhap ten nguoi vay: ");
        String borrower = scanner.nextLine();

        double amount = getDoubleInput("Nhap so tien cho vay: ");

        double interestRate = getDoubleInput("Nhap lai suat (%): ");

        // THÊM: Nhập số tháng cho vay
        int months = getIntInput("Nhap so thang cho vay: ");

        System.out.print("Nhap mo ta: ");
        String description = scanner.nextLine();

        // SỬA: Gọi method với số tháng
        financeManager.addLending(borrower, amount, interestRate, months, description);
    }

    // Utility methods for input validation
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so nguyen hop le!");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();

                // Kiểm tra số 0 ở đầu
                if (input.length() > 1 && input.startsWith("0")) {
                    System.out.println("Khong duoc nhap so 0 o dau! Vui long nhap lai.");
                    continue;
                }

                double value = Double.parseDouble(input);
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so hop le! (Vi du: 1000, 500.5)");
            }
        }
    }
    private static void repayLoan() {
        System.out.println("\n--- TRA NO KHOAN VAY ---");
        financeManager.displayAllLoans();
        System.out.print("Nhap ID khoan vay can tra no: ");
        String loanId = scanner.nextLine();
        double amount = getDoubleInput("Nhap so tien tra no: ");
        financeManager.repayLoan(loanId, amount);
    }

    private static void collectLending() {
        System.out.println("\n--- THU NO KHOAN CHO VAY ---");
        financeManager.displayAllLendings();
        System.out.print("Nhap ID khoan cho vay can thu no: ");
        String lendingId = scanner.nextLine();
        double amount = getDoubleInput("Nhap so tien thu no: ");
        financeManager.collectLending(lendingId, amount);
    }

    private static void displayUpcomingDueItems() {
        int days = getIntInput("Nhap so ngay sap toi can kiem tra: ");
        financeManager.displayUpcomingDueItems(days);
    }
}