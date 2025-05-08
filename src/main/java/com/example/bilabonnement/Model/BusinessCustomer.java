package com.example.bilabonnement.Model;

public class BusinessCustomer {

    private int businessCustomerId;
    private int customerId;
    private String cvrNumber;
    private String companyName;

    public BusinessCustomer() {
    }

    public BusinessCustomer(int businessCustomerId, int customerId, String cvrNumber, String companyName) {
        this.businessCustomerId = businessCustomerId;
        this.customerId = customerId;
        this.cvrNumber = cvrNumber;
        this.companyName = companyName;
    }

    public int getBusinessCustomerId() {
        return businessCustomerId;
    }

    public void setBusinessCustomerId(int businessCustomerId) {
        this.businessCustomerId = businessCustomerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCvrNumber() {
        return cvrNumber;
    }

    public void setCvrNumber(String cvrNumber) {
        this.cvrNumber = cvrNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
