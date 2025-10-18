package com.financemanager.model;

public enum TransactionType {
    INCOME("Thu nhập"),
    EXPENSE("Chi tiêu"),
    TRANSFER("Chuyển khoản");

    private final String vietnameseName;

    TransactionType(String vietnameseName) {
        this.vietnameseName = vietnameseName;
    }

    public String getVietnameseName() {
        return vietnameseName;
    }
}