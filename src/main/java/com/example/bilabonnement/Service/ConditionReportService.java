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
        // Bruger Stream til at beregne totalen.
        return damages.stream()
                .filter(damage -> damage != null && damage.getDamagePrice() != null)
                .map(Damage::getDamagePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
     */
    public double getAverageDamagesPerRental() {
        int totalDamages = damageService.countAllDamages();
        int totalRentals = rentalAgreementService.countAllRentalAgreements();
        if (totalRentals == 0) return 0.0;
        return (double) totalDamages / totalRentals;
    }

    /**
     * Henter det totale antal skader.
     */
    public int getTotalDamages() {
        return damageService.countAllDamages();
    }

    /**
     * Henter det totale antal lejeaftaler.
     */
    public int getTotalRentals() {
        return rentalAgreementService.countAllRentalAgreements();
    }

    /**
     * Beregner den gennemsnitlige pris pr. skade.
     */
    public double getAverageDamagePrice() {
        List<Damage> damages = damageService.findAll();
        if (damages == null || damages.isEmpty()) return 0.0;

        // Bruger Java Streams til at filtrere null priser og beregne gennemsnit.
        List<BigDecimal> validPrices = damages.stream()
                .filter(d -> d != null && d.getDamagePrice() != null)
                .map(Damage::getDamagePrice)
                .collect(Collectors.toList());

        if (validPrices.isEmpty()) return 0.0;

        BigDecimal total = validPrices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return total.doubleValue() / validPrices.size();
    }

    /**
     * Finder og filtrerer tilstandsrapporter. Mindre effektivt for mange rapporter.
     */
    public List<ConditionReport> findByFilters(String searchReportId, Integer customerId,
                                               Integer rentalAgreementId, LocalDate startDate,
                                               LocalDate endDate) {
        List<ConditionReport> allReports = findAll();
        if (allReports == null) {
            return new ArrayList<>();
        }

        List<ConditionReport> detailedReports = allReports.stream()
                .map(report -> enrichConditionReport(report))
                .filter(report -> report != null)
                .collect(Collectors.toList());

        // Filtrer de berigede rapporter ved hjælp af Streams.
        return detailedReports.stream()
                .filter(report -> {
                    boolean matches = true;

                    // Filtrer efter rapport ID.
                    if (searchReportId != null && !searchReportId.isEmpty()) {
                        matches = String.valueOf(report.getConditionReportId()).equals(searchReportId);
                    }

                    // Filtrer efter kunde ID.
                    if (matches && customerId != null) {
                        matches = report.getCustomer() != null && report.getCustomer().getCustomerId() == customerId;
                    }

                    // Filtrer efter lejeaftale ID.
                    if (matches && rentalAgreementId != null) {
                        matches = report.getRentalAgreementId() != null &&
                                report.getRentalAgreementId().equals(rentalAgreementId);
                    }

                    // Filtrer efter datointerval.
                    if (matches && startDate != null) {
                        matches = report.getReportDate() != null && !report.getReportDate().isBefore(startDate);
                    }
                    if (matches && endDate != null) {
                        matches = report.getReportDate() != null && !report.getReportDate().isAfter(endDate);
                    }

                    return matches;
                })
                .collect(Collectors.toList());
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
