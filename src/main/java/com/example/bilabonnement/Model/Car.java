package com.example.bilabonnement.model;

import java.math.BigDecimal;

public class Car {

    private int carId;
    private String registrationNumber;
    private String chassisNumber;
    private BigDecimal steelPrice;
    private String color;
    private double co2Emission;
    private String vehicleNumber;
    private int modelId;
    private int carStatusId;
    private int fuelTypeId;
    private int transmissionTypeId;

    public Car() {
    }

    public Car(String registrationNumber, String chassisNumber, BigDecimal steelPrice, String color,
               double co2Emission, String vehicleNumber, int modelId, int carStatusId,
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

    public BigDecimal getSteelPrice() {
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

    public int getModelId() {
        return modelId;
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
}
