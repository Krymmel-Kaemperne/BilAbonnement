package com.example.bilabonnement.Model;

public class Car {
    private int carId;
    private String registrationNumber;
    private String chassisNumber;
    private double steelPrice;
    private String color;
    private Double co2Emission; // Kan være null
    private String vehicleNumber;
    private String comment;
    private int current_odometer;

    // ID'er til databaselagring og relationer
    private Integer modelId;
    private Integer brandId; // Bruges primært til form-binding og model opslag
    private Integer carStatusId;
    private Integer fuelTypeId;
    private Integer transmissionTypeId;

    // Navne til visning (hentes via JOINs)
    private String modelName;
    private String brandName;
    private String carStatusName;
    private String fuelTypeName;
    private String transmissionTypeName;

    public Car() {
    }


    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getChassisNumber() { return chassisNumber; }
    public void setChassisNumber(String chassisNumber) { this.chassisNumber = chassisNumber; }

    public double getSteelPrice() { return steelPrice; }
    public void setSteelPrice(double steelPrice) { this.steelPrice = steelPrice; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Double getCo2Emission() { return co2Emission; }
    public void setCo2Emission(Double co2Emission) { this.co2Emission = co2Emission; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Integer getModelId() { return modelId; }
    public void setModelId(Integer modelId) { this.modelId = modelId; }

    public Integer getBrandId() { return brandId; }
    public void setBrandId(Integer brandId) { this.brandId = brandId; }

    public Integer getCarStatusId() { return carStatusId; }
    public void setCarStatusId(Integer carStatusId) { this.carStatusId = carStatusId; }

    public Integer getFuelTypeId() { return fuelTypeId; }
    public void setFuelTypeId(Integer fuelTypeId) { this.fuelTypeId = fuelTypeId; }

    public Integer getTransmissionTypeId() { return transmissionTypeId; }
    public void setTransmissionTypeId(Integer transmissionTypeId) { this.transmissionTypeId = transmissionTypeId; }

    public String getModelName() { return modelName; }
    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getCarStatusName() { return carStatusName; }
    public void setCarStatusName(String carStatusName) { this.carStatusName = carStatusName; }

    public String getFuelTypeName() { return fuelTypeName; }
    public void setFuelTypeName(String fuelTypeName) { this.fuelTypeName = fuelTypeName; }

    public String getTransmissionTypeName() { return transmissionTypeName; }
    public void setTransmissionTypeName(String transmissionTypeName) { this.transmissionTypeName = transmissionTypeName; }
    public int getCurrent_odometer() {
        return current_odometer;
    }

    public void setCurrent_odometer(int current_odometer) {
        this.current_odometer = current_odometer;
    }

    @Override
    public String toString() {
        return "Car{" + // ... inkluder alle felter ...
                "carId=" + carId +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", modelName='" + modelName + '\'' +
                ", brandName='" + brandName + '\'' +
                '}';
    }
}
