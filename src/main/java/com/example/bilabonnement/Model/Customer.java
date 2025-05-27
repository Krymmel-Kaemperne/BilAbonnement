package com.example.bilabonnement.Model;

public abstract class Customer {
    private int customerId;
    private String fName;
    private String lName;
    private String email;
    private String phone;
    private String address;
    private int zipcodeId;
    private Zipcode zipcode;
    private CustomerType customerType;

    public Customer() {
    }

    public Customer(String fName, String lName, String email, String phone,
                    String address, int zipcodeId, CustomerType customerType,
                    Zipcode zipcode) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.zipcodeId = zipcodeId;
        this.zipcode = zipcode;
        this.customerType = customerType;
    }

    public Customer(int customerId, String fName, String lName, String email, String phone,
                    String address, int zipcodeId, CustomerType customerType,
                    Zipcode zipcode) {
        this.customerId = customerId;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.zipcodeId = zipcodeId;
        this.zipcode = zipcode;
        this.customerType = customerType;
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


    public Zipcode getZipcode() {
        return zipcode;
    }

    public void setZipcode(Zipcode zipcode) {
        this.zipcode = zipcode;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getDisplayName() {
        String firstNamePart = (getFname() != null) ? getFname() : "";
        String lastNamePart = (getLname() != null) ? getLname() : "";
        String fullName = (firstNamePart + " " + lastNamePart).trim();
        return fullName.isEmpty() ? "Ukendt Kunde (ID: " + customerId + ")" : fullName;
    }
}
