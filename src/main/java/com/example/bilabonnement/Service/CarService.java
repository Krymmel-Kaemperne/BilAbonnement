package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Car;
import com.example.bilabonnement.Repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils; // Bruges til at tjekke om strenge har indhold

import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;


    public List<Car> findAllCars() {
        return carRepository.findAll();
    }


    // Finder en specifik bil ud fra dens ID.
    public Car findById(int carId) {
        // Her kunne man tilføje logik, f.eks. hvis bilen ikke findes,
        // kunne man kaste en custom exception i stedet for bare at returnere null.
        return carRepository.findById(carId);
    }


    public Car create(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car object cannot be null.");
        }
        // Simpel validering - dette bør udvides baseret på forretningsregler
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
        // Yderligere validering kan omfatte:
        // - Tjek at stålpris er positiv
        // - Tjek format på registreringsnummer, chassisnummer etc.
        // - Tjek at de angivne ID'er (modelId, carStatusId etc.) rent faktisk findes i deres respektive tabeller.

        return carRepository.create(car);
    }

    /*
     * Opdaterer en eksisterende bil.
     * @param car Bilen med opdaterede data (skal have et gyldigt carId).
     * @return Den opdaterede bil.
     * @throws IllegalArgumentException hvis carId er ugyldigt eller car objektet er null.
     */
    public Car update(Car car) {
        if (car == null || car.getCarId() <= 0) {
            throw new IllegalArgumentException("Et gyldigt Car objekt med ID er påkrævet for opdatering.");
        }
        // Tilføj lignende validering som i create, hvis nødvendigt,
        // f.eks. at de nye ID'er (modelId, statusId etc.) er gyldige.

        // Tjek om bilen overhovedet eksisterer før opdatering (valgfrit, men god praksis)
        Car existingCar = carRepository.findById(car.getCarId());
        if (existingCar == null) {
            throw new IllegalArgumentException("Bil med ID " + car.getCarId() + " blev ikke fundet og kan ikke opdateres.");
        }

        return carRepository.update(car);
    }

    public List<Car> findCarsByFilters(Integer brand, Integer status, Integer model, Integer fuelType, Integer transmissionType) {
        return carRepository.findByFilters(brand, status, model, fuelType, transmissionType);
    }
}

