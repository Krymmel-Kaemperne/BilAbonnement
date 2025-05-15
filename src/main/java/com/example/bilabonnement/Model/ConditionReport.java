package com.example.bilabonnement.Model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Modelklasse for tilstandsrapporter (condition reports)
public class ConditionReport {
    private int conditionReportId;
    private int rentalAgreementId;
    private String conditionNotes;
    private LocalDate reportDate;
    private BigDecimal totalPrice;

    public int getConditionReportId() { return conditionReportId; }
    public void setConditionReportId(int conditionReportId) { this.conditionReportId = conditionReportId; }

    public int getRentalAgreementId() { return rentalAgreementId; }
    public void setRentalAgreementId(int rentalAgreementId) { this.rentalAgreementId = rentalAgreementId; }

    public String getConditionNotes() { return conditionNotes; }
    public void setConditionNotes(String conditionNotes) { this.conditionNotes = conditionNotes; }

    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
} 