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
// TODO: Importer CarStatusService, TransmissionTypeService
// import com.example.bilabonnement.Service.CarStatusService;
// import com.example.bilabonnement.Service.TransmissionTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam; // Til filtre senere

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/fleet") // Ny base path for alt flåde-relateret
public class FleetController {

    private final CarService carService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final FuelTypeService fuelTypeService;
    // TODO: Injicer CarStatusService og TransmissionTypeService
    // private final CarStatusService carStatusService;
    // private final TransmissionTypeService transmissionTypeService;

    @Autowired
    public FleetController(CarService carService,
                           BrandService brandService,
                           ModelService modelService,
                           FuelTypeService fuelTypeService
            /* TODO: Tilføj CarStatusService, TransmissionTypeService her */) {
        this.carService = carService;
        this.brandService = brandService;
        this.modelService = modelService;
        this.fuelTypeService = fuelTypeService;
        // TODO: this.carStatusService = carStatusService;
        // TODO: this.transmissionTypeService = transmissionTypeService;
    }

    @GetMapping("/overview") // URL bliver /fleet/overview
    public String showFleetOverview(
            // TODO: Tilføj @RequestParam for filterparametre
            org.springframework.ui.Model model // Spring's Model
    ) {
        List<Car> cars = carService.findAllCars();
        model.addAttribute("cars", cars);

        List<Brand> availableBrands = brandService.findAllBrands();
        model.addAttribute("availableBrands", availableBrands);

        List<com.example.bilabonnement.Model.Model> availableModels = modelService.findAllModels();
        model.addAttribute("availableModels", availableModels);

        List<FuelType> availableFuelTypes = fuelTypeService.findAllFuelTypes();
        model.addAttribute("availableFuelTypes", availableFuelTypes);

        // TODO: Hent og tilføj data for Status og TransmissionType filter dropdowns
        // List<CarStatus> availableStatuses = carStatusService.findAllStatuses();
        // model.addAttribute("availableStatuses", availableStatuses);
        // List<TransmissionType> availableTransmissionTypes = transmissionTypeService.findAllTransmissionTypes();
        // model.addAttribute("availableTransmissionTypes", availableTransmissionTypes);

        if (!model.containsAttribute("availableStatuses")) {
            model.addAttribute("availableStatuses", Collections.emptyList());
        }
        if (!model.containsAttribute("availableTransmissionTypes")) {
            model.addAttribute("availableTransmissionTypes", Collections.emptyList());
        }
        // Bestem hvilken HTML-fil der skal vises.
        // Hvis din fleet-overview.html ligger i templates/dataRegistration/
        // return "dataRegistration/fleet-overview";
        // Hvis den ligger direkte i templates/
        System.out.println("Fleet overview called, cars: " + cars.size());
        return "dataRegistration/fleet"; // Antager den ligger i templates/fleet-overview.html
    }

    @GetMapping("/details/{id}") // URL bliver /fleet/details/{id}
    public String showCarDetails(@PathVariable("id") int carId, org.springframework.ui.Model model) {
        Car car = carService.findById(carId);
        if (car == null) {
            // Overvej at tilføje en flash-attribut med fejlmeddelelse
            return "redirect:/fleet/overview"; // Omdiriger til flådeoversigten
        }
        model.addAttribute("car", car);
        // Bestem hvilken HTML-fil der skal vises for detaljer.
        // Hvis din car-details.html ligger i templates/dataRegistration/
        // return "dataRegistration/car-details";
        // Hvis den ligger direkte i templates/
        return "dataRegistration/view-car-details"; // Antager den ligger i templates/car-details.html
    }
}
