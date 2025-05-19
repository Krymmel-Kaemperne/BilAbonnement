package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.RentalAgreement;
import com.example.bilabonnement.Repository.RentalAgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void update(RentalAgreement rentalAgreement) {
        rentalAgreementRepository.update(rentalAgreement);
    }

    public void delete(int id) {
        rentalAgreementRepository.delete(id);
    }

    public int countAllRentalAgreements() {
        return rentalAgreementRepository.countAllRentalAgreements();
    }

}
