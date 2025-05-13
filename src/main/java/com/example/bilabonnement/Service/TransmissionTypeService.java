package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.TransmissionType;
import com.example.bilabonnement.Repository.TransmissionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransmissionTypeService {
    @Autowired
    private TransmissionTypeRepository transmissionTypeRepository;

    public List<TransmissionType> findAllTransmissionTypes() {
        return transmissionTypeRepository.findAllTransmissionTypes();
    }
} 