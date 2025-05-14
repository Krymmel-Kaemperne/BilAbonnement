package com.example.bilabonnement.Controller;

import com.example.bilabonnement.Model.Car;
import com.example.bilabonnement.Model.Brand;
import com.example.bilabonnement.Model.FuelType;
import com.example.bilabonnement.Model.Model; // Din bilmodel-klasse
// TODO: Importer CarStatus, TransmissionType modeller/DTO'er
// import com.example.bilabonnement.Model.CarStatus;
// import com.example.bilabonnement.Model.TransmissionType;

//import org.springframework.ui.Model; // Spring's Model
import com.example.bilabonnement.Service.BrandService;
import com.example.bilabonnement.Service.CarService;
import com.example.bilabonnement.Service.FuelTypeService;
import com.example.bilabonnement.Service.ModelService;
import com.example.bilabonnement.Service.CarStatusService;
import com.example.bilabonnement.Service.TransmissionTypeService;
// TODO: Importer CarStatusService, TransmissionTypeService
// import com.example.bilabonnement.Service.CarStatusService;
// import com.example.bilabonnement.Service.TransmissionTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/fleet") // Ny base path for alt flåde-relateret
public class FleetController {

    private final CarService carService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final FuelTypeService fuelTypeService;
    private final CarStatusService carStatusService;
    private final TransmissionTypeService transmissionTypeService;

    @Autowired
    public FleetController(CarService carService,
                           BrandService brandService,
                           ModelService modelService,
                           FuelTypeService fuelTypeService,
                           CarStatusService carStatusService,
                           TransmissionTypeService transmissionTypeService) {
        this.carService = carService;
        this.brandService = brandService;
        this.modelService = modelService;
        this.fuelTypeService = fuelTypeService;
        this.carStatusService = carStatusService;
        this.transmissionTypeService = transmissionTypeService;
    }

    @GetMapping("/overview")
    public String showFleetOverview(
            @RequestParam(required = false) Integer brand,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false, name = "model") Integer modelId,
            @RequestParam(required = false) Integer fuelType,
            @RequestParam(required = false) Integer transmissionType,
            @RequestParam(required = false) Integer searchCarId,
            org.springframework.ui.Model model
    ) {
        List<Car> cars;
        if (searchCarId != null) {
            Car car = carService.findById(searchCarId);
            cars = (car != null) ? List.of(car) : List.of();
        } else {
            cars = carService.findCarsByFilters(brand, status, modelId, fuelType, transmissionType);
        }
        model.addAttribute("cars", cars);

        List<Brand> availableBrands = brandService.findAllBrands();
        model.addAttribute("availableBrands", availableBrands);

        List<com.example.bilabonnement.Model.Model> availableModels = modelService.findAllModels();
        model.addAttribute("availableModels", availableModels);

        List<FuelType> availableFuelTypes = fuelTypeService.findAllFuelTypes();
        model.addAttribute("availableFuelTypes", availableFuelTypes);

        List<com.example.bilabonnement.Model.CarStatus> availableStatuses = carStatusService.findAllStatuses();
        model.addAttribute("availableStatuses", availableStatuses);
        List<com.example.bilabonnement.Model.TransmissionType> availableTransmissionTypes = transmissionTypeService.findAllTransmissionTypes();
        model.addAttribute("availableTransmissionTypes", availableTransmissionTypes);

        System.out.println("Fleet overview called, cars: " + cars.size());
        return "dataRegistration/fleet";
    }

    @GetMapping("/details/{id}") // URL bliver /fleet/details/{id}
    public String showCarDetails(@PathVariable("id") int carId, org.springframework.ui.Model model) {
        Car car = carService.findById(carId);
        if (car == null) {
            // Overvej at tilføje en flash-attribut med fejlmeddelelse
            return "redirect:/fleet/overview"; // Omdiriger til flådeoversigten
        }
        model.addAttribute("car", car);

        // Returner den korrekte template for detaljer.
        return "dataRegistration/view-car-details";

    }
}
