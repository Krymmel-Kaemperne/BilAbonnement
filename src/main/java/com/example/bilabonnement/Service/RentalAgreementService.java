package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.RentalAgreement;
import com.example.bilabonnement.Repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RentalAgreementService {

    @Autowired
    RentalAgreementRepository rentalAgreementRepository;

    public RentalAgreementService (RentalAgreementRepository rentalAgreementRepository) {
        this.rentalAgreementRepository = rentalAgreementRepository;
    }

    public void create(RentalAgreement rentalAgreement) {
        rentalAgreementRepository.create(rentalAgreement);
    }

    public List<RentalAgreement> findAll() {
        return rentalAgreementRepository.findAll();
    }

    public RentalAgreement findById(int id) {
        return rentalAgreementRepository.findById(id);
    }

    public List<RentalAgreement> findFinishedRentalAgreements() {
        return rentalAgreementRepository.findFinishedRentalAgreements();
    }

    public void update(RentalAgreement rentalAgreement) { // Parameter fra formen
        RentalAgreement existingAgreement = rentalAgreementRepository.findById(rentalAgreement.getRentalAgreementId());
        if (existingAgreement == null) {
            throw new IllegalArgumentException("Lejeaftale med ID " + rentalAgreement.getRentalAgreementId() + " ikke fundet for opdatering.");
        }


        if (rentalAgreement.getCarId() != null) {
            existingAgreement.setCarId(rentalAgreement.getCarId());
        }
        if (rentalAgreement.getCustomerId() != null) {
            existingAgreement.setCustomerId(rentalAgreement.getCustomerId());
        }

        if (rentalAgreement.getMonthlyPrice() != null) {
            existingAgreement.setMonthlyPrice(rentalAgreement.getMonthlyPrice());
        }
        existingAgreement.setKilometersIncluded(rentalAgreement.getKilometersIncluded());

        if (rentalAgreement.getPickupLocationId() != null) {
            existingAgreement.setPickupLocationId(rentalAgreement.getPickupLocationId());
        }
        if (rentalAgreement.getReturnLocationId() != null) {
            existingAgreement.setReturnLocationId(rentalAgreement.getReturnLocationId());
        }

        if (rentalAgreement.getLeasingCode() != null) {
            existingAgreement.setLeasingCode(rentalAgreement.getLeasingCode());
        } else {

            existingAgreement.setLeasingCode(rentalAgreement.getLeasingCode());
        }


        rentalAgreementRepository.update(existingAgreement);
    }
    public void delete(int id) {
        rentalAgreementRepository.delete(id);
    }

    public int countAllRentalAgreements() {
        return rentalAgreementRepository.countAllRentalAgreements();
    }

    public int findNextId() {
        List<RentalAgreement> all = rentalAgreementRepository.findAll();
        return all.stream()
                .mapToInt(RentalAgreement::getRentalAgreementId)
                .max()
                .orElse(0) + 1;
    }

    public boolean hasActiveRental(int customerId) {
        return rentalAgreementRepository.findAll().stream()
                .filter(ra -> ra.getCustomerId() == customerId)
                .anyMatch(ra -> {
                    LocalDate now = LocalDate.now();
                    return !now.isBefore(ra.getStartDate()) && !now.isAfter(ra.getEndDate());
                });
    }

}
