package com.example.bilabonnement.Model;

// import java.math.BigDecimal; // Ikke nødvendig hvis steelPrice er double

public class Car {

    private int carId;
    private String registrationNumber;
    private String chassisNumber;
    private double steelPrice; // Konsekvent double
    private String color;
    private Double co2Emission;
    private String vehicleNumber;

    // Felter til form binding og visning
    private Integer brandId;
    private String modelName;
    private String brandName;   // TILFØJET IGEN - Nødvendig for setBrandName()
    private String comment;     // TILFØJET - For kommentar-feltet

    // Dette felt sættes typisk server-side
    private Integer modelId;

    private Integer carStatusId;
    private Integer fuelTypeId;
    private Integer transmissionTypeId;

    public Car() {
    }

    // Konstruktør opdateret
    public Car(String registrationNumber, String chassisNumber, double steelPrice, String color,
               Double co2Emission, String vehicleNumber, Integer brandId, String modelName, String brandName,
               Integer carStatusId, Integer fuelTypeId, Integer transmissionTypeId, String comment) {
        this.registrationNumber = registrationNumber;
        this.chassisNumber = chassisNumber;
        this.steelPrice = steelPrice; // Bruger double
        this.color = color;
        this.co2Emission = co2Emission;
        this.vehicleNumber = vehicleNumber;
        this.brandId = brandId;
        this.modelName = modelName;
        this.brandName = brandName; // Tilføjet
        this.carStatusId = carStatusId;
        this.fuelTypeId = fuelTypeId;
        this.transmissionTypeId = transmissionTypeId;
        this.comment = comment; // Tilføjet
    }

    // Getters and Setters (oprenset og korrigeret)

    public int getCarId() {
        return carId;
    }
    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }
    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public double getSteelPrice() { // Returnerer double
        return steelPrice;
    }
    public void setSteelPrice(double steelPrice) { // Accepterer double
        this.steelPrice = steelPrice;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public Double getCo2Emission() {
        return co2Emission;
    }
    public void setCo2Emission(Double co2Emission) {
        this.co2Emission = co2Emission;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Integer getBrandId() {
        return brandId;
    }
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getBrandName() { // Getter for brandName
        return brandName;
    }
    public void setBrandName(String brandName) { // Setter for brandName
        this.brandName = brandName;
    }

    public Integer getModelId() {
        return modelId;
    }
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getCarStatusId() {
        return carStatusId;
    }
    public void setCarStatusId(Integer carStatusId) {
        this.carStatusId = carStatusId;
    }

    public Integer getFuelTypeId() {
        return fuelTypeId;
    }
    public void setFuelTypeId(Integer fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }

    public Integer getTransmissionTypeId() {
        return transmissionTypeId;
    }
    public void setTransmissionTypeId(Integer transmissionTypeId) {
        this.transmissionTypeId = transmissionTypeId;
    }

    public String getComment() { // Getter for comment
        return comment;
    }
    public void setComment(String comment) { // Setter for comment
        this.comment = comment;
    }

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
                ", brandId=" + brandId +
                ", modelName='" + modelName + '\'' +
                ", brandName='" + brandName + '\'' + // Tilføjet
                ", modelId=" + modelId +
                ", carStatusId=" + carStatusId +
                ", fuelTypeId=" + fuelTypeId +
                ", transmissionTypeId=" + transmissionTypeId +
                ", comment='" + comment + '\'' + // Tilføjet
                '}';
    }
}
