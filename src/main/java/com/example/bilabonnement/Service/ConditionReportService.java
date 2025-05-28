package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.ConditionReport;
import com.example.bilabonnement.Model.Damage;
import com.example.bilabonnement.Repository.ConditionReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import com.example.bilabonnement.Model.Car;
import com.example.bilabonnement.Model.Customer;
import com.example.bilabonnement.Model.RentalAgreement;

// Serviceklasse for forretningslogik vedrørende tilstandsrapporter
@Service
public class ConditionReportService {
    private final ConditionReportRepository conditionReportRepository;
    private final RentalAgreementService rentalAgreementService;
    private final CarService carService;
    private final CustomerService customerService;
    private final DamageService damageService;

    private final DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    public ConditionReportService(ConditionReportRepository conditionReportRepository,
                                  RentalAgreementService rentalAgreementService,
                                  CarService carService,
                                  CustomerService customerService,
                                  DamageService damageService) {
        this.conditionReportRepository = conditionReportRepository;
        this.rentalAgreementService = rentalAgreementService;
        this.carService = carService;
        this.customerService = customerService;
        this.damageService = damageService;
    }

    /**
     * Opretter en ny tilstandsrapport, håndterer dato og formatering.
     */
    public ConditionReport create(ConditionReport report) {
        if (report == null) {
            throw new IllegalArgumentException("ConditionReport object cannot be null.");
        }

        LocalDate reportDate = LocalDate.now();
        if (report.getFormattedDate() != null && !report.getFormattedDate().isEmpty()) {
            try {
                reportDate = LocalDate.parse(report.getFormattedDate(), displayFormatter);
            } catch (Exception e) {
                System.err.println("Error parsing date '" + report.getFormattedDate() + "'. Using default date: " + reportDate);
            }
        } else if (report.getReportDate() != null) {
            reportDate = report.getReportDate();
        }
        report.setReportDate(reportDate);

        // Formatér datoen for visning.
        formatReportDate(report);

        return conditionReportRepository.create(report);
    }

    /**
     * Finder en tilstandsrapport baseret på dens ID.
     */
    public ConditionReport findById(int id) {
        return conditionReportRepository.findById(id);
    }

    /**
     * Finder tilstandsrapporter tilknyttet en specifik lejeaftale.
     */
    public List<ConditionReport> findByRentalAgreementId(int rentalAgreementId) {
        return conditionReportRepository.findByRentalAgreementId(rentalAgreementId);
    }

    /**
     * Finder alle tilstandsrapporter.
     */
    public List<ConditionReport> findAll() {
        return conditionReportRepository.findAll();
    }

    /**
     * Opdaterer en eksisterende tilstandsrapport.
     */
    public int update(ConditionReport report) {
        if (report == null || report.getConditionReportId() <= 0) {
            throw new IllegalArgumentException("Valid ConditionReport object with ID is required for update.");
        }
        return conditionReportRepository.update(report);
    }

    /**
     * Sletter en tilstandsrapport baseret på dens ID.
     */
    public int delete(int id) {
        return conditionReportRepository.delete(id);
    }

