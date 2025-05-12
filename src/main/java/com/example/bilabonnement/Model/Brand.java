package com.example.bilabonnement.Model;

public class Brand {
    private Integer brandId; // Matcher 'brand_id' fra din database
    private String brandName; // Matcher 'brand_name' fra din database

    public Brand() {
    }

    // Konstruktør med felter (valgfri, men kan være nyttig)
    public Brand(Integer brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }

    // Getters
    public Integer getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    // Setters
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public String toString() {
        return "BrandDTO{" +
                "brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                '}';
    }
}
