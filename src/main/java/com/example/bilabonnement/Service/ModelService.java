package com.example.bilabonnement.Service;

import com.example.bilabonnement.Model.Model;
import com.example.bilabonnement.Repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Model update(Model model) {
        return modelRepository.update(model);
    }

    public List<Model> findAllModels() {
        return modelRepository.findAllModels();
    }

    public Model findById(int id) {
        return modelRepository.findById(id);
    }
}
