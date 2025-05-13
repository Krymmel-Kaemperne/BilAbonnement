package com.example.bilabonnement.Model;

public enum CustomerType {
    PRIVATE("Privat"),
    BUSINESS("Erhverv");

    private final String displayName;

    CustomerType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }


}
