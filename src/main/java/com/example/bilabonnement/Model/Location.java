package com.example.bilabonnement.Model;

public class Location {

    private int locationId;
    private int zipcodeId;
    private String address;
    private String phone;
    private String email;
    private String openingHours;
    private Zipcode zipcode;

    public Location() {
    }

    public Location(int locationId, int zipcodeId, String address,
                    String phone, String email, String openingHours, Zipcode zipcode) {
        this.locationId = locationId;
        this.zipcodeId = zipcodeId;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.openingHours = openingHours;
        this.zipcode = zipcode;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getZipcodeId() {
        return zipcodeId;
    }

    public void setZipcodeId(int zipcodeId) {
        this.zipcodeId = zipcodeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public Zipcode getZipcode() {
        return zipcode;
    }

    public void setZipcode(Zipcode zipcode) {
        this.zipcode = zipcode;
    }

    // Til dropdown
    public String getDisplayName() {
        return address != null ? address : "Ukendt Lokation (ID: " + locationId + ")";
    }

    public String getFullAddressDisplayName() {
        return this.address + ", " + this.zipcode.getZipcode() + " " + this.zipcode.getCityName();
    }

    @Override
    public String toString() {
        return "Location{" +
                "locationId=" + locationId +
                ", zipcodeId=" + zipcodeId +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", openingHours='" + openingHours + '\'' +
                ", zipcode=" + zipcode +
                '}';
    }
}
