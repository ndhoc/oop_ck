package com.financemanager.model;

public class Category {
    private String categoryId;
    private String name;
    private String description;
    private TransactionType transactionType;

    public Category(String name, String description, TransactionType transactionType) {
        this.categoryId = "CAT_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.description = description;
        this.transactionType = transactionType;
    }

    // Getter methods
    public String getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public TransactionType getTransactionType() { return transactionType; }

    // Setter methods
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format("Category{id=%s, name=%s, type=%s}",
                categoryId, name, transactionType.getVietnameseName());
    }
}