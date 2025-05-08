package com.example.bilabonnement.Model;

public class Customer {

    private int customerId;
    private String fName;
    private String lName;
    private String email;
    private String phone;
    private String address;
    private int zipcodeId;
    private CustomerType customerType;
    private Zipcode zipcode;
    private PrivateCustomer privateCustomerDetails;
    private BusinessCustomer businessCustomerDetails;

    public Customer() {
    }

    public Customer(int customerId, String fName, String lName, String email, String phone,
                    String address, int zipcodeId, CustomerType customerType,
                    Zipcode zipcode, PrivateCustomer privateCustomerDetails,
                    BusinessCustomer businessCustomerDetails) {
        this.customerId = customerId;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.zipcodeId = zipcodeId;
        this.customerType = customerType;
        this.zipcode = zipcode;
        this.privateCustomerDetails = privateCustomerDetails;
        this.businessCustomerDetails = businessCustomerDetails;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getFname() {
        return fName;
    }

    public void setFname(String fName) {
        this.fName = fName;
    }

    public String getLname() {
        return lName;
    }

    public void setLname(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZipcodeId() {
        return zipcodeId;
    }

    public void setZipcodeId(int zipcodeId) {
        this.zipcodeId = zipcodeId;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public Zipcode getZipcode() {
        return zipcode;
    }

    public void setZipcode(Zipcode zipcode) {
        this.zipcode = zipcode;
    }

    public PrivateCustomer getPrivateCustomerDetails() {
        return privateCustomerDetails;
    }

    public void setPrivateCustomerDetails(PrivateCustomer privateCustomerDetails) {
        this.privateCustomerDetails = privateCustomerDetails;
    }

    public BusinessCustomer getBusinessCustomerDetails() {
        return businessCustomerDetails;
    }

    public void setBusinessCustomerDetails(BusinessCustomer businessCustomerDetails) {
        this.businessCustomerDetails = businessCustomerDetails;
    }
}
