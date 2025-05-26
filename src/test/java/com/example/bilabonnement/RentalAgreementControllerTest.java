package com.example.bilabonnement;

import com.example.bilabonnement.Controller.RentalAgreementController;
import com.example.bilabonnement.Model.Car;
import com.example.bilabonnement.Model.RentalAgreement;
import com.example.bilabonnement.Service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RentalAgreementControllerTest {

    private RentalAgreementController rentalController;
    private RentalAgreementService rentalAgreementService;
    private CarService carService;
    private CustomerService customerService;
    private LocationService locationService;
    private ConditionReportService conditionReportService;



    @BeforeEach
    void setUp()
    {
        rentalAgreementService = mock(RentalAgreementService.class);
        carService = mock(CarService.class);
        customerService = mock(CustomerService.class);
        locationService = mock(LocationService.class);
        conditionReportService = mock(ConditionReportService.class);

        rentalController = new RentalAgreementController(
                rentalAgreementService,
                carService,
                customerService,
                locationService,
                conditionReportService
        );

    }

    @Test
    @DisplayName("Skal ikke oprette lejeaftale hvis bilen ikke er tilgængelig")
    void createRentalAgreementFailsIfCarNotAvailable()
    {
    Car car = new Car();
        car.setCarId(1);
        car.setCarStatusId(2);
        car.setCarStatusName("Udlejet");

        when(carService.findById(1)).thenReturn(car);

        RentalAgreement agreement = new RentalAgreement();
        agreement.setCarId(1);
        agreement.setStartDate(LocalDate.now());
        agreement.setEndDate(LocalDate.now().plusMonths(3));

        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes =  mock(RedirectAttributes.class);

        String viewName = rentalController.createRentalAgreement(agreement, model, redirectAttributes);

        verify(rentalAgreementService, never()).create(any());
        verify(carService, times(1)).findById(1);
        assertEquals("redirect:/dataRegistration/rental-agreements", viewName);

        System.out.println("✅ Testen lykkedes: Bilen kunne ikke udlejes, da den ikke er tilgængelig");
    }
}
