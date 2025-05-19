package com.example.bilabonnement.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Modelklasse for tilstandsrapporter (condition reports)
public class ConditionReport {
    private int conditionReportId;
    private Integer rentalAgreementId;
    private String conditionNotes;
    private LocalDate reportDate;
    private BigDecimal totalPrice;

    // Transient fields for additional details - not persisted in condition_report table directly
    private transient Car car;
    private transient Customer customer;
    private transient RentalAgreement rentalAgreement;
    private transient java.util.List<Damage> damages; // To also hold associated damages

    public int getConditionReportId() { return conditionReportId; }
    public void setConditionReportId(int conditionReportId) { this.conditionReportId = conditionReportId; }

    public Integer getRentalAgreementId() { return rentalAgreementId; }
    public void setRentalAgreementId(Integer rentalAgreementId) { this.rentalAgreementId = rentalAgreementId; }

    public String getConditionNotes() { return conditionNotes; }
    public void setConditionNotes(String conditionNotes) { this.conditionNotes = conditionNotes; }

    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    // Getters and Setters for transient fields
    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public RentalAgreement getRentalAgreement() { return rentalAgreement; }
    public void setRentalAgreement(RentalAgreement rentalAgreement) { this.rentalAgreement = rentalAgreement; }

    public java.util.List<Damage> getDamages() { return damages; }
    public void setDamages(java.util.List<Damage> damages) { this.damages = damages; }
} 