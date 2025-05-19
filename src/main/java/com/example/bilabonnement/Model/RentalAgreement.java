package com.example.bilabonnement.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalAgreement {
    private int rentalAgreementId;
    private int carId;
    private int customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal monthlyPrice;
    private int kilometersIncluded;
    private int startOdometer;
    private Integer endOdometer;
    private int pickupLocationId;
    private Integer returnLocationId;
    private String leasingCode;

    // DEFAUlT CONSTUCTOR
    public RentalAgreement() {
    }

    //CONSTRUCTOR WITHOUT ID
    public RentalAgreement(int rentalAgreementId, int carId, int customerId,
                           LocalDate startDate, LocalDate endDate, BigDecimal monthlyPrice,
                           int kilometersIncluded, int startOdometer, Integer endOdometer,
                           int pickupLocationId, Integer returnLocationId, String leasingCode) {
        this.rentalAgreementId = rentalAgreementId;
        this.carId = carId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyPrice = monthlyPrice;
        this.kilometersIncluded = kilometersIncluded;
        this.startOdometer = startOdometer;
        this.endOdometer = endOdometer;
        this.pickupLocationId = pickupLocationId;
        this.returnLocationId = returnLocationId;
        this.leasingCode = leasingCode;
    }

    //CONSTRUCTOR WITH ID
    public RentalAgreement(int carId, int customerId, LocalDate startDate, LocalDate endDate,
                           BigDecimal monthlyPrice, int kilometersIncluded, int startOdometer,
                           Integer endOdometer, int pickupLocationId, Integer returnLocationId,
                           String leasingCode) {

        this.carId = carId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyPrice = monthlyPrice;
        this.kilometersIncluded = kilometersIncluded;
        this.startOdometer = startOdometer;
        this.endOdometer = endOdometer;
        this.pickupLocationId = pickupLocationId;
        this.returnLocationId = returnLocationId;
        this.leasingCode = leasingCode;
    }

    //GETTERS

    public int getRentalAgreementId() {
        return rentalAgreementId;
    }

    public int getCarId() {
        return carId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getMonthlyPrice() {
        return monthlyPrice;
    }

    public int getKilometersIncluded() {
        return kilometersIncluded;
    }

    public int getStartOdometer() {
        return startOdometer;
    }

    public Integer getEndOdometer() {
        return endOdometer;
    }

    public int getPickupLocationId() {
        return pickupLocationId;
    }

    public Integer getReturnLocationId() {
        return returnLocationId;
    }

    public String getLeasingCode() {
        return leasingCode;
    }

    //SETTERS

    public void setRentalAgreementId(int rentalAgreementId) {
        this.rentalAgreementId = rentalAgreementId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setMonthlyPrice(BigDecimal monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public void setKilometersIncluded(int kilometersIncluded) {
        this.kilometersIncluded = kilometersIncluded;
    }

    public void setStartOdometer(int startOdometer) {
        this.startOdometer = startOdometer;
    }

    public void setEndOdometer(Integer endOdometer) {
        this.endOdometer = endOdometer;
    }

    public void setPickupLocationId(int pickupLocationId) {
        this.pickupLocationId = pickupLocationId;
    }

    public void setReturnLocationId(Integer returnLocationId) {
        this.returnLocationId = returnLocationId;
    }

    public void setLeasingCode(String leasingCode) {
        this.leasingCode = leasingCode;
    }

    //TO STRING

    @Override
    public String toString() {
        return "RentalAgreement{" +
                "rentalAgreementId=" + rentalAgreementId +
                ", carId=" + carId +
                ", customerId=" + customerId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", monthlyPrice=" + monthlyPrice +
                ", kilometersIncluded=" + kilometersIncluded +
                ", startOdometer=" + startOdometer +
                ", endOdometer=" + endOdometer +
                ", pickupLocation=" + pickupLocationId +
                ", returnLocation=" + returnLocationId +
                ", leasingCode='" + leasingCode + '\'' +
                '}';
    }

}
