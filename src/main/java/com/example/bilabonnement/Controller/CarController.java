package com.example.bilabonnement.Controller;
import com.example.bilabonnement.Model.*;
import org.springframework.ui.Model;
import com.example.bilabonnement.Service.BrandService;
import com.example.bilabonnement.Service.CarService;
import com.example.bilabonnement.Service.FuelTypeService;
import com.example.bilabonnement.Service.ModelService;
import com.example.bilabonnement.Service.CarStatusService;
import com.example.bilabonnement.Service.TransmissionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
        List<CarStatus> allCarStatuses = carStatusService.findAllStatuses();
        model.addAttribute("allCarStatuses", allCarStatuses);
        List<TransmissionType> allTransmissionTypes = transmissionTypeService.findAllTransmissionTypes();
        model.addAttribute("allTransmissionTypes", allTransmissionTypes);
        return "dataRegistration/createCar";
    }

    @PostMapping("/createCar")
    public String createCar(@ModelAttribute Car car, RedirectAttributes redirectAttributes) {
        try {
            com.example.bilabonnement.Model.Model carModelEntity = modelService.findByBrandIdAndModelName(car.getBrandId(), car.getModelName());
            if (carModelEntity == null) {
                carModelEntity = modelService.create(new com.example.bilabonnement.Model.Model(car.getModelName(), car.getBrandId()));
            }
            car.setModelId(carModelEntity.getModelId());
            Car createdCar = carService.create(car);
            if (createdCar != null && createdCar.getCarId() > 0) {
                redirectAttributes.addFlashAttribute("successMessage", "Bil '" + createdCar.getRegistrationNumber() + "' oprettet succesfuldt!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Fejl ved oprettelse af bil.");
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Fejl ved oprettelse af bil: " + e.getMessage());
            return "redirect:/dataRegistration/createCar"; // Redirect back to form with error
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Uventet fejl ved oprettelse af bil.");
            // Log exception e
        }
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
        List<com.example.bilabonnement.Model.CarStatus> allCarStatuses = carStatusService.findAllStatuses();
        model.addAttribute("allCarStatuses", allCarStatuses);
        List<com.example.bilabonnement.Model.TransmissionType> allTransmissionTypes = transmissionTypeService.findAllTransmissionTypes();
        model.addAttribute("allTransmissionTypes", allTransmissionTypes);
        return "dataRegistration/editCar";
    }

    @PostMapping("/car/update")
    public String updateCar(@ModelAttribute Car car, RedirectAttributes redirectAttributes) {

        // Find biler i databasen
        Car existingCar = carService.findById(car.getCarId());

        // Status før og efter.
        String oldStatus = carStatusService.findCarStatusById(existingCar.getCarStatusId()).getStatusName();
        String newStatus = carStatusService.findCarStatusById(car.getCarStatusId()).getStatusName();

        // STOP 'Udlejet' from going to 'Tilgængelig' or Solgt
        if ("Udlejet".equalsIgnoreCase(oldStatus)
                && ("Tilgængelig".equalsIgnoreCase(newStatus) || "Solgt".equalsIgnoreCase(newStatus))) {

            redirectAttributes.addFlashAttribute("errorMessage",
                    "Bilen har status 'Udlejet' og kan ikke ændres til 'Tilgængelig' eller 'Solgt'.");
            return "redirect:/dataRegistration/car/edit/" + car.getCarId();
        }

        com.example.bilabonnement.Model.Model currentModel = modelService.findById(existingCar.getModelId());

        if(currentModel != null) {
            if(!currentModel.getModelName().equals(car.getModelName())) {
                currentModel.setModelName(car.getModelName());

                boolean modelUpdated = modelService.update(currentModel) != null;
                if (!modelUpdated) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Fejl ved opdatering af modelnavn for bil med ID " + car.getCarId() + ".");
                    return "redirect:/dataRegistration/car/edit/" + car.getCarId();
                }
            }
        }

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
        return "redirect:/fleet/overview";
    }
}