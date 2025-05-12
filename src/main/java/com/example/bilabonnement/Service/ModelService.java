package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Model;
import com.example.bilabonnement.Repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelService {

    @Autowired
    private ModelRepository modelRepository;

    public Model findByBrandIdAndModelName(Integer brandId, String modelName) {
        return modelRepository.findByBrandIdAndModelName(brandId, modelName);
    }

    public Model create(Model model) {
        return modelRepository.create(model);
    }
}