    /**
     * Beregner den totale pris for en liste af skader.
     */
    public BigDecimal calculateTotalDamagePrice(List<Damage> damages) {
        if (damages == null || damages.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalDamagePrice = BigDecimal.ZERO;
        // Bruger for loop til at beregne total pris.
        for (Damage damage : damages) {
            if (damage != null && damage.getDamagePrice() != null) {
                totalDamagePrice = totalDamagePrice.add(damage.getDamagePrice());
            }
        }

        return totalDamagePrice;
    }

    /**
     * Henter en tilstandsrapport med alle tilknyttede detaljer.
     */
    public ConditionReport getConditionReportWithDetails(int conditionReportId) {
        // Finder selve rapporten og beriger den.
        ConditionReport report = findById(conditionReportId);
        if (report == null) {
            return null;
        }
        return enrichConditionReport(report);
    }

    /**
     * Beregner gennemsnitligt antal skader pr. lejeaftale.

    public double getAverageDamagesPerRental() {
        int totalDamages = damageService.countAllDamages();
        int totalRentals = rentalAgreementService.countAllRentalAgreements();
        if (totalRentals == 0) return 0.0;
        return (double) totalDamages / totalRentals;
    }

    /**
     * Henter det totale antal skader.

    public int getTotalDamages() {
        return damageService.countAllDamages();
    }

    /**
     * Henter det totale antal lejeaftaler.

    public int getTotalRentals() {
        return rentalAgreementService.countAllRentalAgreements();
    }

    /**
     * Beregner den gennemsnitlige pris pr. skade.
     */
    public double getAverageDamagePrice() {
        List<Damage> damages = damageService.findAll();
        if (damages == null || damages.isEmpty()) {
            return 0.0;
        }

        BigDecimal totalDamagePrice = BigDecimal.ZERO;
        int validDamageCount = 0;

        // Løber igennem skader, summerer priser og tæller gyldige.
        for (Damage damage : damages) {
            // Tjekker om skade og pris er gyldige.
            if (damage != null && damage.getDamagePrice() != null) {
                totalDamagePrice = totalDamagePrice.add(damage.getDamagePrice());
                validDamageCount++;
            }
        }

        // Returnerer 0.0 hvis der ikke var gyldige skader.
        if (validDamageCount == 0) {
            return 0.0;
        }

        // Beregner gennemsnittet af gyldige skade-priser.
        return totalDamagePrice.doubleValue() / validDamageCount;
    }

    /**
     * Finder og filtrerer tilstandsrapporter. Mindre effektivt for mange rapporter.
     */
    /**
     * Finder og filtrerer tilstandsrapporter baseret på forskellige kriterier.
     */
    public List<ConditionReport> findByFilters(String searchReportId, Integer customerId,
                                               Integer rentalAgreementId, LocalDate startDate,
                                               LocalDate endDate) {
        List<ConditionReport> allReports = findAll();
        if (allReports == null) {
            return new ArrayList<>();
        }

        List<ConditionReport> detailedReports = new ArrayList<>();
        for (ConditionReport report : allReports) {
            ConditionReport enrichedReport = enrichConditionReport(report);
            if (enrichedReport != null) {
                detailedReports.add(enrichedReport);
            }
        }

        List<ConditionReport> filteredReports = new ArrayList<>();
        for (ConditionReport report : detailedReports) {
            boolean matches = true;

            if (searchReportId != null && !searchReportId.isEmpty()) {
                if (!String.valueOf(report.getConditionReportId()).equals(searchReportId)) {
                    matches = false;
                }

            }

            if (matches && customerId != null) {
                if (report.getCustomer() == null || report.getCustomer().getCustomerId() != customerId.intValue()) {
                    matches = false;
                }


            }

            if (matches && rentalAgreementId != null) {
                if (report.getRentalAgreementId() == null || !report.getRentalAgreementId().equals(rentalAgreementId)) {
                    matches = false;
                }
            }

            if (matches && startDate != null) {
                if (report.getReportDate() == null || report.getReportDate().isBefore(startDate)) {
                    matches = false;
                }
            }
            if (matches && endDate != null) {
                if (report.getReportDate() == null || report.getReportDate().isAfter(endDate)) {
                    matches = false;
                }
            }

            if (matches) {
                filteredReports.add(report);
            }
        }

        return filteredReports;
    }



    /**
     * Formaterer rapportdatoen til en streng.
     */
    public void formatReportDate(ConditionReport report) {
        if (report != null && report.getReportDate() != null) {
            report.setFormattedDate(report.getReportDate().format(displayFormatter));
        } else if (report != null) {
            report.setFormattedDate("");
        }
    }

    /**
     * Formaterer datoen for alle rapporter i en liste.
     */
    public void formatReportDates(List<ConditionReport> reports) {
        if (reports == null) return;
        reports.forEach(this::formatReportDate);
    }

    /**
     * Beregner gennemsnitligt antal skader pr. tilstandsrapport.
     */
    public double getAverageDamagesPerReport() {
        int totalDamages = damageService.countAllDamages();
        int totalReports = findAll().size();
        if (totalReports == 0) return 0.0;
        return (double) totalDamages / totalReports;
    }

    /**
     * Henter afsluttede lejeaftaler til rapportoprettelse.
     */
    public List<RentalAgreement> getFinishedRentalAgreementsForReportCreation() {
        List<RentalAgreement> finishedAgreements = rentalAgreementService.findFinishedRentalAgreements();
        if (finishedAgreements == null) {
            return new ArrayList<>();
        }
        // Filtrer aftaler fra, der allerede har en tilstandsrapport
        return finishedAgreements.stream()
                .filter(agreement -> {
                    List<ConditionReport> reports = conditionReportRepository.findByRentalAgreementId(agreement.getRentalAgreementId());
                    return reports == null || reports.isEmpty();
                })
                .collect(Collectors.toList());
    }

    private ConditionReport enrichConditionReport(ConditionReport report) {
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

        List<Damage> damages = damageService.findByConditionReportId(report.getConditionReportId());
        report.setDamages(damages);

        report.setTotalPrice(calculateTotalDamagePrice(damages));

        return report;
    }
}
