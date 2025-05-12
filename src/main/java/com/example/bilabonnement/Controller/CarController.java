package com.example.bilabonnement.Controller;

import com.example.bilabonnement.Model.Car;
import com.example.bilabonnement.Model.Brand;
import com.example.bilabonnement.Model.FuelType;
import com.example.bilabonnement.Model.Model;

import com.example.bilabonnement.Repository.BrandRepository;
import com.example.bilabonnement.Repository.FuelTypeRepository;
import com.example.bilabonnement.Repository.ModelRepository;
import com.example.bilabonnement.Repository.CarRepository;
import com.example.bilabonnement.Service.BrandService;
import com.example.bilabonnement.Service.CarService;
import com.example.bilabonnement.Service.FuelTypeService;
import com.example.bilabonnement.Service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dataRegistration")
public class CarController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private FuelTypeService fuelTypeService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private CarService carService;



    @GetMapping("/createCar")
    public String showCreateCarForm(org.springframework.ui.Model model) {
        model.addAttribute("car", new Car());
        List<Brand> allBrands = brandService.findAllBrands();
        model.addAttribute("allBrands", allBrands);
        List<FuelType> allFuelTypes = fuelTypeService.findAllFuelTypes();
        model.addAttribute("allFuelTypes", allFuelTypes);
        return "dataRegistration/createCar";
    }

    @PostMapping("/createCar")
    public String createCar(@ModelAttribute Car car) {
        // Find or create the model
        Model model = modelService.findByBrandIdAndModelName(car.getBrandId(), car.getModelName());
        if (model == null) {
            model = modelService.create(new Model(car.getModelName(), car.getBrandId()));
        }
        car.setModelId(model.getModelId());
        // Save the car to the database
        carService.create(car);
        System.out.println("Car saved: " + car);
        return "redirect:/dataRegistration/fleet";
    }
}
