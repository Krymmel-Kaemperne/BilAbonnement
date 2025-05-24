package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Car;
// import com.example.bilabonnement.Repository.CarRepository; // Keep for context, but it won't be mocked
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith; // Mockito
// import org.mockito.InjectMocks; // Mockito
// import org.mockito.Mock; // Mockito
// import org.mockito.junit.jupiter.MockitoExtension; // Mockito

import static org.junit.jupiter.api.Assertions.*;

class CarServiceTest {


    private CarService carService;

    private Car testCar;

    @BeforeEach
    void setUp() {

        carService = new CarService(null, null); // Pass null for CarRepository for now

        testCar = new Car();
        testCar.setCarId(1);
        testCar.setRegistrationNumber("AB12345");
        testCar.setChassisNumber("CHASSIS123");
        testCar.setSteelPrice(200000.00);
        testCar.setColor("Red");
        testCar.setCo2Emission(120.5);
        testCar.setVehicleNumber("VEHICLE123");
        testCar.setModelId(1);
        testCar.setCarStatusId(1);
        testCar.setFuelTypeId(1);
        testCar.setTransmissionTypeId(1);
        testCar.setCurrentOdometer(15000);
        testCar.setIrkCode("IRK123");
    }



    @Test
    void createCar_exceptionFlow_nullRegistrationNumber() {

        testCar.setRegistrationNumber(null);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.create(testCar);
        });

        assertEquals("Registreringsnummer er påkrævet.", exception.getMessage());
    }

    @Test
    void createCar_exceptionFlow_nullCarObject() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.create(null);
        });
        assertEquals("Car object cannot be null.", exception.getMessage());
    }
    
    @Test
    void createCar_exceptionFlow_invalidModelId() {

        testCar.setModelId(0); // Invalid ID


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.create(testCar);
        });
        assertEquals("Et gyldigt Model ID er påkrævet.", exception.getMessage());
    }

    @Test
    void createCar_exceptionFlow_invalidCarStatusId() {
        testCar.setCarStatusId(0);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.create(testCar);
        });
        assertEquals("Et gyldigt Status ID er påkrævet.", exception.getMessage());
    }

    @Test
    void createCar_exceptionFlow_invalidFuelTypeId() {
        testCar.setFuelTypeId(0);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.create(testCar);
        });
        assertEquals("Et gyldigt Brændstoftype ID er påkrævet.", exception.getMessage());
    }

    @Test
    void createCar_exceptionFlow_invalidTransmissionTypeId() {
        testCar.setTransmissionTypeId(0);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.create(testCar);
        });
        assertEquals("Et gyldigt Transmissionstype ID er påkrævet.", exception.getMessage());
    }



    @Test
    void updateCar_exceptionFlow_nullCarObject() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.update(null);
        });
        assertEquals("Et gyldigt Car objekt med ID er påkrævet for opdatering.", exception.getMessage());
    }

    @Test
    void updateCar_exceptionFlow_invalidCarId() {
        testCar.setCarId(0); // Invalid ID
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            carService.update(testCar);
        });
        assertEquals("Et gyldigt Car objekt med ID er påkrævet for opdatering.", exception.getMessage());
    }
} 