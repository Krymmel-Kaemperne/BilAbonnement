package com.example.bilabonnement.Model;

import java.math.BigDecimal;

// Modelklasse for skader tilknyttet tilstandsrapporter
public class Damage {
    private int damageId;
    private int conditionReportId;
    private String damageDescription;
    private BigDecimal damagePrice;

    public int getDamageId() { return damageId; }
    public void setDamageId(int damageId) { this.damageId = damageId; }

    public int getConditionReportId() { return conditionReportId; }
    public void setConditionReportId(int conditionReportId) { this.conditionReportId = conditionReportId; }

    public String getDamageDescription() { return damageDescription; }
    public void setDamageDescription(String damageDescription) { this.damageDescription = damageDescription; }

    public BigDecimal getDamagePrice() { return damagePrice; }
    public void setDamagePrice(BigDecimal damagePrice) { this.damagePrice = damagePrice; }
} 