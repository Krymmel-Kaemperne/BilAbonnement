package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Zipcode;
import com.example.bilabonnement.Repository.ZipcodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZipcodeService {

    @Autowired
    private ZipcodeRepository zipcodeRepository;

    public List<Zipcode> findAllZipcodes() {
        return zipcodeRepository.findAll();
    }

    public Zipcode findById(int id) {
        return zipcodeRepository.findById(id);
    }
}
