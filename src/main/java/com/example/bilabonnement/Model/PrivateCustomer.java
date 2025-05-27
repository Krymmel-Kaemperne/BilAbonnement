package com.example.bilabonnement.Model;

public class PrivateCustomer extends Customer {
    private String cprNumber;

    public PrivateCustomer() {
        super();
    }

    // Constructor for NYE PrivateCustomer instanser
    public PrivateCustomer(String fName, String lName, String email, String phone,
                           String address, int zipcodeId, Zipcode zipcode, String cprNumber) {
        super(fName, lName, email, phone, address, zipcodeId, CustomerType.PRIVATE, zipcode);
        this.cprNumber = cprNumber;
    }

    // Constructor for at genopbygge PrivateCustomer FRA DATABASEN
    public PrivateCustomer(int customerId, String fName, String lName, String email, String phone,
                           String address, int zipcodeId, Zipcode zipcode, String cprNumber) {

        super(customerId, fName, lName, email, phone, address, zipcodeId, CustomerType.PRIVATE, zipcode);
        this.cprNumber = cprNumber;
    }

    public String getCprNumber() {
        return cprNumber;
    }

    public void setCprNumber(String cprNumber) {
        this.cprNumber = cprNumber;
    }

    @Override
    public String getDisplayName() {
        String fullName = (getFname() + " " + getLname()).trim();
        return fullName.isEmpty() ? "Ukendt Privatkunde (ID: " + getCustomerId() + ")" : fullName;
    }
}
