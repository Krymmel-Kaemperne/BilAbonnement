package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Car;
import com.example.bilabonnement.Model.RentalAgreement;
import com.example.bilabonnement.Repository.CarRepository;
import com.example.bilabonnement.Repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils; // Bruges til at tjekke om strenge har indhold

import java.time.LocalDate;
import java.util.List;

@Service
public class CarService {



    @Autowired
    RentalAgreementRepository rentalAgreementRepository;


    private CarRepository carRepository;



    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Finder alle biler i systemet.
     */
    public List<Car> findAllCars() {
        return carRepository.findAll();
    }


    /**
     * Finder en specifik bil ud fra dens ID.
     */
    public Car findById(int carId) {
        return carRepository.findById(carId);
    }


    /**
     * Opretter en ny bil efter validering.
     */
    public Car create(Car car) {
        // Validerer input Car objektet og dets felter.
        if (car == null) {
            throw new IllegalArgumentException("Car object cannot be null.");
        }
        if (!StringUtils.hasText(car.getRegistrationNumber())) {
            throw new IllegalArgumentException("Registreringsnummer er påkrævet.");
        }
        if (car.getModelId() == null || car.getModelId() <= 0) {
            // for at sikre, at modellen rent faktisk eksisterer.
            throw new IllegalArgumentException("Et gyldigt Model ID er påkrævet.");
        }
        if (car.getCarStatusId() == null || car.getCarStatusId() <= 0) {
            throw new IllegalArgumentException("Et gyldigt Status ID er påkrævet.");
        }
        if (car.getFuelTypeId() == null || car.getFuelTypeId() <= 0) {
            throw new IllegalArgumentException("Et gyldigt Brændstoftype ID er påkrævet.");
        }
        if (car.getTransmissionTypeId() == null || car.getTransmissionTypeId() <= 0) {
            throw new IllegalArgumentException("Et gyldigt Transmissionstype ID er påkrævet.");
        }

        return carRepository.create(car);
    }

    /*
     * Opdaterer en eksisterende bil efter validering.
     */
    public Car update(Car car) {
        if (car == null || car.getCarId() <= 0) {
            throw new IllegalArgumentException("Et gyldigt Car objekt med ID er påkrævet for opdatering.");
        }

        Car existingCar = carRepository.findById(car.getCarId());
        if (existingCar == null) {
            throw new IllegalArgumentException("Bil med ID " + car.getCarId() + " blev ikke fundet og kan ikke opdateres.");
        }

        return carRepository.update(car);
    }

    /**
     * Finder biler baseret på de angivne filterkriterier.
     */
    public List<Car> findCarsByFilters(Integer brand, Integer status, Integer model, Integer fuelType, Integer TransmissionType) {
        return carRepository.findByFilters(brand, status, model, fuelType, TransmissionType);
    }



    /**
     * Tjekker om en bil aktuelt er udlejet.
     * Sammenligner dagens dato med slutdatoen for lejeaftaler tilknyttet bilen.
     */
    public boolean isCarCurrentlyRentedOut(int carId) {
        List<RentalAgreement> agreements = rentalAgreementRepository.findAgreementsByCarId(carId);
        LocalDate today = LocalDate.now();

        System.out.println("Car ID: " + carId);
        System.out.println("Found " + agreements.size() + " rental agreements");

        for (RentalAgreement ag : agreements) {
            System.out.println("Start: " + ag.getStartDate() + ", End: " + ag.getEndDate());
        }

        boolean result = agreements.stream().anyMatch(agreement ->
                !agreement.getEndDate().isBefore(today)
        );

        System.out.println("Rented out? " + result);

        return result;
    }
}
