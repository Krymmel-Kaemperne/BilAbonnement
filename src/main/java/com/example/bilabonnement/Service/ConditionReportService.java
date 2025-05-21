package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.ConditionReport;
import com.example.bilabonnement.Model.Damage; 
import com.example.bilabonnement.Repository.ConditionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

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

    private final DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ConditionReport create(ConditionReport report) {
        // Set date to today if not provided
        if (report.getReportDate() == null) {
            report.setReportDate(LocalDate.now());
        }

        // Parse the date in dd-MM-yyyy format if it's a string
        if (report.getFormattedDate() != null && !report.getFormattedDate().isEmpty()) {
            try {
                report.setReportDate(LocalDate.parse(report.getFormattedDate(), displayFormatter));
            } catch (Exception e) {
                // If parsing fails, keep the existing date or today's date
                if (report.getReportDate() == null) {
                    report.setReportDate(LocalDate.now());
                }
            }
        }

        // Format the date for display
        formatReportDate(report);

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
        
        // Always recalculate total price based on current damages
        if (damages != null && !damages.isEmpty()) {
            report.setTotalPrice(calculateTotalDamagePrice(damages));
        } else {
            report.setTotalPrice(BigDecimal.ZERO);
        }

        return report;
    }

    public double getAverageDamagesPerRental() {
        int totalDamages = damageService.countAllDamages();
        int totalRentals = rentalAgreementService.countAllRentalAgreements();
        if (totalRentals == 0) return 0.0;
        return (double) totalDamages / totalRentals;
    }

    public int getTotalDamages() {
        return damageService.countAllDamages();
    }

    public int getTotalRentals() {
        return rentalAgreementService.countAllRentalAgreements();
    }

    public double getAverageDamagePrice() {
        List<Damage> damages = damageService.findAll();
        if (damages.isEmpty()) return 0.0;
        double total = damages.stream()
            .filter(d -> d.getDamagePrice() != null)
            .mapToDouble(d -> d.getDamagePrice().doubleValue())
            .sum();
        return total / damages.size();
    }

    public List<ConditionReport> findByFilters(String searchReportId, Integer customerId, 
                                             Integer rentalAgreementId, LocalDate startDate, 
                                             LocalDate endDate) {
        List<ConditionReport> allReports = findAll();
        List<ConditionReport> filteredReports = new ArrayList<>();

        for (ConditionReport basicReport : allReports) {
            ConditionReport detailedReport = getConditionReportWithDetails(basicReport.getConditionReportId());
            if (detailedReport == null) continue;

            // Apply filters
            boolean includeReport = true;

            // Search by report ID
            if (searchReportId != null && !searchReportId.isEmpty()) {
                includeReport = String.valueOf(detailedReport.getConditionReportId()).equals(searchReportId);
            }

            // Filter by customer
            if (includeReport && customerId != null && detailedReport.getCustomer() != null) {
                includeReport = detailedReport.getCustomer().getCustomerId() == customerId;
            }

            // Filter by rental agreement
            if (includeReport && rentalAgreementId != null) {
                includeReport = detailedReport.getRentalAgreementId() != null && 
                              detailedReport.getRentalAgreementId().equals(rentalAgreementId);
            }

            // Filter by date range
            if (includeReport && startDate != null && detailedReport.getReportDate() != null) {
                includeReport = !detailedReport.getReportDate().isBefore(startDate);
            }
            if (includeReport && endDate != null && detailedReport.getReportDate() != null) {
                includeReport = !detailedReport.getReportDate().isAfter(endDate);
            }

            if (includeReport) {
                filteredReports.add(detailedReport);
            }
        }

        return filteredReports;
    }

    // Format a single report's date
    public void formatReportDate(ConditionReport report) {
        if (report != null && report.getReportDate() != null) {
            report.setFormattedDate(report.getReportDate().format(displayFormatter));
        }
    }

    // Format dates for a list of reports
    public void formatReportDates(List<ConditionReport> reports) {
        if (reports == null) return;
        
        for (ConditionReport report : reports) {
            formatReportDate(report);
        }
    }

    // Beregn gennemsnitlige antal skader pr. tilstandsrapport
    public double getAverageDamagesPerReport() {
        int totalDamages = damageService.countAllDamages();
        int totalReports = findAll().size();
        if (totalReports == 0) return 0.0;
        return (double) totalDamages / totalReports;
    }

    // Get finished rental agreements for condition report creation
    public List<RentalAgreement> getFinishedRentalAgreementsForReportCreation() {
        return rentalAgreementService.findFinishedRentalAgreements();
    }
} 