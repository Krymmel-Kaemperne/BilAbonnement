package com.example.bilabonnement;

import com.example.bilabonnement.Model.Damage;
import com.example.bilabonnement.Repository.ConditionReportRepository;
import com.example.bilabonnement.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConditionReportServiceTest {

    private ConditionReportService conditionReportService;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        ConditionReportRepository conditionReportRepository = Mockito.mock(ConditionReportRepository.class);
        RentalAgreementService rentalAgreementService = Mockito.mock(RentalAgreementService.class);
        CarService carService = Mockito.mock(CarService.class);
        CustomerService customerService = Mockito.mock(CustomerService.class);
        DamageService damageService = Mockito.mock(DamageService.class);
        // Pass mocks to service
        conditionReportService = new ConditionReportService(
                conditionReportRepository,
                rentalAgreementService,
                carService,
                customerService,
                damageService
        );
    }

    @Test
    @DisplayName("Test af calculateTotalDamagePrice med tom liste")
    void calculateTotalDamagePrice_emptyList_returnsZero() {
        List<Damage> damages = new ArrayList<>();
        BigDecimal expectedTotal = BigDecimal.ZERO;

        BigDecimal actualTotal = conditionReportService.calculateTotalDamagePrice(damages);

        assertEquals(expectedTotal, actualTotal, "Den samlede pris for en tom liste skal være nul.");
    }

    @Test
    @DisplayName("Test af calculateTotalDamagePrice med skader med gyldige priser")
    void calculateTotalDamagePrice_withValidPrices_returnsCorrectTotal() {
        List<Damage> damages = new ArrayList<>();
        damages.add(new Damage(1, "Skade 1", new BigDecimal("100.50")));
        damages.add(new Damage(2, "Skade 2", new BigDecimal("250.00")));
        damages.add(new Damage(3, "Skade 3", new BigDecimal("50.25")));

        BigDecimal expectedTotal = new BigDecimal("400.75");

        BigDecimal actualTotal = conditionReportService.calculateTotalDamagePrice(damages);

        assertEquals(expectedTotal, actualTotal, "Den samlede pris skal være summen af gyldige priser.");
    }
}
