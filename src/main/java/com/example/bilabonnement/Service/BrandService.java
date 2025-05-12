package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Brand;
import com.example.bilabonnement.Repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> findAllBrands() {
        return brandRepository.findAllBrands();
    }


}
