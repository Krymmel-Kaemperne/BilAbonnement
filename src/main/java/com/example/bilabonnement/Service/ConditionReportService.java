package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.ConditionReport;
import com.example.bilabonnement.Model.Damage; 
import com.example.bilabonnement.Repository.ConditionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import com.example.bilabonnement.Model.Car;
import com.example.bilabonnement.Model.Customer;
import com.example.bilabonnement.Model.RentalAgreement;
import com.example.bilabonnement.Service.RentalAgreementService;
import com.example.bilabonnement.Service.CarService;
import com.example.bilabonnement.Service.CustomerService;
import com.example.bilabonnement.Service.DamageService;

// Serviceklasse for forretningslogik vedrørende tilstandsrapporter
@Service
public class ConditionReportService {
    @Autowired
    private ConditionReportRepository conditionReportRepository;

    @Autowired
    private RentalAgreementService rentalAgreementService;
    @Autowired
    private CarService carService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DamageService damageService;

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

    // Beregn totalpris for alle skader tilknyttet en tilstandsrapport
    public BigDecimal calculateTotalDamagePrice(List<Damage> damages) {
        BigDecimal total =  BigDecimal.ZERO;
        for (Damage damage : damages) {
            if (damage.getDamagePrice() != null) {
                total = total.add(damage.getDamagePrice());
            }
        }
        return total;
    }

    /**
     * Finds a ConditionReport by its ID and populates it with related details
     * like RentalAgreement, Car, Customer, and its list of Damages.
     *
     * @param conditionReportId The ID of the condition report to find.
     * @return A ConditionReport object with populated transient fields, or null if not found.
     */
    public ConditionReport getConditionReportWithDetails(int conditionReportId) {
        ConditionReport report = conditionReportRepository.findById(conditionReportId);
        if (report == null) {
            return null;
        }

        if (report.getRentalAgreementId() != null) {
            RentalAgreement agreement = rentalAgreementService.findById(report.getRentalAgreementId());
            report.setRentalAgreement(agreement);

            if (agreement != null) {
                Car car = carService.findById(agreement.getCarId());
                report.setCar(car);

                Customer customer = customerService.findById(agreement.getCustomerId());
                report.setCustomer(customer);
            }
        }

        // Populate damages associated with this report
        List<Damage> damages = damageService.findByConditionReportId(conditionReportId);
        report.setDamages(damages);
        
        // Recalculate total price based on fetched damages, if not already set or to ensure consistency
        if (report.getTotalPrice() == null || report.getTotalPrice().compareTo(BigDecimal.ZERO) == 0) {
             if (damages != null && !damages.isEmpty()) {
                 report.setTotalPrice(calculateTotalDamagePrice(damages));
             }
        }

        return report;
    }
} 