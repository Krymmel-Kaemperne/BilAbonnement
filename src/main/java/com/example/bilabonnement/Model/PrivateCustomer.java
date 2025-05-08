package com.example.bilabonnement.Model;

public class PrivateCustomer {

    private int privateCustomerId;
    private int customerId;
    private String cprNumber;

    public PrivateCustomer() {
    }

    public PrivateCustomer(int privateCustomerId, int customerId, String cprNumber) {
        this.privateCustomerId = privateCustomerId;
        this.customerId = customerId;
        this.cprNumber = cprNumber;
    }

    public int getPrivateCustomerId() {
        return privateCustomerId;
    }

    public void setPrivateCustomerId(int privateCustomerId) {
        this.privateCustomerId = privateCustomerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCprNumber() {
        return cprNumber;
    }

    public void setCprNumber(String cprNumber) {
        this.cprNumber = cprNumber;
    }
}
