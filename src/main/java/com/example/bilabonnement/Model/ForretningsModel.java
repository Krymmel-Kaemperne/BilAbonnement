package com.example.bilabonnement.Model;

import java.math.BigDecimal;

public class ForretningsModel {
    private int activeRentals;
    private BigDecimal currentRevenue;
    private BigDecimal totalRevenue;
    private BigDecimal averageRentalIncome;
    private int totalCars;
    private int availableCars;
    private int carsWithDamages;
    private int totalCustomers;
    private int newRentalsThisMonth;
    private int completedRentalAgreements;
    private BigDecimal avgRentalPeriodMonths;
    private BigDecimal currentMonthRevenue;
    private BigDecimal avgRevenuePerCustomer;

    public ForretningsModel(int activeRentals, BigDecimal currentRevenue, BigDecimal totalRevenue, BigDecimal averageRentalIncome,
                            int totalCars, int availableCars, int carsWithDamages, int totalCustomers, int newRentalsThisMonth,
                            int completedRentalAgreements, BigDecimal avgRentalPeriodMonths, BigDecimal currentMonthRevenue, BigDecimal avgRevenuePerCustomer) {
        this.activeRentals = activeRentals;
        this.currentRevenue = currentRevenue;
        this.totalRevenue = totalRevenue;
        this.averageRentalIncome = averageRentalIncome;
        this.totalCars = totalCars;
        this.availableCars = availableCars;
        this.carsWithDamages = carsWithDamages;
        this.totalCustomers = totalCustomers;
        this.newRentalsThisMonth = newRentalsThisMonth;
        this.completedRentalAgreements = completedRentalAgreements;
        this.avgRentalPeriodMonths = avgRentalPeriodMonths;
        this.currentMonthRevenue = currentMonthRevenue;
        this.avgRevenuePerCustomer = avgRevenuePerCustomer;
    }

    public int getActiveRentals() {
        return activeRentals;
    }

    public void setActiveRentals(int activeRentals) {
        this.activeRentals = activeRentals;
    }

    public BigDecimal getCurrentRevenue() {
        return currentRevenue;
    }

    public void setCurrentRevenue(BigDecimal currentRevenue) {
        this.currentRevenue = currentRevenue;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getAverageRentalIncome() {
        return averageRentalIncome;
    }

    public void setAverageRentalIncome(BigDecimal averageRentalIncome) {
        this.averageRentalIncome = averageRentalIncome;
    }

    public int getTotalCars() {
        return totalCars;
    }

    public void setTotalCars(int totalCars) {
        this.totalCars = totalCars;
    }

    public int getAvailableCars() {
        return availableCars;
    }

    public void setAvailableCars(int availableCars) {
        this.availableCars = availableCars;
    }

    public int getCarsWithDamages() {
        return carsWithDamages;
    }

    public void setCarsWithDamages(int carsWithDamages) {
        this.carsWithDamages = carsWithDamages;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public int getNewRentalsThisMonth() {
        return newRentalsThisMonth;
    }

    public void setNewRentalsThisMonth(int newRentalsThisMonth) {
        this.newRentalsThisMonth = newRentalsThisMonth;
    }

    public int getCompletedRentalAgreements() {
        return completedRentalAgreements;
    }

    public void setCompletedRentalAgreements(int completedRentalAgreements) {
        this.completedRentalAgreements = completedRentalAgreements;
    }

    public BigDecimal getAvgRentalPeriodMonths() {
        return avgRentalPeriodMonths;
    }

    public void setAvgRentalPeriodMonths(BigDecimal avgRentalPeriodMonths) {
        this.avgRentalPeriodMonths = avgRentalPeriodMonths;
    }

    public BigDecimal getCurrentMonthRevenue() {
        return currentMonthRevenue;
    }

    public void setCurrentMonthRevenue(BigDecimal currentMonthRevenue) {
        this.currentMonthRevenue = currentMonthRevenue;
    }

    public BigDecimal getAvgRevenuePerCustomer() {
        return avgRevenuePerCustomer;
    }

    public void setAvgRevenuePerCustomer(BigDecimal avgRevenuePerCustomer) {
        this.avgRevenuePerCustomer = avgRevenuePerCustomer;
    }
} 