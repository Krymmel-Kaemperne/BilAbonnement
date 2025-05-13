package com.example.bilabonnement.Model;

public class Brand {
    private int brandId;
    private String name;

    public Brand() {}
    public Brand(int brandId, String name) {
        this.brandId = brandId;
        this.name = name;
    }
    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}