package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Location;
import com.example.bilabonnement.Repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private LocaleResolver localeResolver;

    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }

    public Location findLocationById(int id) {
        if(id <= 0) {
            return null;
        }
        return locationRepository.findById(id);
    }

}
