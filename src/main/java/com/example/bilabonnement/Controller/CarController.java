package com.example.bilabonnement.Controller;

import com.example.bilabonnement.Model.*;
// Importer CarStatus, TransmissionType modeller/DTO'er

import com.example.bilabonnement.Service.*;
import org.springframework.ui.Model;
// Importer CarStatusService, TransmissionTypeService

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/dataRegistration") // Beholder denne base path for opret/rediger bil
public class CarController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private FuelTypeService fuelTypeService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private CarService carService;
    @Autowired
    private CarStatusService carStatusService;
    @Autowired
    private TransmissionTypeService transmissionTypeService;

    @GetMapping("/createCar")
    public String showCreateCarForm(Model model) {
        model.addAttribute("car", new Car());
        List<Brand> allBrands = brandService.findAllBrands();
        model.addAttribute("allBrands", allBrands);
        List<FuelType> allFuelTypes = fuelTypeService.findAllFuelTypes();
        model.addAttribute("allFuelTypes", allFuelTypes);

        // TODO: Hent og tilføj data for CarStatus og TransmissionType dropdowns
        if (!model.containsAttribute("allCarStatuses")) {
            model.addAttribute("allCarStatuses", Collections.emptyList());
        }
        if (!model.containsAttribute("allTransmissionTypes")) {
            model.addAttribute("allTransmissionTypes", Collections.emptyList());
        }
        return "dataRegistration/createCar";
    }

    @PostMapping("/createCar")
    public String createCar(@ModelAttribute Car car) {
        com.example.bilabonnement.Model.Model carModelEntity = modelService.findByBrandIdAndModelName(car.getBrandId(), car.getModelName());
        if (carModelEntity == null) {
            carModelEntity = modelService.create(new com.example.bilabonnement.Model.Model(car.getModelName(), car.getBrandId()));
        }
        car.setModelId(carModelEntity.getModelId());
        carService.create(car);
        return "redirect:/fleet/overview"; // OPDATERET REDIRECT
    }

    @GetMapping("/car/edit/{id}")
    public String showEditCarForm(@PathVariable("id") int carId, Model model, RedirectAttributes redirectAttributes) {
        Car car = carService.findById(carId);
        if (car == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bil med ID " + carId + " blev ikke fundet.");
            return "redirect:/fleet/overview"; // OPDATERET REDIRECT
        }
        model.addAttribute("car", car);
        List<Brand> allBrands = brandService.findAllBrands();
        model.addAttribute("allBrands", allBrands);
        List<FuelType> allFuelTypes = fuelTypeService.findAllFuelTypes();
        model.addAttribute("allFuelTypes", allFuelTypes);
        List<CarStatus> allCarStatusTypes  = carStatusService.findAllStatuses();
        model.addAttribute("allCarStatusTypes", allCarStatusTypes);
        List<TransmissionType> allTransmissionTypes = transmissionTypeService.findAllTransmissionTypes();
        model.addAttribute("allTransmissiontypes", allTransmissionTypes);

        return "dataRegistration/editCar";
    }

    @PostMapping("/car/update")
    public String updateCar(@ModelAttribute Car car, RedirectAttributes redirectAttributes) {
        com.example.bilabonnement.Model.Model carModelEntity = modelService.findByBrandIdAndModelName(car.getBrandId(), car.getModelName());
        if (carModelEntity == null) {
            carModelEntity = modelService.create(new com.example.bilabonnement.Model.Model(car.getModelName(), car.getBrandId()));
        }
        if (carModelEntity != null && carModelEntity.getModelId() != null) {
            car.setModelId(carModelEntity.getModelId());
            Car updatedCar = carService.update(car);
            if (updatedCar != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Bil '" + car.getRegistrationNumber() + "' opdateret succesfuldt!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Fejl ved opdatering af bil med ID " + car.getCarId() + ".");
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Fejl: Kunne ikke oprette eller finde bilmodel under opdatering.");
        }
        return "redirect:/fleet/overview"; // OPDATERET REDIRECT
    }
}
