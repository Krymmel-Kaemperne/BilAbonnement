package com.example.bilabonnement.Model;

public class Zipcode {

    private int zipcodeId;
    private String zipcode;
    private String cityName;

    public Zipcode() {
    }

    public Zipcode(int zipcodeId, String zipcode, String cityName) {
        this.zipcodeId = zipcodeId;
        this.zipcode = zipcode;
        this.cityName = cityName;
    }

    public int getZipcodeId() {
        return zipcodeId;
    }

    public void setZipcodeId(int zipcodeId) {
        this.zipcodeId = zipcodeId;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
