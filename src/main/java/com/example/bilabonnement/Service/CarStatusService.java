package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.CarStatus;
import com.example.bilabonnement.Repository.CarStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarStatusService {
    @Autowired
    private CarStatusRepository carStatusRepository;

    public List<CarStatus> findAllStatuses() {
        return carStatusRepository.findAllStatuses();
    }

    public CarStatus findCarStatusById(int statusId) {
        return carStatusRepository.findCarStatusById(statusId);
    }
} 