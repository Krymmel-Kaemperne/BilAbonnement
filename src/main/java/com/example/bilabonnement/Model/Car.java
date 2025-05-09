package com.example.bilabonnement.Model; // SKAL VÆRE FØRSTE LINJE

// import java.math.BigDecimal; // Ikke længere nødvendig hvis steelPrice er double

public class Car {

    private int carId;
    private String registrationNumber;
    private String chassisNumber;
    private double steelPrice; // Konsekvent double
    private String color;
    private Double co2Emission; // Forbliver Double for at kunne være null fra form/database
    private String vehicleNumber;

    // Felter til form binding
    private Integer brandId;    // ID for valgt mærke fra dropdown
    private String modelName;   // Navn på model indtastet af bruger
    private String brandName;

    // Dette felt sættes typisk server-side efter at have fundet/oprettet modellen
    private Integer modelId;    // Det faktiske model_id der gemmes i car tabellen

    private Integer carStatusId;
    private Integer fuelTypeId;
    private Integer transmissionTypeId;
    private String comment;     // For kommentar-feltet

    // Tom konstruktør er vigtig for Spring/Thymeleaf databinding
    public Car() {
    }

    // Fuld konstruktør (valgfri, men kan være nyttig til test eller manuel oprettelse)
    public Car(String registrationNumber, String chassisNumber, double steelPrice, String color,
               Double co2Emission, String vehicleNumber, Integer brandId, String modelName,
               Integer carStatusId, Integer fuelTypeId, Integer transmissionTypeId, String comment) {
        this.registrationNumber = registrationNumber;
        this.chassisNumber = chassisNumber;
        this.steelPrice = steelPrice;
        this.color = color;
        this.co2Emission = co2Emission;
        this.vehicleNumber = vehicleNumber;
        this.brandId = brandId;
        this.modelName = modelName;
        // this.modelId ville typisk blive sat separat efter model opslag/oprettelse
        this.carStatusId = carStatusId;
        this.fuelTypeId = fuelTypeId;
        this.transmissionTypeId = transmissionTypeId;
        this.comment = comment;
    }

    // Getters and Setters

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

    public String getBrandName() {
        return brandName;
    }
    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", chassisNumber='" + chassisNumber + '\'' +
                ", steelPrice=" + steelPrice + // Viser double
                ", color='" + color + '\'' +
                ", co2Emission=" + co2Emission +
                ", vehicleNumber='" + vehicleNumber + '\'' +
                ", brandId=" + brandId +
                ", modelName='" + modelName + '\'' +
                ", modelId=" + modelId +
                ", carStatusId=" + carStatusId +
                ", fuelTypeId=" + fuelTypeId +
                ", transmissionTypeId=" + transmissionTypeId +
                ", comment='" + comment + '\'' +
                '}';
    }
}
