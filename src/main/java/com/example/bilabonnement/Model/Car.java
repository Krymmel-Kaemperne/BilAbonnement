package com.example.bilabonnement.Model;

import java.math.BigDecimal;

public class Car {

    private int carId;
    private String registrationNumber;
    private String chassisNumber;
    private double steelPrice;
    private String color;
    private Double co2Emission;
    private String vehicleNumber;
    private Integer brandId;       // Til at modtage valgt brand fra dropdown
    private String modelName;      // Til at modtage indtastet modelnavn

    private Integer modelId;       // Det faktiske model_id der gemmes i car tabellen

    private Integer carStatusId;
    private Integer fuelTypeId;
    private Integer transmissionTypeId;


    public Car() {
    }

    // Opdater konstruktøren hvis nødvendigt, eller fjern den hvis den ikke bruges aktivt
    public Car(String registrationNumber, String chassisNumber, BigDecimal steelPrice, String color,
               Double co2Emission, String vehicleNumber, Integer modelId, Integer carStatusId,
               Integer fuelTypeId, Integer transmissionTypeId) {
        this.registrationNumber = registrationNumber;
        this.chassisNumber = chassisNumber;
        this.steelPrice = steelPrice;
        this.color = color;
        this.co2Emission = co2Emission;
        this.vehicleNumber = vehicleNumber;
        this.modelId = modelId;
        this.carStatusId = carStatusId;
        this.fuelTypeId = fuelTypeId;
        this.transmissionTypeId = transmissionTypeId;
    }

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getChassisNumber() { return chassisNumber; }
    public void setChassisNumber(String chassisNumber) { this.chassisNumber = chassisNumber; }

    public BigDecimal getSteelPrice() { return steelPrice; }
    public void setSteelPrice(BigDecimal steelPrice) { this.steelPrice = steelPrice; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Double getCo2Emission() { return co2Emission; }
    public void setCo2Emission(Double co2Emission) { this.co2Emission = co2Emission; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public Integer getBrandId() { return brandId; }
    public void setBrandId(Integer brandId) { this.brandId = brandId; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public Integer getModelId() { return modelId; }
    public void setModelId(Integer modelId) { this.modelId = modelId; } // Vigtig for server-side logik

    public Integer getCarStatusId() { return carStatusId; }
    public void setCarStatusId(Integer carStatusId) { this.carStatusId = carStatusId; }

    public Integer getFuelTypeId() { return fuelTypeId; }
    public void setFuelTypeId(Integer fuelTypeId) { this.fuelTypeId = fuelTypeId; }

    public Integer getTransmissionTypeId() { return transmissionTypeId; }
    public void setTransmissionTypeId(Integer transmissionTypeId) { this.transmissionTypeId = transmissionTypeId; }
    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", chassisNumber='" + chassisNumber + '\'' +
                ", steelPrice=" + steelPrice +
                ", color='" + color + '\'' +
                ", co2Emission=" + co2Emission +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", modelId=" + modelId +
                ", carStatusId=" + carStatusId +
                ", fuelTypeId=" + fuelTypeId +
                ", transmissionTypeId=" + transmissionTypeId +
                '}';
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public void setSteelPrice(double steelPrice) {
        this.steelPrice = steelPrice;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setCo2Emission(double co2Emission) {
        this.co2Emission = co2Emission;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setCarStatusId(int carStatusId) {
        this.carStatusId = carStatusId;
    }

    public void setFuelTypeId(int fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }

    public void setTransmissionTypeId(int transmissionTypeId) {
        this.transmissionTypeId = transmissionTypeId;
    }

    public int getModelId() {
        return modelId; }
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getBrandId() {
        return brandId;
    }
    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName; }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
