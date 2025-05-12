package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.FuelType;
import com.example.bilabonnement.Repository.FuelTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuelTypeService {

    @Autowired
    private FuelTypeRepository fuelTypeRepository;

    public List<FuelType> findAllFuelTypes() {
        return fuelTypeRepository.findAllFuelTypes();
    }
}
