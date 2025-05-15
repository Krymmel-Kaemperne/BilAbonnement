package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.ConditionReport;
import com.example.bilabonnement.Repository.ConditionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Serviceklasse for forretningslogik vedrørende tilstandsrapporter
@Service
public class ConditionReportService {
    @Autowired
    private ConditionReportRepository conditionReportRepository;

    public ConditionReport create(ConditionReport report) {
        return conditionReportRepository.create(report);
    }

    public ConditionReport findById(int id) {
        return conditionReportRepository.findById(id);
    }

    public List<ConditionReport> findByRentalAgreementId(int rentalAgreementId) {
        return conditionReportRepository.findByRentalAgreementId(rentalAgreementId);
    }

    public List<ConditionReport> findAll() {
        return conditionReportRepository.findAll();
    }

    public int update(ConditionReport report) {
        return conditionReportRepository.update(report);
    }

    public int delete(int id) {
        return conditionReportRepository.delete(id);
    }
} 