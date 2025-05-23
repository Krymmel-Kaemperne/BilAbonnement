package com.example.bilabonnement.Controller;

import com.example.bilabonnement.Model.Car;
import com.example.bilabonnement.Model.Brand;
import com.example.bilabonnement.Model.FuelType;
import com.example.bilabonnement.Model.Model;
import com.example.bilabonnement.Model.CarStatus;
import com.example.bilabonnement.Model.TransmissionType;
import com.example.bilabonnement.Service.BrandService;
import com.example.bilabonnement.Service.CarService;
import com.example.bilabonnement.Service.FuelTypeService;
import com.example.bilabonnement.Service.ModelService;
import com.example.bilabonnement.Service.CarStatusService;
import com.example.bilabonnement.Service.TransmissionTypeService;
import com.example.bilabonnement.Service.CarStatusService;
import com.example.bilabonnement.Service.TransmissionTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/fleet")
public class FleetController {

    @Autowired
    private CarService carService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private FuelTypeService fuelTypeService;
    @Autowired
    private CarStatusService carStatusService;
    @Autowired
    private TransmissionTypeService transmissionTypeService;


    @GetMapping("/overview")
    public String showFleetOverview(
            @RequestParam(required = false) Integer brand,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false, name = "model") Integer modelId,
            @RequestParam(required = false) Integer fuelType,
            @RequestParam(required = false) Integer transmissionType,
            @RequestParam(required = false) Integer searchCarId,
            org.springframework.ui.Model model,
            jakarta.servlet.http.HttpServletResponse response
    ) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

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

        List<Model> availableModels = modelService.findAllModels();
        model.addAttribute("availableModels", availableModels);

        List<FuelType> availableFuelTypes = fuelTypeService.findAllFuelTypes();
        model.addAttribute("availableFuelTypes", availableFuelTypes);

        List<CarStatus> availableStatuses = carStatusService.findAllStatuses();
        model.addAttribute("availableStatuses", availableStatuses);

        Map<Integer, String> statusNames = availableStatuses.stream()
                .collect(Collectors.toMap(
                        CarStatus::getCarStatusId,
                        CarStatus::getStatusName
                ));
        model.addAttribute("statusNames", statusNames);

        List<TransmissionType> availableTransmissionTypes = transmissionTypeService.findAllTransmissionTypes();
        model.addAttribute("availableTransmissionTypes", availableTransmissionTypes);

        return "dataRegistration/fleet";
    }

    @PostMapping("/updateStatus")
    public String updateCarStatus(
            @RequestParam("carId") int carId,
            @RequestParam("newStatusId") int newStatusId, // Dette er ID'et fra <select>
            RedirectAttributes redirectAttributes
    ) {
        Car car = carService.findById(carId);
        if (car != null) {
            car.setCarStatusId(newStatusId); // Opdaterer bilens status ID
            Car updatedCar = carService.update(car); // Gemmer ændringen
            if (updatedCar != null) {
                // Forbedret succesmeddelelse med statusnavn
                com.example.bilabonnement.Model.CarStatus newStatus =
                        carStatusService.findCarStatusById(newStatusId); // Antager du har en sådan metode
                String statusName = (newStatus != null)
                        ? newStatus.getStatusName() : "Ukendt";

                redirectAttributes.addFlashAttribute(
                        "successMessage", "Status for bil ID " + carId + " (" + updatedCar.getRegistrationNumber() +
                                ") opdateret til '" + statusName + "'.");
            } else {
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "Fejl: Kunne ikke opdatere status for bil ID " + carId + "."
                );
            }
        } else {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Fejl: Bil med ID " + carId + " blev ikke fundet."
            );
        }
        return "redirect:/fleet/overview";
    }

    @GetMapping("/details/{id}") // URL bliver /fleet/details/{id}
    public String showCarDetails(@PathVariable("id") int carId, org.springframework.ui.Model model) {
        Car car = carService.findById(carId);
        if (car == null) {
            return "redirect:/fleet/overview"; // Omdiriger til flådeoversigten
        }
        model.addAttribute("car", car);

        // Returner den korrekte template for detaljer.
        return "dataRegistration/view-car-details";

    }
}
