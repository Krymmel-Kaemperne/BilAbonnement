package com.example.bilabonnement.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentalAgreement {
    private int rentalAgreementId;
    private Integer carId;
    private Integer customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal monthlyPrice;
    private int kilometersIncluded;
    private int startOdometer;
    private Integer endOdometer;
    private int pickupLocation;
    private Integer returnLocation;
    private String leasingCode;

    // DEFAUlT CONSTUCTOR
    public RentalAgreement() {
    }

    //CONSTRUCTOR WITHOUT ID
    public RentalAgreement(int rentalAgreementId, Integer carId, Integer customerId,
                           LocalDate startDate, LocalDate endDate, BigDecimal monthlyPrice,
                           int kilometersIncluded, int startOdometer, Integer endOdometer,
                           int pickupLocation, Integer returnLocation, String leasingCode) {
        this.rentalAgreementId = rentalAgreementId;
        this.carId = carId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyPrice = monthlyPrice;
        this.kilometersIncluded = kilometersIncluded;
        this.startOdometer = startOdometer;
        this.endOdometer = endOdometer;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.leasingCode = leasingCode;
    }

    //CONSTRUCTOR WITH ID
    public RentalAgreement(Integer carId, Integer customerId, LocalDate startDate, LocalDate endDate,
                           BigDecimal monthlyPrice, int kilometersIncluded, int startOdometer,
                           Integer endOdometer, int pickupLocation, Integer returnLocation,
                           String leasingCode) {

        this.carId = carId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.monthlyPrice = monthlyPrice;
        this.kilometersIncluded = kilometersIncluded;
        this.startOdometer = startOdometer;
        this.endOdometer = endOdometer;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.leasingCode = leasingCode;
    }

    //GETTERS

    public int getRentalAgreementId() {
        return rentalAgreementId;
    }

    public Integer getCarId() {
        return carId;
    }

    public Integer getCustomerId() {
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

    public int getPickupLocation() {
        return pickupLocation;
    }

    public Integer getReturnLocation() {
        return returnLocation;
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

    public void setPickupLocation(int pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public void setReturnLocation(Integer returnLocation) {
        this.returnLocation = returnLocation;
    }

    public void setLeasingCode(String leasingCode) {
        this.leasingCode = leasingCode;
    }

    // END DATE VALIDATION

    public boolean isEndDateValid()
    {
        return startDate != null && endDate != null && endDate.isAfter(startDate.plusMonths(3).minusDays(1));
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
                ", pickupLocation=" + pickupLocation +
                ", returnLocation=" + returnLocation +
                ", leasingCode='" + leasingCode + '\'' +
                '}';
    }

}
