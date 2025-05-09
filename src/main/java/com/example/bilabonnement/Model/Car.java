package com.example.bilabonnement.Model;

public class Car {

    private int carId;
    private String registrationNumber;
    private String chassisNumber;
    private double steelPrice;
    private String color;
    private double co2Emission;
    private String vehicleNumber;
    private int modelId;
    private String modelName;
    private int brandId;
    private String brandName;
    private int carStatusId;
    private int fuelTypeId;
    private int transmissionTypeId;


    public Car() {
    }

    public Car(String registrationNumber, String chassisNumber, double steelPrice, String color,
               double co2Emission, String vehicleNumber, int modelId,  int carStatusId,
               int fuelTypeId, int transmissionTypeId) {
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


    public int getCarId() {
        return carId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public double getSteelPrice() {
        return steelPrice;
    }

    public String getColor() {
        return color;
    }

    public double getCo2Emission() {
        return co2Emission;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }


    public int getCarStatusId() {
        return carStatusId;
    }

    public int getFuelTypeId() {
        return fuelTypeId;
    }

    public int getTransmissionTypeId() {
        return transmissionTypeId;
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
