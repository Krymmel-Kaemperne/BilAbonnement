package com.example.bilabonnement.Model;


public class BusinessCustomer extends Customer {
        private String cvrNumber;
        private String companyName;

        public BusinessCustomer() {
            super();
        }

        public BusinessCustomer(String fName, String lName, String email, String phone,
                                String address, int zipcodeId, Zipcode zipcode,
                                String cvrNumber, String companyName) {
            super(fName, lName, email, phone, address, zipcodeId, CustomerType.BUSINESS, zipcode);
            this.cvrNumber = cvrNumber;
            this.companyName = companyName;
        }

    public BusinessCustomer(int customerId, String fName, String lName, String email, String phone,
                            String address, int zipcodeId, Zipcode zipcode,
                            String cvrNumber, String companyName) {
        // Kald superklassens constructor for genopbygning
        super(customerId, fName, lName, email, phone, address, zipcodeId, CustomerType.BUSINESS, zipcode);
        this.cvrNumber = cvrNumber;
        this.companyName = companyName;
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

    @Override
    public String getDisplayName() {
        return this.companyName != null && !this.companyName.isEmpty() ? this.companyName : "Ukendt Firma (ID: " + getCustomerId() + ")";
    }


}
