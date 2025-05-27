package com.example.bilabonnement.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// Modelklasse for tilstandsrapporter (condition reports)
public class ConditionReport {
    private int conditionReportId;
    private Integer rentalAgreementId;
    private String conditionNotes;
    private LocalDate reportDate;
    private BigDecimal totalPrice;
    private List<Damage> damages;
    private String formattedDate; // For displaying formatted date

    private transient Car car;
    private transient Customer customer;
    private transient RentalAgreement rentalAgreement;

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

    public List<Damage> getDamages() { return damages; }
    public void setDamages(List<Damage> damages) { this.damages = damages; }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    // Getters and Setters for transient fields
    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public RentalAgreement getRentalAgreement() { return rentalAgreement; }
    public void setRentalAgreement(RentalAgreement rentalAgreement) { this.rentalAgreement = rentalAgreement; }
} 