package com.example.bilabonnement.Model;

public class FuelType {
    private Integer fuelTypeId;
    private String fuelTypeName;

    // Tom konstruktør
    public FuelType() {
    }

    // Konstruktør med felter
    public FuelType(Integer fuelTypeId, String fuelTypeName) {
        this.fuelTypeId = fuelTypeId;
        this.fuelTypeName = fuelTypeName;
    }

    public Integer getFuelTypeId() {
        return fuelTypeId;
    }

    public String getFuelTypeName() {
        return fuelTypeName;
    }

    public void setFuelTypeId(Integer fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }

    public void setFuelTypeName(String fuelTypeName) {
        this.fuelTypeName = fuelTypeName;
    }

    @Override
    public String toString() {
        return "FuelType{" +
                "fuelTypeId=" + fuelTypeId +
                ", fuelTypeName='" + fuelTypeName + '\'' +
                '}';
    }
}
